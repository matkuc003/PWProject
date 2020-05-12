package pl.utp.placefinder.mapper;

import org.springframework.stereotype.Component;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LocationMapper {
    public Location convertToEntity(LocationDTO locationDTO) {
        LocalDateTime date = mapStringToDateTime(locationDTO.getDate());
        return new Location(0l, locationDTO.getLat(), locationDTO.getLng(), date, locationDTO.getUser());
    }

    public LocalDateTime mapStringToDateTime(String dateTime) {
        Instant instant = Instant.ofEpochMilli(Long.parseLong(dateTime));
        LocalDateTime date = instant.atZone(ZoneId.of("Poland")).toLocalDateTime();
        return date;
    }
}
