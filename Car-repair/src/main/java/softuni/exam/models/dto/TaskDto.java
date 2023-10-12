package softuni.exam.models.dto;

import java.math.BigDecimal;

public class TaskDto {
    private Long id;
    private BigDecimal price;
    private MechanicBasicInfo mechanic;
    private CarBasicInfo car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MechanicBasicInfo getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicBasicInfo mechanic) {
        this.mechanic = mechanic;
    }

    public CarBasicInfo getCar() {
        return car;
    }

    public void setCar(CarBasicInfo car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return String.format("Car %s %s with %dkm\n-Mechanic: %s %s - task №%d:\n --Engine: %.1f\n---Price: %s$\n",
                car.getCarMake(), car.getCarModel(), car.getKilometers(),
                mechanic.getFirstName(), mechanic.getLastName(),
                id, car.getEngine(), price.setScale(2));
    }
}
