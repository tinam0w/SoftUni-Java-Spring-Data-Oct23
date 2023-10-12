package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "astronomers")
@XmlAccessorType(XmlAccessType.FIELD)
public class AstronomerSeedRootDto {

    @XmlElement(name = "astronomer")
    private List<AstronomerSeedDto> astronomersListDto;

    public List<AstronomerSeedDto> getAstronomersListDto() {
        return astronomersListDto;
    }

    public void setAstronomersListDto(List<AstronomerSeedDto> astronomersListDto) {
        this.astronomersListDto = astronomersListDto;
    }
}
