package io.dropwizard.jersey.sessions;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

@Singleton
public class SessionFactoryProvider extends AbstractValueParamProvider {

    @Inject
    public SessionFactoryProvider(final Provider<MultivaluedParameterExtractorProvider> extractorProvider) {
        super(extractorProvider, Parameter.Source.UNKNOWN);
    }

    @Override
    protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
        final Class<?> classType = parameter.getRawType();

        final Session sessionAnnotation = parameter.getAnnotation(Session.class);
        if (sessionAnnotation == null) {
            return null;
        }

        if (classType.isAssignableFrom(HttpSession.class)) {
            return x -> new HttpSessionFactory(sessionAnnotation.doNotCreate());
        } else if (classType.isAssignableFrom(Flash.class)) {
            return x -> new FlashFactory(sessionAnnotation.doNotCreate());
        } else {
            return null;
        }
    }

//    public static class SessionInjectionResolver extends ParamInjectionResolver<Session> {
//        @Inject
//        public SessionInjectionResolver(SessionFactoryProvider provider) {
//            super(provider, Session.class, null);
//        }
//    }

    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(SessionFactoryProvider.class).to(AbstractValueParamProvider.class).in(Singleton.class);
//            bind(SessionInjectionResolver.class).to(
//                    new TypeLiteral<InjectionResolver<Session>>() {
//                    }
//            ).in(Singleton.class);
        }
    }

    public static class SessionFeature implements Feature {

        @Override
        public boolean configure(FeatureContext context) {
            context.register(new Binder());
            return true;
        }
    }
}

