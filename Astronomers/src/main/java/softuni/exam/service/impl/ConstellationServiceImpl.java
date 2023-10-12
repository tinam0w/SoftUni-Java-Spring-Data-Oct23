package softuni.exam.service.impl;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import softuni.exam.models.dtos.ConstellationSeedDto;
import softuni.exam.models.entities.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.util.ValidationUtil;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private final static String CONSTELLATIONS_FILE_PATH = "src/main/resources/files/json/constellations.json";

    private final ConstellationRepository constellationRepository;
    private final ModelMapper mapper;

    private final ValidationUtil validation;
    private final Gson gson;



    public ConstellationServiceImpl(ConstellationRepository constellationRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.constellationRepository = constellationRepository;
        this.gson = gson;
        this.validation = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return constellationRepository.count() > 0 ;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return Files.readString(Path.of(CONSTELLATIONS_FILE_PATH));
    }

    @Override
    public String importConstellations() throws IOException {
       StringBuilder sb = new StringBuilder();

        Arrays.stream(gson
                .fromJson(readConstellationsFromFile(), ConstellationSeedDto[].class)).filter(constellationSeedDto -> {
            boolean isValidated = validation.isValid(constellationSeedDto);

            Optional<Constellation> constellationByName = constellationRepository.findByName(constellationSeedDto.getName());

          if (isValidated && constellationByName.isEmpty()){
              sb.append(String.format("Successfully imported constellation %s - %s", constellationSeedDto.getName(), constellationSeedDto.getDescription()))
                      .append(System.lineSeparator());
          }else {
              sb.append("Invalid constellation").append(System.lineSeparator());
          }

          if (constellationByName.isPresent()){
              isValidated = false;
          }

          return isValidated;

        }).map(constellationSeedDto -> mapper.map(constellationSeedDto, Constellation.class))
                .forEach(constellationRepository::save);


        return sb.toString();
    }
}
