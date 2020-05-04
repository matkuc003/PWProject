package pl.utp.placefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationType;
import pl.utp.placefinder.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByLocationType(LocationType locationType);

    List<Location> findAllByUser(User user);
}