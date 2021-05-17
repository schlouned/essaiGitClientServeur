/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ue_ee_ds.resources;

import be.isl.ue.business.PersonFacade;
import be.isl.ue.entity.Person;
import be.isl.ue.vm.PersonSearchVM;
import java.util.List;
import javax.inject.Inject;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Schloune Denis
 */
@Path("Persons")
public class PersonService {
    //inject dependency with facade
    @Inject
    PersonFacade ejb;
    
    //get for all persons without any filter
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("All")
    @JsonbDateFormat
    public List<Person> getAll(){
        return ejb.findAll();
    }
    
    //get for a person on the name
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonbDateFormat
    public List<Person> getByName(@DefaultValue("") @QueryParam("name") String name){
        PersonSearchVM s = new PersonSearchVM();
        s.setLastName(name);
        
        return ejb.findVM(s);
    }
    
    //get by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonbDateFormat
    public Person find(@PathParam("id") Integer id){
        return ejb.find(id);
    }
    
    //count
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST(){
        return String.valueOf(ejb.count());
    }
    
      //findRange
    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonbDateFormat
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to){
        return ejb.findRange(new int[]{from, to});
    }
    
    //create
    @POST
    @Consumes((MediaType.APPLICATION_JSON))
    public void create(Person entity){
        ejb.create(entity);
    }
    
    //update
    @PUT
    @Path("{id}")
    @Consumes((MediaType.APPLICATION_JSON))
    public void edit(@PathParam("id") Integer id, Person entity){
        ejb.edit(entity);
    }
    
    //delete
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id){
        ejb.remove(ejb.find(id));
    }
    
  
}
