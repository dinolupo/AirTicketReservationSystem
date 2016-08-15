package io.github.dinolupo.airticket.presentation.flights;

import io.github.dinolupo.airticket.business.flights.boundary.FlightsManager;
import io.github.dinolupo.airticket.business.flights.entity.Flight;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by dinolupo.github.io on 31/07/16.
 */
@Model
public class Flights {

    @Inject
    FlightsManager boundary;

    Flight flight;

    @Inject
    Validator validator;

    @PostConstruct
    public void init() {
        flight = new Flight();
    }

    public Flight getFlight() {
        return flight;
    }

    // show validation errors
    public void showValidationError(String content) {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgBundle = context.getApplication().getMessageBundle();
        Locale locale = context.getViewRoot().getLocale();
        ResourceBundle messageBundle = ResourceBundle.getBundle(msgBundle,locale);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                messageBundle.getString(content), content);
        context.addMessage(null, message);
    }

    public List<Flight> getFlights(){
        return boundary.findAll();
    }

    // JSF action
    public Object save() {
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);
        for (ConstraintViolation<Flight> violation : violations) {
            showValidationError(violation.getMessage());
        }
        if (violations.isEmpty()) {
            this.boundary.save(flight);
        }
        // stay on the same page
        return null;
    }

}
