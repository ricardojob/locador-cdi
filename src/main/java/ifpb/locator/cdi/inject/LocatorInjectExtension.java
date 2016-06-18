package ifpb.locator.cdi.inject;

import ifpb.locator.ServerContext;
import ifpb.locator.cdi.Locator;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 17/06/2016, 01:22:23
 */
public class LocatorInjectExtension implements Extension {

    private ServerContext context;

    public void a(@Observes ServerContext context) {
        this.context = context;
    }

    <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
        InjectionTarget<X> wrapper = new DefaultInjectionTarget<>(pit);
        pit.setInjectionTarget(wrapper);
    }

    class DefaultInjectionTarget<X> extends AdapterInjectionTarget<X> {

        private final AnnotatedType<X> at;

        public DefaultInjectionTarget(ProcessInjectionTarget<X> pit) {
            super(pit.getInjectionTarget());
            this.at = pit.getAnnotatedType();
        }

        @Override
        public void inject(X instance, CreationalContext<X> ctx) {
            super.inject(instance, ctx);

            for (Field field : at.getJavaClass().getDeclaredFields()) {
                Locator annotation = field.getAnnotation(Locator.class);
                if (annotation != null) {
                    fieldResolver(annotation, field, instance);
                }
            }
        }

        private void fieldResolver(Locator annotation, Field field, X instance) {
            try {
                String value = annotation.value();
                field.setAccessible(true);
                Class<?> baseType = field.getType();
                Object obj = (context == null) ? null : context
                        .bean(value)
                        .withInterface(baseType).locate();
                field.set(instance, obj);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(
                        "Could not resolve property", e);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(LocatorInjectExtension.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
