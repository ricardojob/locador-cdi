package ifpb.ads.exemplo.inject;

import ifpb.locator.ServerContext;
import ifpb.locator.named.App;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 17/06/2016, 16:08:07
 */
//@Named
//@Dependent
class CustomInjectionTargets<X> implements InjectionTarget<X> {

    private final InjectionTarget<X> it;
    private final AnnotatedType<X> at;
    private final ProcessInjectionTarget<X> pit;

    
    public CustomInjectionTargets() {
        this.it = null;
        this.at = null;
        this.pit = null;
    }

//    @Inject
    private ServerContext app;

    
     
    public CustomInjectionTargets(ProcessInjectionTarget<X> pit, ServerContext context) {
        this.it = pit.getInjectionTarget();
        this.at = pit.getAnnotatedType();
        this.pit = pit;
        this.app = context;
    }

    @Override
    public X produce(CreationalContext<X> ctx) {
        return it.produce(ctx);
    }

    @Override
    public void dispose(X instance) {
        it.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return it.getInjectionPoints();
    }

    // The container calls inject() method when it's performing field
    // injection and calling bean initializer methods.
    // Our custom wrapper will also check for fields annotated with
    // @Property and resolve them by invoking the Property Resolver
    // method
    @Override
    public void inject(X instance, CreationalContext<X> ctx) {
        it.inject(instance, ctx);
        for (Field field : at.getJavaClass().getDeclaredFields()) {
            Locator annotation = field.getAnnotation(Locator.class);
            if (annotation != null) {
                try {
                    String value = annotation.value();
                    field.setAccessible(true);
                    Class<?> baseType = field.getType();

                    String ap = app == null ? "nulo" : app.bean(value).name();
                    if (baseType == String.class) {
                        field.set(instance, value + " - " + ap);
                    } else if (baseType == Integer.class) {
                        field.set(instance, Integer.valueOf(value));
                    } else {
                        pit.addDefinitionError(new InjectionException("Type " + baseType + " of Field " + value + " not recognized yet!"));
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(
                            "Could not resolve property", e);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(PropertyLoaderExtension.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void postConstruct(X instance) {
        it.postConstruct(instance);
    }

    @Override
    public void preDestroy(X instance) {
        it.preDestroy(instance);
    }

}
