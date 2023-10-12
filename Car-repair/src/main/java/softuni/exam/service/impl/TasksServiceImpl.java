package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskDto;
import softuni.exam.models.dto.TaskImportDto;
import softuni.exam.models.dto.TasksWrapperDto;
import softuni.exam.models.entity.*;
import softuni.exam.repository.CarsRepository;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.repository.PartsRepository;
import softuni.exam.repository.TasksRepository;
import softuni.exam.service.TasksService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.Constants.*;

@Service
public class TasksServiceImpl implements TasksService {
    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";

    private final TasksRepository tasksRepository;
    private final PartsRepository partsRepository;
    private final MechanicsRepository mechanicsRepository;
    private final CarsRepository carsRepository;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public TasksServiceImpl(TasksRepository tasksRepository, PartsRepository partsRepository, MechanicsRepository mechanicsRepository, CarsRepository carsRepository, ValidationUtils validationUtils, ModelMapper modelMapper, XmlParser xmlParser) {
        this.tasksRepository = tasksRepository;
        this.partsRepository = partsRepository;
        this.mechanicsRepository = mechanicsRepository;
        this.carsRepository = carsRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return tasksRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        List<TaskImportDto> tasks = xmlParser.fromFile(Path.of(TASKS_FILE_PATH).toFile(), TasksWrapperDto.class).getTasks();

        for (TaskImportDto task : tasks) {
            sb.append(System.lineSeparator());

            if (validationUtils.isValid(task)) {

                Optional<Mechanic> mechanic = mechanicsRepository.findFirstByFirstname(task.getMechanic().getFirstName());
                Optional<Car> car = carsRepository.findById(task.getCar().getId());
                Optional<Part> part = partsRepository.findById(task.getPart().getId());

                if (mechanic.isEmpty() || car.isEmpty() || part.isEmpty()) {
                    sb.append(String.format(INVALID_FORMAT, TASK));
                    continue;
                }

                Task taskToSave = modelMapper.map(task, Task.class);
                taskToSave.setMechanic(mechanic.get());
                taskToSave.setCar(car.get());
                taskToSave.setPart(part.get());

                tasksRepository.save(taskToSave);
                sb.append(String.format(SUCCESSFULLY_FORMAT,
                        TASK,
                        task.getPrice().setScale(2),
                        "").trim());
            } else {
                sb.append(String.format(INVALID_FORMAT, TASK));
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        return tasksRepository.findAllByCarCarTypeOrderByPriceDesc(CarType.coupe)
                .stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .map(TaskDto::toString)
                .collect(Collectors.joining())
                .trim();
    }
}
