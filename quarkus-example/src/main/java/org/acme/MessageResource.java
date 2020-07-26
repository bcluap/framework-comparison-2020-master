package org.acme;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class MessageResource {

    @Inject
    MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@NotBlank @PathParam("name") String name, @QueryParam("sleep") long sleep) {
        return messageService.sayHello(name, sleep);
    }
    
    @Route(path = "/hello2/:name", methods = HttpMethod.GET)
    void greetings(RoutingExchange ex) { 
        ex.ok( messageService.sayHello(ex.getParam("name").get(), Long.parseLong(ex.getParam("sleep").orElse("0"))));
    }
}