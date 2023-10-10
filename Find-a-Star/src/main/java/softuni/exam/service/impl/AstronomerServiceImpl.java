package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AstronomerImportDto;
import softuni.exam.models.dto.AstronomerWrapperDto;
import softuni.exam.models.entity.Astronomer;
import softuni.exam.models.entity.Star;
import softuni.exam.repository.AstronomerRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.AstronomerService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.impl.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AstronomerServiceImpl implements AstronomerService {

    private static String ASTRONOMERS_PATH = "src/main/resources/files/xml/astronomers.xml";
    private static String ASTRONOMER_VALID_FORMAT = "Successfully imported astronomer %s %s - %.2f";
    private static String ASTRONOMER_INVALID_FORMAT = "Invalid astronomer";

    private AstronomerRepository astronomerRepository;
    private ValidationUtils validationUtils;
    private ModelMapper modelMapper;
    private XmlParser xmlParser;
    private StarRepository starRepository;

    public AstronomerServiceImpl(AstronomerRepository astronomerRepository, ValidationUtils validationUtils, ModelMapper modelMapper, XmlParser xmlParser, StarRepository starRepository) {
        this.astronomerRepository = astronomerRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.starRepository = starRepository;
    }

    @Override
    public boolean areImported() {
        return astronomerRepository.count() > 0;
    }

    @Override
    public String readAstronomersFromFile() throws IOException {
        return Files.readString(Path.of(ASTRONOMERS_PATH));
    }

    @Override
    public String importAstronomers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        AstronomerWrapperDto astronomerWrapperDto = xmlParser.fromFile(new File(ASTRONOMERS_PATH), AstronomerWrapperDto.class);

        List<AstronomerImportDto> astronomers = astronomerWrapperDto.getAstronomers();

        for (AstronomerImportDto astronomer : astronomers) {
            sb.append(System.lineSeparator());

            boolean isValid = validationUtils.isValid(astronomer)
                    && astronomerRepository.findFirstByFirstNameAndLastName(astronomer.getFirstName(), astronomer.getLastName()).isEmpty()
                    && starRepository.findById(astronomer.getObservingStar()).isPresent();

            if (isValid) {
                Star starById = starRepository.getById(astronomer.getObservingStar());

                Astronomer astronomerToSave = modelMapper.map(astronomer, Astronomer.class);
                astronomerToSave.setObservingStar(starById);

                astronomerToSave.setBirthday(
                        LocalDate.parse(astronomer.getBirthday(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                //starById.setObserver(astronomerToSave);

                astronomerRepository.saveAndFlush(astronomerToSave);

                sb.append(String.format(ASTRONOMER_VALID_FORMAT,
                        astronomerToSave.getFirstName(),
                        astronomerToSave.getLastName(),
                        astronomerToSave.getAverageObservationHours()));
            } else {
                sb.append(ASTRONOMER_INVALID_FORMAT);
            }
        }

        return sb.toString().trim();
    }
}
