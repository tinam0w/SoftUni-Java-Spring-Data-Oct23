package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.StarImportDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.StarType;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.service.StarService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class StarServiceImpl implements StarService {

    private static String STARS_PATH = "src/main/resources/files/json/stars.json";
    private static String STAR_VALID_FORMAT = "Successfully imported star %s - %.2f light years";
    private static String STARTS_INVALID_FORMAT = "Invalid star";
    private static String EXPORT_FORMAT = "Star: %s\n" +
            "*Distance: %.2f light years\n" +
            "**Description: %s\n" +
            "***Constellation: %s";

    private StarRepository starRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private Gson gson;
    private ConstellationService constellationService;

    public StarServiceImpl(StarRepository starRepository, ValidationUtils validationUtils, ModelMapper modelMapper, Gson gson, ConstellationService constellationService) {
        this.starRepository = starRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.constellationService = constellationService;
    }

    @Override
    public boolean areImported() {
        return starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return Files.readString(Path.of(STARS_PATH));
    }

    @Override
    public String importStars() throws IOException {
        StringBuilder sb = new StringBuilder();

        StarImportDto[] stars = gson.fromJson(readStarsFileContent(), StarImportDto[].class);

        for (StarImportDto star : stars) {
            sb.append(System.lineSeparator());

            if (!validationUtils.isValid(star)
                    || starRepository.findFirstByName(star.getName()).isPresent()) {

                sb.append(STARTS_INVALID_FORMAT);
                continue;
            }

            Star starToSave = modelMapper.map(star, Star.class);
            Constellation constellationToSave = constellationService.findById(star.getConstellation()).get();
            starToSave.setConstellation(constellationToSave);

            starRepository.save(starToSave);

            sb.append(String.format(STAR_VALID_FORMAT,
                    starToSave.getName(),
                    starToSave.getLightYears()));
        }

        return sb.toString().trim();
    }

    @Override
    public String exportStars() {
        StringBuilder sb = new StringBuilder();

        Set<Star> stars = starRepository.findAllStarsToExport();

        stars.forEach(star -> sb.append(String.format(EXPORT_FORMAT,
                        star.getName(),
                        star.getLightYears(),
                        star.getDescription(),
                        star.getConstellation().getName()))
                .append(System.lineSeparator()));

        return sb.toString().trim();
    }
}
