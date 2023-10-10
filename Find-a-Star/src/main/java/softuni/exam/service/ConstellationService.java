package softuni.exam.service;

import softuni.exam.models.entity.Constellation;

import java.io.IOException;
import java.util.Optional;


public interface ConstellationService {

    boolean areImported();

    String readConstellationsFromFile() throws IOException;

	String importConstellations() throws IOException;

    Optional<Constellation> findById(Long id);
}
