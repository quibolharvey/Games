package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuSelectController implements Initializable  {

    public List<Chapters> chaptersListAll;

    @FXML
    public ListView chaptersList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateChapters();
    }
    
    @FXML
    private void chaptersClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            try {
                if (chaptersList.getSelectionModel().getSelectedItem()!=null) {
                    if (chaptersList.getSelectionModel().getSelectedItem().toString().equals("All Chapters")) {
                        Main.setChapterSelectEdit("All Chapters");
                        Main.setRoot("quiz_mode_select_controller");
                    } else {
                        Main.setChapterSelectEdit(chaptersListAll.get(chaptersList.getSelectionModel().getSelectedIndex()).getChapter());
                        Main.setRoot("quiz_mode_select_controller");
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } 
    }

    @FXML
    private void goToHome() {
        try {
            Main.setRoot("main");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateChapters() {
        try {
            chaptersList.getItems().clear();
            chaptersListAll = XMLGetHelper.getAllChapters();
            int size = chaptersListAll.size();
            for (int i=0; i<size; i++) {
                Label label = new Label(chaptersListAll.get(i).getChapter());
                label.setWrapText(true);
                label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                chaptersList.getItems().add(label);
            }

            // add all
            chaptersList.getItems().add("All Chapters");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
