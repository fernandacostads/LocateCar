package ada.locate.car.app;

import ada.locate.car.app.config.front.FrontConfig;
import ada.locate.car.app.menu.ClientMenu;
import ada.locate.car.app.menu.Menu;
import ada.locate.car.app.messages.MessagesApp;
import ada.locate.car.app.menu.VehicleMenu;
import ada.locate.car.app.config.vehicle.VehicleControllerConfig;
import ada.locate.car.app.config.vehicle.VehicleControllerImplConfig;
import ada.locate.car.app.config.vehicle.VehicleMenuConfig;
import ada.locate.car.app.config.vehicle.VehicleServiceConfig;
import ada.locate.car.controller.api.Controller;
import ada.locate.car.controller.impl.client.*;
import ada.locate.car.controller.impl.vehicle.CreateVehicleControllerImpl;
import ada.locate.car.controller.impl.vehicle.DeleteVehicleControllerImpl;
import ada.locate.car.controller.impl.vehicle.ReadVehicleControllerImpl;
import ada.locate.car.controller.impl.vehicle.UpdateVehicleControllerImpl;
import ada.locate.car.core.model.Client;
import ada.locate.car.core.model.Vehicle;
import ada.locate.car.core.usecase.*;
import ada.locate.car.frontend.impl.*;
import ada.locate.car.infra.api.Repository;
import ada.locate.car.infra.repository.ClientRepository;
import ada.locate.car.infra.repository.VehicleRepository;
import ada.locate.car.service.client.CreateClientService;
import ada.locate.car.service.client.DeleteClientService;
import ada.locate.car.service.client.UpdateClientService;
import ada.locate.car.service.vehicle.CreateVehicleService;
import ada.locate.car.service.vehicle.DeleteVehicleService;
import ada.locate.car.service.vehicle.ReadVehicleService;
import ada.locate.car.service.vehicle.UpdateVehicleService;
import ada.locate.car.backup.frontend.api.Input;
import ada.locate.car.backup.frontend.api.Output;
import ada.locate.car.backup.frontend.impl.*;

import javax.swing.*;

public class LocateCar {
    public static void run() {
        Repository<Client> clientRepository = ClientRepository.getInstance();

        FrontConfig frontConfig = createFrontConfig2();
        VehicleServiceConfig vehicleServiceConfig = createVehicleServiceConfig();
        VehicleControllerImplConfig vehicleControllerImplConfig = vehicleControllerImplConfig(vehicleServiceConfig, frontConfig);
        VehicleControllerConfig vehicleControllerConfig = createVehicleControllerConfig(vehicleControllerImplConfig);
        VehicleMenuConfig vehicleMenuConfig = createVehicleMenuConfig(vehicleControllerConfig, frontConfig);
        Menu vehicleMenu = new VehicleMenu(vehicleMenuConfig);

        Input<String[]> inputMultipleFields = new ShowInputMultipleFieldsImpl();
        Input<String> inputOnlyField = new ShowInputOnlyFieldImpl();
        Input<String> inputOptionString = new ShowInputOptionsStringImpl();
        Input<String> inputCPF = new CPFInput();
        Input<String> inputCNPJ = new CNPJInput();
        Output showInformation = new ShowInformationOutputImpl();


        CreateClient createClientService = new CreateClientService(clientRepository);
        UpdateClient updateClientService = new UpdateClientService(clientRepository);
        DeleteClient deleteClientService = new DeleteClientService(clientRepository);
//        UpdateClient updateClientService = new UpdateClientService();
        Controller createClientCPF = new CreateClientCPFControllerImpl(inputMultipleFields, showInformation, inputCPF, createClientService);
        Controller createClientCNPJ = new CreateClientCNPJControllerImpl(inputMultipleFields, showInformation, inputCNPJ, createClientService);
        Controller deleteClientCPF = new DeleteClientCPFControllerImpl(inputOnlyField, showInformation, deleteClientService);

        Controller updatedClientCPF = new UpdateClientCPFControllerImpl(inputMultipleFields, showInformation, updateClientService);
        Controller updatedClientCNPJ = new UpdateClientCNPJControllerImpl(inputMultipleFields, showInformation, updateClientService);

        Menu clientMenu = new ClientMenu(inputOptionString, createClientCPF, updatedClientCPF, createClientCNPJ, updatedClientCNPJ);

        JFrame frame = CreateFrame.execute();
        frame.setVisible(true);

        String option;
        do {
            //main menu
            //recebe de quem se trata a edição
            option = inputOptionString.execute(MessagesApp.MAIN_MENU.get(), MessagesApp.MAIN_OPTIONS_MENU.get());

            if (!option.isEmpty()) {
                //direciona para o menu com as opções específicas de Client, Vehicle ou Alocation
                switch (option) {
                    case "Client" -> clientMenu.run();
                    case "Vehicle" -> vehicleMenu.run();
                    //Alocation
                    // case 3 -> AlocationMenu.run();
                }
            }
        } while (!option.isEmpty());

        frame.dispose();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //injeta todas as dependencias para uma chamada da camada service
    private static VehicleServiceConfig createVehicleServiceConfig() {
        Repository<Vehicle> vehicleRepository = VehicleRepository.getInstance();
        return new VehicleServiceConfig(
                new CreateVehicleService(vehicleRepository),
                new ReadVehicleService(vehicleRepository),
                new UpdateVehicleService(vehicleRepository),
                new DeleteVehicleService(vehicleRepository)
        );
    }

    //injeta todas as dependencias para uma chamada do controller
    private static VehicleControllerConfig createVehicleControllerConfig(VehicleControllerImplConfig vehicleControllerImpl) {
        return new VehicleControllerConfig(
                new CreateVehicleControllerImpl(vehicleControllerImpl),
                new ReadVehicleControllerImpl(vehicleControllerImpl),
                new UpdateVehicleControllerImpl(vehicleControllerImpl),
                new DeleteVehicleControllerImpl(vehicleControllerImpl)
        );
    }

    //injeta para que eu consiga receber os inputs pelo front e consiga passar a requisição pro service
    private static VehicleControllerImplConfig vehicleControllerImplConfig(VehicleServiceConfig vehicleServiceConfig, FrontConfig front2) {
        return new VehicleControllerImplConfig(vehicleServiceConfig, front2);
    }


    //injeta para que eu consiga passar a requisição pro controller
    private static VehicleMenuConfig createVehicleMenuConfig(VehicleControllerConfig vehicleControllerConfig, FrontConfig front2) {
        return new VehicleMenuConfig(vehicleControllerConfig, front2);
    }

    //injeta todas as interfaces que eu posso ter no meu front
    private static FrontConfig createFrontConfig2() {
        return new FrontConfig(
                new ShowInputOptionsInsertModelFilter(),
                new ShowInputOptionsModel(),
                new ShowInputOptionsReadVehicle(),
                new ShowInputOptionsUpdateVehicle(),
                new ShowInputOptionsVehicle(),
                new ShowInputDataVehicle(),
                new ShowInputUpdateVehicleColorAndNumberPlate(),
                new ShowInputExclusionField(),
                new ShowInputUpdatePlate(),
                new ShowInputUpdateColor(),
                new ShowInputNewPlateNumber(),
                new ShowInputInsertFilterPlate(),
                new ShowInputInsertFilterColor());
    }
}
