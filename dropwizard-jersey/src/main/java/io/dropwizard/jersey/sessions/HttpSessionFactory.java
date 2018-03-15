package io.dropwizard.jersey.sessions;

import org.glassfish.hk2.api.Factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

public final class HttpSessionFactory implements Factory<HttpSession> {
    @Context
    private HttpServletRequest request;
    private boolean doNotCreate;

    public HttpSessionFactory(boolean doNotCreate) {
        this.doNotCreate = doNotCreate;
    }

    public HttpSession provide() {
        if (request == null) {
            return null;
        }

        return request.getSession(!doNotCreate);
    }

    public void dispose(HttpSession httpSession) {
        // not used
    }
}
