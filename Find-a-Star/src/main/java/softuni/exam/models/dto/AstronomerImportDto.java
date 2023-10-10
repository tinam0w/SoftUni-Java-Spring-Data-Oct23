package softuni.exam.models.dto;


import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "astronomer")
@XmlAccessorType(XmlAccessType.FIELD)
public class AstronomerImportDto {

    @NotNull
    @DecimalMin("500.00")
    @XmlElement(name = "average_observation_hours")
    private Double averageObservationHours;

    @XmlElement
    private String birthday;

    @NotNull
    @Size(min = 2, max = 30)
    @XmlElement(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    @XmlElement(name = "last_name")
    private String lastName;

    @NotNull
    @DecimalMin("15000.00")
    @XmlElement
    private Double salary;

    @NotNull
    @XmlElement(name = "observing_star_id")
    private Long observingStar;

    public AstronomerImportDto() {
    }

    public AstronomerImportDto(Double averageObservationHours, String birthday, String firstName, String lastName, Double salary, Long observingStar) {
        this.averageObservationHours = averageObservationHours;
        this.birthday = birthday;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.observingStar = observingStar;
    }

    public Double getAverageObservationHours() {
        return averageObservationHours;
    }

    public void setAverageObservationHours(Double averageObservationHours) {
        this.averageObservationHours = averageObservationHours;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getObservingStar() {
        return observingStar;
    }

    public void setObservingStar(Long observingStar) {
        this.observingStar = observingStar;
    }
}
