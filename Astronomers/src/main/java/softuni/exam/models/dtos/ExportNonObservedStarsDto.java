package softuni.exam.models.dtos;

public class ExportNonObservedStarsDto {

    private String starName;

    private Double lightYears;

    private String description;

    private String constellationName;

    public ExportNonObservedStarsDto(String starName, Double lightYears, String description, String constellationName) {
        this.starName = starName;
        this.lightYears = lightYears;
        this.description = description;
        this.constellationName = constellationName;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public Double getLightYears() {
        return lightYears;
    }

    public void setLightYears(Double lightYears) {
        this.lightYears = lightYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public void setConstellationName(String constellationName) {
        this.constellationName = constellationName;
    }
}
