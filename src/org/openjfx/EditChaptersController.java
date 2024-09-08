package org.openjfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditChaptersController implements Initializable {

    public List<Chapters> chaptersList;

    @FXML
    public JFXListView chaptersEditList;

    @FXML
    public StackPane pane;

    @FXML
    public JFXButton addChapter, deleteChapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateChapters();
    }

    @FXML
    private void chapterEditClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            try {
                Main.setChapterSelectEdit(chaptersList.get(chaptersEditList.getSelectionModel().getSelectedIndex()).getChapter());
                Main.setRoot("edit_mode_controller");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            if (chaptersEditList.getSelectionModel().getSelectedItem()!=null) {
                deleteChapter.setDisable(false);
            } else {
                deleteChapter.setDisable(true);
            }
        }
    }

    @FXML
    private void homeBtn() throws IOException {
        Main.setRoot("main");
    }

    @FXML
    private void addNewChapter() {
        newChapter("Add New Chapter", "Add New Chapter");
    }

    @FXML
    private void deleteChapter() {
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        Label head = new Label("Confirmation");
        head.setStyle("-fx-text-fill: white;");
        dialogContent.setHeading(head);

        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setVgap(10);

        Label context = new Label("Are you sure you want to delete " + chaptersList.get(chaptersEditList.getSelectionModel().getSelectedIndex()).getChapter() + "?");
        context.setStyle("-fx-text-fill: white;");

        dialogContent.setBody(context);
        JFXButton yes = new JFXButton("YES");
        yes.getStyleClass().add("JFXButton");
        dialogContent.setActions(yes);
        JFXDialog dialog = new JFXDialog(pane, dialogContent, JFXDialog.DialogTransition.CENTER);
        yes.setTextFill(Paint.valueOf("#ffffff"));
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent __) {
                XMLGetHelper.deleteChapter(chaptersList.get(chaptersEditList.getSelectionModel().getSelectedIndex()).getChapter());
                populateChapters();
                dialog.close();
            }
        });
        dialogContent.setStyle("-fx-background-color: red;");
        dialog.show();

    }

    public void populateChapters() {
        try {
            chaptersEditList.getItems().clear();
            chaptersList = XMLGetHelper.getAllChapters();
            int size = chaptersList.size();
            for (int i=0; i<size; i++) {
                Label label = new Label(chaptersList.get(i).getChapter());
                label.setWrapText(true);
                label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                chaptersEditList.getItems().add(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newChapter(String title, String header) { // entryType -> main, adjustments
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        Label head = new Label(header == null ? title : title);
        dialogContent.setHeading(head);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(6);

        TextField elementField = new TextField();
        elementField.setPromptText("Chapter Title");

        vbox.getChildren().add(elementField);

        dialogContent.setBody(vbox);
        JFXButton close = new JFXButton("Save");
        close.getStyleClass().add("JFXButton");
        dialogContent.setActions(close);
        JFXDialog dialog = new JFXDialog(pane, dialogContent, JFXDialog.DialogTransition.CENTER);
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent __) {
                XMLGetHelper.addChapter(new Chapters(elementField.getText()));
                populateChapters();
                dialog.close();
            }
        });
        dialog.show();
    }
}
