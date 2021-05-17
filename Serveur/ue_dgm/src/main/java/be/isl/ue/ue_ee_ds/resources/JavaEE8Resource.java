package be.isl.ue.ue_ee_ds.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("ueApplication")
public class JavaEE8Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
}
