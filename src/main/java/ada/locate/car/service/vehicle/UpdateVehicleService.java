package ada.locate.car.service.vehicle;

import ada.locate.car.core.model.Vehicle;
import ada.locate.car.core.usecase.UpdateVehicle;
import ada.locate.car.infra.dto.VehicleDTO;
import ada.locate.car.infra.api.Repository;

public class UpdateVehicleService implements UpdateVehicle {

    private final Repository<Vehicle> repository;

    public UpdateVehicleService(Repository<Vehicle> vehicleRepository) {
        this.repository = vehicleRepository;
    }


    @Override
    public void execute(VehicleDTO vehicleDTO) {
        Vehicle oldVehicle = repository.read(vehicleDTO.plateNumber());
        Vehicle newVehicle = null;
        switch (vehicleDTO.description().toLowerCase()){
            case "color" -> newVehicle = new Vehicle(
                    oldVehicle.getBrand(),
                    oldVehicle.getYearManufacture(),
                    vehicleDTO.color(),
                    oldVehicle.getPlateNumber(),
                    oldVehicle.getModel());
            case "plate number" -> newVehicle = new Vehicle(
                    oldVehicle.getBrand(),
                    oldVehicle.getYearManufacture(),
                    oldVehicle.getColor(),
                    vehicleDTO.newPlateNumber(),
                    oldVehicle.getModel());
            case "plate color and number" -> newVehicle = new Vehicle(
                    oldVehicle.getBrand(),
                    oldVehicle.getYearManufacture(),
                    vehicleDTO.color(),
                    vehicleDTO.newPlateNumber(),
                    oldVehicle.getModel());
        }
        repository.update(newVehicle, oldVehicle);
    }
}
