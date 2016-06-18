package ifpb.locator.cdi.inject;

import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 18/06/2016, 01:57:10
 */
class AdapterInjectionTarget<X> implements InjectionTarget<X> {

    private final InjectionTarget<X> it;

    public AdapterInjectionTarget(InjectionTarget<X> it) {
        this.it = it;
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

    @Override
    public void inject(X instance, CreationalContext<X> ctx) {
        it.inject(instance, ctx);
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
