package io.github.dinolupo.airticket;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by dinolupo.github.io on 31/07/16.
 */
@Converter(autoApply=true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Calendar> {

    @Override
    public Calendar convertToDatabaseColumn(ZonedDateTime entityAttribute) {
        if (entityAttribute == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(entityAttribute.toInstant().toEpochMilli());
        calendar.setTimeZone(TimeZone.getTimeZone(entityAttribute.getZone()));
        return calendar;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Calendar databaseColumn) {
        if (databaseColumn == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(databaseColumn.toInstant(), databaseColumn.getTimeZone().toZoneId());
    }

}