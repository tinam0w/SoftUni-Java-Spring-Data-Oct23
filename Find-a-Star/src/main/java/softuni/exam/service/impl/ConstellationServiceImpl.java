package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ConstellationImportDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private static String CONSTELLATIONS_PATH = "src/main/resources/files/json/constellations.json";
    private static String CONSTELLATION_VALID_FORMAT = "Successfully imported constellation %s - %s";
    private static String CONSTELLATION_INVALID_FORMAT = "Invalid constellation";

    private ConstellationRepository constellationRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private Gson gson;

    public ConstellationServiceImpl(ConstellationRepository constellationRepository, ValidationUtils validationUtils, ModelMapper modelMapper, Gson gson) {
        this.constellationRepository = constellationRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return constellationRepository.count() > 0;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return Files.readString(Path.of(CONSTELLATIONS_PATH));
    }

    @Override
    public String importConstellations() throws IOException {
        StringBuilder sb = new StringBuilder();

        ConstellationImportDto[] constellations = gson.fromJson(readConstellationsFromFile(), ConstellationImportDto[].class);

        for (ConstellationImportDto constellation : constellations) {
            sb.append(System.lineSeparator());

            if (!validationUtils.isValid(constellation)
            || constellationRepository.findByName(constellation.getName()).isPresent()) {

                sb.append(CONSTELLATION_INVALID_FORMAT);
                continue;
            }

            Constellation constellationToSave = modelMapper.map(constellation, Constellation.class);
            constellationRepository.save(constellationToSave);

            sb.append(String.format(CONSTELLATION_VALID_FORMAT,
                    constellationToSave.getName(),
                    constellationToSave.getDescription()));
        }

        return sb.toString().trim();
    }

    @Override
    public Optional<Constellation> findById(Long id) {
        return constellationRepository.findById(id);
    }
}
