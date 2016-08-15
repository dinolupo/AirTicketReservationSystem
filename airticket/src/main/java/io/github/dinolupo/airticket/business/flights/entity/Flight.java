package io.github.dinolupo.airticket.business.flights.entity;

import io.github.dinolupo.airticket.business.CrossCheck;
import io.github.dinolupo.airticket.business.ValidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;


/**
 * Created by dinolupo.github.io on 30/07/16.
 */

@Entity
@NamedQueries({
    @NamedQuery(name = Flight.findAll, query = "SELECT f FROM Flight f"),
    @NamedQuery(name = Flight.findByFlightNumber, query = "SELECT f FROM Flight f where f.flightNumber = :flightNumber")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CrossCheck(message = "validation.flight.crosscheckfailed")
public class Flight implements ValidEntity {

    final static String PREFIX = "flights.entity.";
    public final static String findAll = PREFIX + "findAll";
    public final static String findByFlightNumber = PREFIX + "findByFlightNumber";

    // Optimistick Locking
    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Size(min = 5, max = 8)
    String flightNumber;

    @Size(min=3, max=16)
    String fromAirport;

    @Size(min=3, max=16)
    String toAirport;

    @NotNull
    ZonedDateTime departureDate;

    @NotNull
    ZonedDateTime arrivalDate;

    @NotNull
    FlightStatus flightStatus;

    public Flight(String flightNumber, String fromAirport, String toAirport, ZonedDateTime departureDate, ZonedDateTime arrivalDate) {
        this.flightNumber = flightNumber;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.flightStatus = FlightStatus.SCHEDULED;
    }

    public Flight() {
        this.flightStatus = FlightStatus.SCHEDULED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public ZonedDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(ZonedDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    @Override
    public boolean isValid() {
        return departureDate != null && arrivalDate != null && departureDate.isBefore(arrivalDate);
    }
}

