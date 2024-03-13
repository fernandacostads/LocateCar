package ada.locate.car.controller.impl.client;

import ada.locate.car.controller.api.Controller;
import ada.locate.car.app.messages.MessagesClient;
import ada.locate.car.swing.api.Input;
import ada.locate.car.swing.api.Output;

import java.util.Arrays;

public class UpdateClientCNPJControllerImpl implements Controller {
    private final Input<String[]> inputMultipleFields;
    private final Output showInformation;

    public UpdateClientCNPJControllerImpl(Input<String[]> inputMultipleFields, Output showInformation) {
        this.inputMultipleFiel = inputMultipleFields;
        this.showInformation = showInformation;
    }

    @Override
    public void execute() {
        String[] data = inputMultipleFields.execute(MessagesClient.MENU_UPDATED_CLIENT.get(), MessagesClient.UPDATED_CLIENT.get());

        showInformation.execute("Client updated!", Arrays.toString(data));

        System.out.println(Arrays.toString(data));
    }
}