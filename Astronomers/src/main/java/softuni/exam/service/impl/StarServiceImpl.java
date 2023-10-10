package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.ExportNonObservedStarsDto;
import softuni.exam.models.dtos.StarSeedDto;
import softuni.exam.models.entities.Constellation;
import softuni.exam.models.entities.Star;

import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.StarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StarServiceImpl implements StarService {

    private final static String STARS_FILE_PATH = "src/main/resources/files/json/stars.json";

    private final Gson gson;
    private final ConstellationRepository constellationRepository;
    private final ValidationUtil validation;

    private final StarRepository starRepository;
    private final ModelMapper mapper;


    public StarServiceImpl(Gson gson, ValidationUtil validationUtil, ModelMapper mapper, StarRepository starRepository, ConstellationRepository constellationRepository) {
        this.gson = gson;
        this.validation = validationUtil;
        this.mapper = mapper;
        this.starRepository = starRepository;
        this.constellationRepository = constellationRepository;

    }

    @Override
    public boolean areImported() {
        return starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return Files.readString(Path.of(STARS_FILE_PATH));
    }

    @Override
    public String importStars() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readStarsFileContent(), StarSeedDto[].class))
                .filter(starSeedDto -> {
                    boolean isValidated = validation.isValid(starSeedDto);

                    Optional<Star> starByName = starRepository.findByName(starSeedDto.getName());

                    if (isValidated && starByName.isEmpty()) {
                        sb.append(String.format("Successfully imported star %s - %.2f light years", starSeedDto.getName(),starSeedDto.getLightYears())).append(System.lineSeparator());
                    }else {
                        sb.append("Invalid star").append(System.lineSeparator());
                    }

                    if (starByName.isPresent()){
                       isValidated = false;
                    }

                    return isValidated;
                })
                .map(starSeedDto -> {
                    Star star = mapper.map(starSeedDto, Star.class);

                    Optional<Constellation> consIsPresent = constellationRepository.findById(starSeedDto.getConstellation());

                    star.setConstellation(consIsPresent.get());

                    return star;
                }).forEach(starRepository::save);

        return sb.toString();
    }

    @Override
    public String exportStars() {
        StringBuilder sb = new StringBuilder();

        List<ExportNonObservedStarsDto> exportList = starRepository.getAllNonObservedStars();

        for (ExportNonObservedStarsDto exportNonObservedStarsDto : exportList) {
            sb.append(String.format("Star: %s\n" +
                    "   *Distance: %.2f light years\n" +
                    "   **Description: %s\n" +
                    "   ***Constellation: %s\n",
                    exportNonObservedStarsDto.getStarName(),
                    exportNonObservedStarsDto.getLightYears(),
                    exportNonObservedStarsDto.getDescription(),
                    exportNonObservedStarsDto.getConstellationName()));
        }


        return sb.toString();
    }
}
