package io.github.dinolupo.airticket.business.flights.boundary;

import io.github.dinolupo.airticket.business.flights.entity.Flight;
import io.github.dinolupo.airticket.business.flights.entity.FlightStatus;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by dinolupo.github.io on 30/07/16.
 */
public class FlightResource {

    long id;
    FlightsManager flightsManager;


    public FlightResource(long id, FlightsManager flightsManager) {
        this.id = id;
        this.flightsManager = flightsManager;
    }

    @GET
    public Flight find(){
        return flightsManager.findById(id);
    }


    @DELETE
    public void delete() {
        flightsManager.delete(id);
    }

    @PUT
    public void update(Flight todo) {
        todo.setId(id);
        flightsManager.save(todo);
    }


    @PUT
    @Path("/status")
    public Response statusUpdate(JsonObject status) {
        if (!status.containsKey("flightStatus")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","JSON does not contain required key 'flightStatus'")
                    .build();
        }

        FlightStatus flightStatus;
        try {
            flightStatus = FlightStatus.valueOf(status.getString("flightStatus"));
        } catch (IllegalArgumentException ex) {
            flightStatus = FlightStatus.UNKNOWN;
        }

        Flight flight = flightsManager.updateStatus(id, flightStatus);
        if (flight == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","Flight with id " + id + " does not exist.")
                    .build();
        } else {
            return Response.ok(flight).build();
        }
    }

}
