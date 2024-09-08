package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizModeSelectController  implements Initializable {

    @FXML
    public Label examModeLabel, studyModeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void goToHome() {
        try {
            Main.setRoot("menu_select_controller");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void studyModeClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            try {
                Main.setModeSelect("Study Mode");
                Main.setRoot("study_controller");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @FXML
    private void changeStudyModeColorEnter(MouseEvent e) {
        studyModeLabel.setStyle("-fx-background-color: #ff9800");
        studyModeLabel.setTextFill(Paint.valueOf("#ffffff"));
    }

    @FXML
    private void changeStudyModeColorExit(MouseEvent e) {
        studyModeLabel.setStyle("-fx-background-color: #ffcc80");
        studyModeLabel.setTextFill(Paint.valueOf("#000000"));
    }

    @FXML
    private void examModeClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            try {
                Main.setModeSelect("Exam Mode");
                Main.setRoot("timed_selection_controller");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @FXML
    private void changeExamModeColorEnter(MouseEvent e) {
        examModeLabel.setStyle("-fx-background-color: #009688");
        examModeLabel.setTextFill(Paint.valueOf("#ffffff"));
    }

    @FXML
    private void changeExamModeColorExit(MouseEvent e) {
        examModeLabel.setStyle("-fx-background-color:  #80cbc4");
        examModeLabel.setTextFill(Paint.valueOf("#000000"));
    }
}
