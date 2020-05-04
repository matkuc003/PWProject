package pl.utp.placefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationType;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByLocationType(LocationType locationType);

}