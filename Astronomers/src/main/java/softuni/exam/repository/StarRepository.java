package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dtos.ExportNonObservedStarsDto;
import softuni.exam.models.entities.Star;

import java.util.List;
import java.util.Optional;

@Repository

public interface StarRepository extends JpaRepository<Star, Long> {

    Optional<Star> findByName(String name);

    @Query("SELECT new softuni.exam.models.dtos.ExportNonObservedStarsDto(s.name,s.lightYears,s.description,s.constellation.name) FROM Star s  where s.id not in (select a.observingStar.id from Astronomer a) and s.starType = 'RED_GIANT' ORDER BY s.lightYears")
    List<ExportNonObservedStarsDto> getAllNonObservedStars();




}
