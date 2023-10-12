package softuni.exam.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Component
public class XmlParser {
    public <T> T fromFile(File file, Class<T> object) throws JAXBException, FileNotFoundException, JAXBException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance(object);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        FileReader fileReader = new FileReader(file);

        return (T) unmarshaller.unmarshal(fileReader);
    }
}
