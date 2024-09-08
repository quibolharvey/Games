package org.openjfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class EditModeController implements Initializable {

    @FXML
    public TableView questionsTable, scoresTable;
    @FXML
    public TableColumn questionsCol, actionsCol, scoreDateCol, scoreCol;
    @FXML
    public TextArea questionField, explanationField;
    @FXML
    public JFXButton addImageBtn, saveBtn;
    @FXML
    public TextField correctAnswerField, option1Field, option2Field, option3Field;
    @FXML
    public Label imgLabel, chapterLabel;
    @FXML
    public AnchorPane easyLayout;
    @FXML
    public StackPane pane;

    public List<XMLQuestions> questionsList;
    public File imgFile;
    JFXSnackbar snackbar;

    public boolean isNewQuestion = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new JFXSnackbar(easyLayout);
        chapterLabel.setText(Main.getChapterSelectEdit());
        populateTable();
        populateScores();
    }

    public void populateTable() {
        questionsCol.setCellValueFactory(new PropertyValueFactory<>("question"));

        // populate questions table
        questionsList = XMLGetHelper.getAllQuestions(Main.getChapterSelectEdit());
        for (int i=0; i<questionsList.size(); i++) {
            questionsTable.getItems().add(questionsList.get(i));
        }

        // DELETE
        TableColumn<XMLQuestions, XMLQuestions> deleteButtonCol = new TableColumn<>("Actions");
        deleteButtonCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteButtonCol.setCellFactory(param -> new TableCell<XMLQuestions, XMLQuestions>() {
            private final JFXButton deleteButton = new JFXButton("Delete");
            private final JFXButton updateButton = new JFXButton("Update");


            @Override
            protected void updateItem(XMLQuestions questions, boolean empty) {
                super.updateItem(questions, empty);

                if (questions == null) {
                    setGraphic(null);
                    return;
                }
                HBox hBox = new HBox();

                deleteButton.setButtonType(JFXButton.ButtonType.RAISED);
                deleteButton.setTextFill(Paint.valueOf("#FFFFFF"));
                deleteButton.setStyle("-fx-background-color: #f44336");
                deleteButton.setOnAction(
                        event -> {

                            JFXDialogLayout dialogContent = new JFXDialogLayout();
                            Label head = new Label("Confirmation");
                            head.setStyle("-fx-text-fill: white;");
                            dialogContent.setHeading(head);

                            FlowPane flowPane = new FlowPane();
                            flowPane.setOrientation(Orientation.VERTICAL);
                            flowPane.setVgap(10);

                            Label context = new Label("Are you sure you want to delete this question?");
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
                                    getTableView().getItems().remove(questions);
                                    XMLGetHelper.deleteQuestion(questions);
                                    questionsList.remove(questions);
                                    dialog.close();
                                }
                            });
                            dialogContent.setStyle("-fx-background-color: red;");
                            dialog.show();
                        }
                );

                updateButton.setButtonType(JFXButton.ButtonType.RAISED);
                updateButton.setTextFill(Paint.valueOf("#FFFFFF"));
                updateButton.setStyle("-fx-background-color: #03a9f4");
                updateButton.setOnAction(
                        event -> {
                            isNewQuestion = false;

                            questionField.setText(questions.getQuestion());
                            correctAnswerField.setText(questions.getAnswer());
                            option1Field.setText(questions.getOption2());
                            option2Field.setText(questions.getOption3());
                            option3Field.setText(questions.getOption4());

                            if (imgFile != null) {
                                imgFile = null;
                                imgLabel.setText(null);
                            }
                        }
                );

                hBox.setSpacing(5);
                hBox.getChildren().add(updateButton);
                hBox.getChildren().add(deleteButton);

                setGraphic(hBox);
            }
        });
        deleteButtonCol.setPrefWidth(160);
        deleteButtonCol.setMinWidth(160);
        deleteButtonCol.setMaxWidth(160);
        deleteButtonCol.setSortable(false);

        questionsTable.getColumns().add(deleteButtonCol);
    }

    @FXML
    private void editQuestion(MouseEvent e) {
        if (e.getClickCount() == 2) {

        }
    }

    @FXML
    private void newQuestion() {
        clearEntries();
    }

    private void clearEntries() {
        questionField.clear();
        correctAnswerField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        explanationField.clear();
        imgLabel.setText(null);

        isNewQuestion = true;
        imgFile = null;
    }

    @FXML
    private void addImage() {
        try {
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.jpg)", "*.jpg", "*.jpeg", "*.png");
            chooser.getExtensionFilters().add(extFilter);

            imgFile = chooser.showOpenDialog(questionField.getScene().getWindow());
            imgLabel.setText(imgFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveQuestion() {
        if (isNewQuestion) { // create new
            XMLQuestions question = new XMLQuestions();
            question.setQuestion(questionField.getText());
            question.setAnswer(correctAnswerField.getText());
            question.setOption1(correctAnswerField.getText());
            question.setOption2(option1Field.getText());
            question.setOption3(option2Field.getText());
            question.setOption4(option3Field.getText());
            question.setChapter(Main.getChapterSelectEdit());
            question.setExplanation(explanationField.getText());

            // copy selected file
            if (imgFile != null) {
                question.setImg("res/qs/" + imgFile.getName());
                Path to = Paths.get( "res/qs/" + imgFile.getName());
                try {
                    Files.copy(imgFile.toPath(), to);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            XMLGetHelper.addQuestion(question);
            questionsTable.getItems().add(question);
            questionsList.add(question);
            clearEntries();
            showSnackBar("success-toast", "Question added!");
        } else { // update

        }
    }

    public void showSnackBar(String type, String message) {
        snackbar.close();
        snackbar.setPrefWidth(500);
        snackbar.setStyle("-fx-font-size: 20;");
        snackbar.toFront();
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new Label(message), PseudoClass.getPseudoClass(type)));
    }

    @FXML
    private void homeBtn() throws IOException {
        Main.setRoot("edit_chapters_controller");
    }

    public void populateScores() {
        scoreDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        List<Scores> scoresList = XMLGetHelper.getScores();
        Collections.reverse(scoresList);
        for (int i=0; i<scoresList.size(); i++) {
            scoresTable.getItems().add(scoresList.get(i));
        }
    }
}
