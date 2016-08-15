package systemtest;

import org.junit.Before;
import org.junit.Test;

import javax.json.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by dinolupo.github.io on 30/07/16.
 */
public class FlightsResourceTest {

    private Client client;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/airticket/api/flight");
    }

    @Test
    public void crudTest() throws Exception {

        final String FLIGHT_NUMBER = "DLH2054";
        final String FROM = "Milan";
        final String TO = "Paris";
        final ZonedDateTime DEPARTURE_DATE = ZonedDateTime.now();
        final ZonedDateTime ARRIVAL_DATE = DEPARTURE_DATE.plusHours(2).plusMinutes(30);
        final String FLIGHT_STATUS = "SCHEDULED";

        final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .withLocale(Locale.getDefault());

        // create an object with POST
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject flightToCreate = jsonObjectBuilder
                .add("flightNumber", FLIGHT_NUMBER)
                .add("fromAirport", FROM)
                .add("toAirport", TO)
                .add("departureDate", DEPARTURE_DATE.format(formatter))
                .add("arrivalDate", ARRIVAL_DATE.format(formatter))
                .add("flightStatus", FLIGHT_STATUS)
        .build();

        Response postResponse = target.request().post(Entity.json(flightToCreate));
        assertThat(postResponse.getStatusInfo(),is(Response.Status.CREATED));

        String location = postResponse.getHeaderString("Location");
//        System.out.printf("location = %s\n", location);

        // GET {id}, using the location returned before
        JsonObject jsonObject = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertTrue(jsonObject.getString("flightNumber").equals(FLIGHT_NUMBER));
        assertTrue(jsonObject.getString("fromAirport").equals(FROM));
        assertTrue(jsonObject.getString("toAirport").equals(TO));
        assertTrue(ZonedDateTime.parse(jsonObject.getString("departureDate")).isEqual(DEPARTURE_DATE));
        assertTrue(ZonedDateTime.parse(jsonObject.getString("arrivalDate")).isEqual(ARRIVAL_DATE));
        assertTrue(jsonObject.getString("flightStatus").equals(FLIGHT_STATUS));

        // update with PUT
        final ZonedDateTime UPDATED_ARRIVAL_TIME = ARRIVAL_DATE.plusHours(1);
        final String UPDATED_STATUS_DELAYED = "DELAYED";
        JsonObjectBuilder updateObjectBuilder = Json.createObjectBuilder();
        JsonObject updated = updateObjectBuilder
                .add("flightNumber", FLIGHT_NUMBER)
                .add("fromAirport", FROM)
                .add("toAirport", TO)
                .add("departureDate", DEPARTURE_DATE.format(formatter))
                .add("arrivalDate", UPDATED_ARRIVAL_TIME.format(formatter)) //updated arrival date
                .add("flightStatus", UPDATED_STATUS_DELAYED) // updated flight status
                .build();
        Response updateResponse = client.target(location).request(MediaType.APPLICATION_JSON).put(Entity.json(updated));
        assertThat(updateResponse.getStatusInfo(), is(Response.Status.NO_CONTENT));

        location = postResponse.getHeaderString("Location");
        System.out.printf("location = %s\n", location);

        // update again to verify Optimistick Lock exception (we don' send back the version, so it is assumed to be 0 like before and the update will fail
        updateObjectBuilder = Json.createObjectBuilder();
        updated = updateObjectBuilder
                .add("flightNumber", FLIGHT_NUMBER)
                .add("fromAirport", FROM)
                .add("toAirport", TO)
                .add("departureDate", DEPARTURE_DATE.format(formatter))
                .add("arrivalDate", ARRIVAL_DATE.format(formatter)) //updated arrival date
                .add("flightStatus", "ONTIME") // updated flight status
                .build();
        updateResponse = client.target(location).request(MediaType.APPLICATION_JSON).put(Entity.json(updated));
        assertThat(updateResponse.getStatusInfo(), is(Response.Status.CONFLICT));
        assertTrue(updateResponse.getHeaderString("cause").startsWith("conflict occurred:"));

        // find again with GET {id} and verify fields were updated
        JsonObject updatedFlight = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        final long OBJECT_ID = updatedFlight.getInt("id");

        assertTrue(updatedFlight.getString("flightNumber").equals(FLIGHT_NUMBER));
        assertTrue(updatedFlight.getString("fromAirport").equals(FROM));
        assertTrue(updatedFlight.getString("toAirport").equals(TO));
        assertTrue(ZonedDateTime.parse(updatedFlight.getString("departureDate")).isEqual(DEPARTURE_DATE));
        assertTrue(ZonedDateTime.parse(updatedFlight.getString("arrivalDate")).isEqual(UPDATED_ARRIVAL_TIME));
        assertTrue(updatedFlight.getString("flightStatus").equals(UPDATED_STATUS_DELAYED));

        // find by flight number
        WebTarget path = target
                .path("/flightNumber")
                .path(FLIGHT_NUMBER);

        System.out.println(path);

        JsonObject updatedFlightGetByNumber = path
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertTrue(updatedFlightGetByNumber.getString("flightNumber").equals(FLIGHT_NUMBER));
        assertTrue(updatedFlightGetByNumber.getString("fromAirport").equals(FROM));
        assertTrue(updatedFlightGetByNumber.getString("toAirport").equals(TO));
        assertTrue(ZonedDateTime.parse(updatedFlightGetByNumber.getString("departureDate")).isEqual(DEPARTURE_DATE));
        assertTrue(ZonedDateTime.parse(updatedFlightGetByNumber.getString("arrivalDate")).isEqual(UPDATED_ARRIVAL_TIME));
        assertTrue(updatedFlightGetByNumber.getString("flightStatus").equals(UPDATED_STATUS_DELAYED));


        // update status ("flightStatus" field) with a subresource PUT method
        JsonObjectBuilder statusBuilder = Json.createObjectBuilder();
        JsonObject status = statusBuilder
                .add("flightStatus", "ONTIME")
                .build();
        client.target(location)
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));

        // verify that status is updated
        updatedFlight = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        assertThat(updatedFlight.getString("flightStatus"), is("ONTIME"));

        // update status on not existing object
        JsonObjectBuilder notExistingBuilder = Json.createObjectBuilder();
        status = notExistingBuilder
                .add("flightStatus", "ONTIME")
                .build();
        Response response = target.path("-42")
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));

        response.getHeaders().forEach((k, v) -> System.out.println(k + ": " + v));
        assertThat(response.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        assertFalse(response.getHeaderString("reason").isEmpty());

        // update with malformed status
        JsonObjectBuilder malformedBuilder = Json.createObjectBuilder();
        status = malformedBuilder
                .add("something wrong", true)
                .build();
        response = client.target(location)
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));
        assertThat(response.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        assertFalse(response.getHeaderString("reason").isEmpty());

        // GET all
        response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(),is(Response.Status.OK));
        JsonArray allFlights = response.readEntity(JsonArray.class);
        assertFalse(allFlights.isEmpty());

        // verify that the array contains the Flight with id = OBJECT_ID created in this test case
        JsonObject flight = null;
        for(JsonValue obj: allFlights) {
            if (((JsonObject)obj).getInt("id") == OBJECT_ID) {
                flight = (JsonObject)obj;
                break;
            }
        }
        assertTrue(flight != null);

        System.out.println(flight);

        // DELETE non existing object
        Response deleteResponse = target.
                path("-42").
                request(MediaType.APPLICATION_JSON)
                .delete();
        assertThat(deleteResponse.getStatusInfo(),is(Response.Status.NO_CONTENT));

        // DELETE existing object
        Response deleteResponseFound = client
                .target(location)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        System.out.println(location);

        assertThat(deleteResponseFound.getStatusInfo(),is(Response.Status.NO_CONTENT));

    }

}
