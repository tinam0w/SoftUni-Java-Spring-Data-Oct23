package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicImportDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.service.MechanicsService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;

@Service
public class MechanicsServiceImpl implements MechanicsService {
    private static final String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";

    private final MechanicsRepository mechanicsRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtils validationUtils;

    @Autowired
    public MechanicsServiceImpl(MechanicsRepository mechanicsRepository, ModelMapper modelMapper, Gson gson, ValidationUtils validationUtils) {
        this.mechanicsRepository = mechanicsRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return mechanicsRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        StringBuilder sb = new StringBuilder();

        List<MechanicImportDto> mechanics = Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicImportDto[].class)).collect(Collectors.toList());

        for (MechanicImportDto mechanic : mechanics) {
            sb.append(System.lineSeparator());

            if(mechanicsRepository.findFirstByEmail(mechanic.getEmail()).isPresent() ||
            mechanicsRepository.findFirstByFirstname(mechanic.getFirstName()).isPresent() ||
            !validationUtils.isValid(mechanic)) {
                sb.append(String.format(INVALID_FORMAT, MECHANIC));
                continue;
            }

            mechanicsRepository.save(modelMapper.map(mechanic, Mechanic.class));

            sb.append(String.format(SUCCESSFULLY_FORMAT,
                    MECHANIC,
                    mechanic.getFirstName(),
                    mechanic.getLastName()));
        }

        return sb.toString().trim();
    }
}
