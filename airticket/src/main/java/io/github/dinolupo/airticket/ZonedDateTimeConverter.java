package io.github.dinolupo.airticket;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Created by dinolupo.github.io on 31/07/16.
 */
//@FacesConverter(forClass=ZonedDateTime.class)
@FacesConverter("zonedDateTimeConverter")
public class ZonedDateTimeConverter implements Converter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof ZonedDateTime) {
            return getFormatter(context, component).format((ZonedDateTime) modelValue);
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid ZonedDateTime"));
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }

        try {
            return ZonedDateTime.parse(submittedValue, getFormatter(context, component));
        } catch (DateTimeParseException e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid zoned date time"), e);
        }
    }

    private DateTimeFormatter getFormatter(FacesContext context, UIComponent component) {
        return DateTimeFormatter.ofPattern(getPattern(component), getLocale(context, component));
    }

    private String getPattern(UIComponent component) {
        String pattern = (String) component.getAttributes().get("pattern");
        if (pattern == null) {
            throw new IllegalArgumentException("pattern attribute is required");
        }

        return pattern;
    }

    private Locale getLocale(FacesContext context, UIComponent component) {
        Object locale = component.getAttributes().get("locale");
        return (locale instanceof Locale) ? (Locale) locale
                : (locale instanceof String) ? new Locale((String) locale)
                : context.getViewRoot().getLocale();
    }

}

