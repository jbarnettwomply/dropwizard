package io.dropwizard.jersey.sessions;

import org.glassfish.hk2.api.Factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

public final class FlashFactory implements Factory<Flash<?>> {
    @Context
    private HttpServletRequest request;
    private boolean doNotCreate;

    public FlashFactory(boolean doNotCreate) {
        this.doNotCreate = doNotCreate;
    }

    @SuppressWarnings("rawtypes")
    public Flash<?> provide() {
        if (request == null) {
            return null;
        }

        final HttpSession session = request.getSession(!this.doNotCreate);
        if (session != null) {
            return new Flash(session);
        }

        return null;
    }

    public void dispose(Flash<?> flash) {
        // not used
    }
}
