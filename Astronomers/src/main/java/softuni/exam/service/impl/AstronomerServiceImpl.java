package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.AstronomerSeedRootDto;
import softuni.exam.models.entities.Astronomer;
import softuni.exam.models.entities.Star;
import softuni.exam.repository.AstronomerRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.AstronomerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class AstronomerServiceImpl implements AstronomerService {

    private final static String ASTRONOMERS_FILE_PATH = "src/main/resources/files/xml/astronomers.xml";

    private final StarRepository starRepository;

    private final AstronomerRepository astronomerRepository;

    private final XmlParser xmlParser;

    private final ModelMapper mapper;

    private final ValidationUtil validation;

    public AstronomerServiceImpl(StarRepository starRepository, AstronomerRepository astronomerRepository, XmlParser xmlParser, ModelMapper mapper, ValidationUtil validationUtil) {
        this.starRepository = starRepository;
        this.astronomerRepository = astronomerRepository;
        this.xmlParser = xmlParser;
        this.mapper = mapper;
        this.validation = validationUtil;
    }

    @Override
    public boolean areImported() {
        return astronomerRepository.count() > 0;
    }

    @Override
    public String readAstronomersFromFile() throws IOException {
        return Files.readString(Path.of(ASTRONOMERS_FILE_PATH));
    }

    @Override
    public String importAstronomers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(ASTRONOMERS_FILE_PATH, AstronomerSeedRootDto.class).getAstronomersListDto()
                .stream()
                .filter(
                        astronomerSeedDto -> {
                            boolean isValidated = validation.isValid(astronomerSeedDto);

                            Optional<Astronomer> astronomerOptional = astronomerRepository.findByFirstNameAndLastName(astronomerSeedDto.getFirstName(), astronomerSeedDto.getLastName());

                            if (astronomerOptional.isPresent()) {
                                isValidated = false;
                            }

                            Optional<Star> starById = starRepository.findById(astronomerSeedDto.getObservingStarId());

                            if (starById.isEmpty()) {
                                isValidated = false;
                            }

                            if (isValidated && astronomerOptional.isEmpty() && starById.isPresent()){
                                sb.append(String.format("Successfully imported astronomer %s %s - %.2f",
                                        astronomerSeedDto.getFirstName(), astronomerSeedDto.getLastName(), astronomerSeedDto.getAverageObservationHours()))
                                        .append(System.lineSeparator());
                            }else {
                                sb.append("Invalid astronomer")
                                        .append(System.lineSeparator());
                            }

                            return isValidated;
                        }
                ).map(
                        astronomerSeedDto -> {
                            Astronomer astronomer = mapper.map(astronomerSeedDto, Astronomer.class);
                            Optional<Star> optionalStar = starRepository.findById(astronomerSeedDto.getObservingStarId());
                            astronomer.setObservingStar(optionalStar.get());
                            return astronomer;
                        }
                ).forEach(astronomerRepository::save);

        return sb.toString();
    }
}
