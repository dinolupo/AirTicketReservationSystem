package io.github.dinolupo.airticket.business.flights.boundary;

import io.github.dinolupo.airticket.business.flights.entity.Flight;
import io.github.dinolupo.airticket.business.flights.entity.FlightStatus;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by dinolupo.github.io on 30/07/16.
 */
@Stateless
public class FlightsManager {

    @PersistenceContext
    EntityManager entityManager;

    public Flight findById(long id) {
        return entityManager.find(Flight.class, id);
    }

    public Flight findByFlightNumber(String flightNumber) {
        Query namedQuery = entityManager.createNamedQuery(Flight.findByFlightNumber);
        namedQuery.setParameter("flightNumber", flightNumber);
        List resultList = namedQuery.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return (Flight) resultList.get(0);
        }
    }

    public Flight updateStatus(long id, FlightStatus status) {
        Flight flight = findById(id);
        if (flight == null) {
            return null;
        }
        flight.setFlightStatus(status);
        return flight;
    }

    public void delete(long id) {
        try {
            Flight reference = entityManager.getReference(Flight.class, id);
            entityManager.remove(reference);
        } catch (EntityNotFoundException ex) {
            // we want to remove it, so do not care of the exception
        }
    }


    public List<Flight> findAll() {
        return entityManager.createNamedQuery(Flight.findAll, Flight.class).getResultList();
    }

    public Flight save(Flight flight) {
        Flight merge = entityManager.merge(flight);
        return merge;
    }


}
