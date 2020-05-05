package pl.utp.placefinder.mapper;

import org.springframework.stereotype.Component;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.model.LocationType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LocationMapper {
    public Location convertToEntity(LocationDTO locationDTO) {
        Instant instant = Instant.ofEpochMilli(Long.parseLong(locationDTO.getDate()));
        LocalDateTime date = instant.atZone(ZoneId.of("Poland")).toLocalDateTime();

        return new Location(0l, locationDTO.getLat(), locationDTO.getLng(), LocationType.valueOf(locationDTO.getLocationType()),
                date, locationDTO.getUser());
    }
}
