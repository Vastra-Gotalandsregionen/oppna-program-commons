package se.vgregion.messagebus.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/test-rs")
@Produces("application/json")
public class RestService {

    @GET
    public String testing() {
        return "Testing";
    }

    @GET
    @Path("/echo")
    public String echo(String str) {
        return str;
    }
}
