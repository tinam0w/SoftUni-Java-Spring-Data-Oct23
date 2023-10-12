package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarImportDto;
import softuni.exam.models.dto.CarsWrapperDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarsRepository;
import softuni.exam.service.CarsService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static softuni.exam.models.Constants.*;

@Service
public class CarsServiceImpl implements CarsService {
    private static String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";

    private final CarsRepository carsRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;
    private final XmlParser xmlParser;

    @Autowired
    public CarsServiceImpl(CarsRepository carsRepository, ModelMapper modelMapper, ValidationUtils validationUtils, XmlParser xmlParser) {
        this.carsRepository = carsRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return carsRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        List<CarImportDto> cars = xmlParser
                .fromFile(Path.of(CARS_FILE_PATH)
                        .toFile(), CarsWrapperDto.class)
                .getCars();

        for (CarImportDto car : cars) {
            sb.append(System.lineSeparator());

            if (carsRepository.findFirstByPlateNumber(car.getPlateNumber()).isPresent() ||
            !validationUtils.isValid(car)){
                sb.append(String.format(INVALID_FORMAT, CAR));
                continue;
            }

            carsRepository.save(modelMapper.map(car, Car.class));
            sb.append(String.format(SUCCESSFULLY_FORMAT,
                    CAR,
                    car.getCarMake() + " -",
                    car.getCarModel()));
        }

        return sb.toString().trim();
    }
}
