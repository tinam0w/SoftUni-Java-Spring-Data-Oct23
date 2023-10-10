package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.StarType;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {
    Optional<Star> findFirstByName(String name);

    @Override
    Optional<Star> findById(Long aLong);

    //Set<Star> findAllByStarTypeAndObservers_EmptyOrderByLightYearsAsc(StarType starType);

    //@Query("select s from Star s where s.starType = 'RED_GIANT' and size(s.observers) = 0 order by s.lightYears")
    //Set<Star> exportStars();

    @Query("select s from Star s where s.starType = 'RED_GIANT' and s.id not in Astronomer.observingStar.id order by s.lightYears")
    Set<Star> findAllStarsToExport();
}
