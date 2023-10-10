package softuni.exam.models.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "astronomers")
@XmlAccessorType(XmlAccessType.FIELD)
public class AstronomerWrapperDto {

    @XmlElement(name = "astronomer")
    private List<AstronomerImportDto> astronomers;

    public AstronomerWrapperDto(List<AstronomerImportDto> astronomers) {
        this.astronomers = astronomers;
    }

    public AstronomerWrapperDto() {
    }

    public List<AstronomerImportDto> getAstronomers() {
        return astronomers;
    }

    public void setAstronomers(List<AstronomerImportDto> astronomers) {
        this.astronomers = astronomers;
    }


}
