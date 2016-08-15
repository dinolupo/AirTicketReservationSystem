package io.github.dinolupo.airticket.business.flights.boundary;

import io.github.dinolupo.airticket.business.flights.entity.Flight;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by dinolupo.github.io on 30/07/16.
 */
@Stateless
@Path("flight")
public class FlightsResource {

    @Inject
    FlightsManager flightsManager;

    @Path("{id}")
    public FlightResource findById(@PathParam("id") long id){
        return new FlightResource(id, flightsManager);
    }

    @GET
    @Path("flightNumber/{flightNumber}")
    public Flight findByFlightNumber(@PathParam("flightNumber") String flightNumber){
        return flightsManager.findByFlightNumber(flightNumber);
    }

    @GET
    public List<Flight> all() {
        return flightsManager.findAll();
    }

    @POST
    public Response save(@Valid Flight todo, @Context UriInfo uriInfo) {
        Flight savedObject = flightsManager.save(todo);
        long id = savedObject.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        Response response = Response.created(uri).build();
        return response;
    }

}
