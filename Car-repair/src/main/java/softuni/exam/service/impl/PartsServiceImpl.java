package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartImportDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartsRepository;
import softuni.exam.service.PartsService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;

@Service
public class PartsServiceImpl implements PartsService {
    private static String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartsRepository partsRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtils validationUtils;

    @Autowired
    public PartsServiceImpl(PartsRepository partsRepository, ModelMapper modelMapper, Gson gson, ValidationUtils validationUtils) {
        this.partsRepository = partsRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return partsRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder sb = new StringBuilder();

        List<PartImportDto> parts = Arrays.stream(gson.fromJson(readPartsFileContent(), PartImportDto[].class)).collect(Collectors.toList());

        for (PartImportDto part : parts) {
            sb.append(System.lineSeparator());

            if (partsRepository.findFirstByPartName(part.getPartName()).isPresent() ||
                    !validationUtils.isValid(part)) {
                sb.append(String.format(INVALID_FORMAT, PART));
                continue;
            }

            partsRepository.save(modelMapper.map(part, Part.class));

            sb.append(String.format(SUCCESSFULLY_FORMAT,
                    PART,
                    part.getPartName() + " -",
                    part.getPrice()));
        }

        return sb.toString().trim();
    }
}
