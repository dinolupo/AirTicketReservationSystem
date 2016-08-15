package io.github.dinolupo.airticket.business.flights;

import io.github.dinolupo.airticket.business.flights.entity.Flight;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

/**
 * Created by dinolupo.github.io on 30/07/16.
 */
//public boolean isValid() {
//        return departureDate != null && arrivalDate != null && departureDate.isBefore(arrivalDate);
//        }

public class FlightManagerTest {

    @Test
    public void valid() {
        ZonedDateTime takeoffTime = ZonedDateTime.now();
        Flight flight = new Flight("blh123","Milan", "Paris", takeoffTime, takeoffTime.plusHours(2));
        assertTrue(flight.isValid());
    }

    @Test
    public void notValid() {
        ZonedDateTime takeoffTime = ZonedDateTime.now();
        Flight flight = new Flight("blh123","Milan", "Paris", takeoffTime, takeoffTime.minusMinutes(1));
        assertFalse(flight.isValid());
    }

}
