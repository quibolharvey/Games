package org.openjfx;

import javafx.fxml.FXML;

import java.io.IOException;

public class Controller {

    @FXML
    private void goToEasy() {
        try {
            Main.setRoot("menu_select_controller");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
