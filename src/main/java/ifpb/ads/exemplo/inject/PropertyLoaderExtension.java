package ifpb.ads.exemplo.inject;

import ifpb.locator.ServerContext;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.inject.Inject;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 17/06/2016, 01:22:23
 */
public class PropertyLoaderExtension implements Extension {

//    @Inject
    private ServerContext context;

    <T, E> void processInjectionPoint(@Observes ProcessInjectionPoint<T, E> event)
            throws Exception {
        InjectionPoint injectionPoint = event.getInjectionPoint();

        Locator qualifier = injectionPoint.getAnnotated().getAnnotation(
                Locator.class);
        if (qualifier == null) {
            return;
        }
        
        String key = qualifier.value();
        Class<?> clazz = injectionPoint.getMember().getDeclaringClass();

//        System.out.println("calc: "+context.bean(key).withInterface(clazz).locate());
        
        event.setInjectionPoint(injectionPoint);
    }

    public void a(@Observes ServerContext context) {
        this.context = context;
        System.out.println("---------------\t\tContext");
    }

    <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
//    <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit, BeanManager beanManager) {
//System.out.println("continuouuu---"+pit.getAnnotatedType());
        InjectionTarget<X> wrapper = new CustomInjectionTarget<>(pit); //{
        pit.setInjectionTarget(wrapper);
    }

//     Set<AnnotatedField<? super T>>  fields;
    /*  public <T> void initializePropertyLoading(final @Observes ProcessInjectionTarget<T> pit) {
        System.out.println("\t initialize proterpies");
        AnnotatedType<T> at = pit.getAnnotatedType();
        System.out.println("\tat: " + at.getJavaClass());
        System.out.println("\tan: " + pit.getAnnotatedType());

//        AnnotatedType<X> at = pit.getAnnotatedType();
        Set<AnnotatedField<? super T>> fields = at.getFields();
//        Set<AnnotatedField<? super T>> fieldValues = at.getFields();

        fields.stream()
                .filter(field -> field.isAnnotationPresent(Locator.class))
                .forEach(var -> {
                    pit.setInjectionTarget(
                            new CacheInjectionTarget(pit.getInjectionTarget()));
                });
    }
    /*
        if (!at.isAnnotationPresent(Locator.class)) {
            return;
        }

        Locator locator = at.getAnnotation(Locator.class);
        String name = locator.value();
        System.out.println("\tvalor---" + name);
        final InjectionTarget<T> it = pit.getInjectionTarget();
        InjectionTarget<T> wrapped = new InjectionTarget<T>() {
            @Override
            public void inject(T instance, CreationalContext<T> ctx) {
                it.inject(instance, ctx);
                for (Map.Entry<Field, Object> property : fieldValues.entrySet()) {
                    try {
                        Field key = property.getKey();
                        key.setAccessible(true);
                        Class<?> baseType = key.getType();
                        String value = property.getValue().toString();
                        if (baseType == String.class) {
                            key.set(instance, value);
                        } else if (baseType == Integer.class) {
                            key.set(instance, Integer.valueOf(value));
                        } else {
                            pit.addDefinitionError(new InjectionException("Type " + baseType + " of Field " + key.getName() + " not recognized yet!"));
                        }
                    } catch (Exception e) {
                        pit.addDefinitionError(new InjectionException(e));
                    }
                }
            }

            @Override
            public void postConstruct(T instance) {
                it.postConstruct(instance);
            }

            @Override
            public void preDestroy(T instance) {
                it.dispose(instance);
            }

            @Override
            public void dispose(T instance) {
                it.dispose(instance);
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            @Override
            public T produce(CreationalContext<T> ctx) {
                return it.produce(ctx);
            }
        };
        pit.setInjectionTarget(wrapped);
     
}
//  private <T> void assignPropertiesToFields(Set<AnnotatedField<? super T>> fields, Properties properties) {
//    for (AnnotatedField<? super T> field : fields) {
//      if(field.isAnnotationPresent(Propertyy.class)) {
//        Propertyy propertyy = field.getAnnotation(Propertyy.class);
//        Object value = properties.get(propertyy.value());
//        Field memberField = field.getJavaMember();
//        fieldValues.put(memberField, value);
//      }
//    }
//  }

class CacheInjectionTarget<X> implements InjectionTarget<X> {

    private InjectionTarget<X> it;

    public CacheInjectionTarget(InjectionTarget<X> it) {
        this.it = it;
    }

    @Override
    public void inject(X instance, CreationalContext<X> ctx) {

        it.inject(instance, ctx);

//        for (Map.Entry<Field, Object> property : fieldValues.entrySet()) {
//            try {
//                Field key = property.getKey();
//                key.setAccessible(true);
//                Class<?> baseType = key.getType();
//                String value = property.getValue().toString();
//                if (baseType == String.class) {
//                    key.set(instance, value);
//                } else if (baseType == Integer.class) {
//                    key.set(instance, Integer.valueOf(value));
//                } else {
//                    pit.addDefinitionError(new InjectionException("Type " + baseType + " of Field " + key.getName() + " not recognized yet!"));
//                }
//            } catch (Exception e) {
//                pit.addDefinitionError(new InjectionException(e));
//            }
//        }
    }

    @Override
    public void postConstruct(X instance) {
        it.postConstruct(instance);
    }

    @Override
    public void preDestroy(X instance) {
        it.dispose(instance);
    }

    @Override
    public void dispose(X instance) {
        it.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return it.getInjectionPoints();
    }

    @Override
    public X produce(CreationalContext<X> ctx) {
        return it.produce(ctx);
    }
     */
    class CustomInjectionTarget<X> implements InjectionTarget<X> {

        private final InjectionTarget<X> it;
        private final AnnotatedType<X> at;
        private final ProcessInjectionTarget<X> pit;

        public CustomInjectionTarget() {
            this.it = null;
            this.at = null;
            this.pit = null;
        }

        public CustomInjectionTarget(ProcessInjectionTarget<X> pit) {
            this.it = pit.getInjectionTarget();
            this.at = pit.getAnnotatedType();
            this.pit = pit;
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

                        if (baseType == String.class) {
                            field.set(instance, value + " - " + baseType.getCanonicalName() + " - " + context);
                        } else if (baseType == Integer.class) {
                            field.set(instance, Integer.valueOf(value));
                        } else {
                            Object obj = (context == null) ? null : context
                                    .bean(value)
                                    .withInterface(baseType).locate();
                            if (obj != null) {
                                field.set(instance, obj);
                            } else {
                               // pit.addDefinitionError(new InjectionException("Type " + baseType + " of Field " + value + " not recognized yet!"));
                            }
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
}
