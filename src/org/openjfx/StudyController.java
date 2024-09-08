package org.openjfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class StudyController implements Initializable {

    @FXML
    ToggleGroup options;

    @FXML
    AnchorPane easyLayout;

    @FXML
    Label questionLabel, modeLabel;

    @FXML
    RadioButton op1, op2, op3, op4;

    @FXML
    public ImageView imageQuestion;

    @FXML
    public StackPane pane;

    JFXSnackbar snackbar;
    public List<XMLQuestions> xmlQuestionsList;

    public int currentNode = 0;
    public int score = 0;

    public static final String CORRECT_SRC = "res/trks/chk.mp3";
    public static final String CORRECT_BELL_SRC = "res/trks/chk2.mp3";
    public static final String WRONG_SRC = "res/trks/rng1.mp3";

    @FXML
    private void goToHome() {
        try {
            Main.setRoot("main");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CHECK BUTTON
    public void checkQuestion(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) options.getSelectedToggle();

        if (null == selectedRadioButton) {
            showSnackBar("error-toast", "No answer selected!");
            playWrong();
        } else {
            String toogleGroupValue = selectedRadioButton.getText();
            // CHECK IF THE ANSWER IS CORRECT

            if (toogleGroupValue.equals(xmlQuestionsList.get(currentNode).getAnswer())) {
                score += 1;
                explanationCreator("Correct", "Correct", xmlQuestionsList.get(currentNode).getExplanation(), xmlQuestionsList.get(currentNode).getAnswer());
                playCorrectBell();
            } else {
                explanationCreator("Incorrect", "Incorrect", xmlQuestionsList.get(currentNode).getExplanation(), xmlQuestionsList.get(currentNode).getAnswer());
                playWrong();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new JFXSnackbar(easyLayout);

        // FETCH QUESTIONAIRES
        if (Main.getChapterSelectEdit().equals("All Chapters")) {
            xmlQuestionsList = XMLGetHelper.getAllQuestions();
        } else {
            xmlQuestionsList = XMLGetHelper.getAllQuestions(Main.getChapterSelectEdit());
        }

        Collections.shuffle(xmlQuestionsList);

        modeLabel.setText(Main.getModeSelect());

        // PLOT TO FRONTEND
        updateQuestion();
    }

    public void updateQuestion() {
        try {

            options.selectToggle(null);
            XMLQuestions currentQueue = xmlQuestionsList.get(currentNode);
            questionLabel.setText("Q" + (currentNode+1) + ".) " + currentQueue.getQuestion());
            if (!currentQueue.getImg().equals("")) {
                InputStream stream = new FileInputStream(currentQueue.getImg());
                Image image = new Image(stream);
                imageQuestion.setImage(image);
            } else {
                imageQuestion.setImage(null);
            }

            // ADD AND SHUFFLE QUESTIONS
            List<String> ops = new ArrayList<>();
            ops.add(currentQueue.getOption1());
            ops.add(currentQueue.getOption2());
            ops.add(currentQueue.getOption3());
            ops.add(currentQueue.getOption4());

            Collections.shuffle(ops);

            op1.setText(ops.get(0));
            op2.setText(ops.get(1));
            op3.setText(ops.get(2));
            op4.setText(ops.get(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackBar(String type, String message) {
        snackbar.close();
        snackbar.setPrefWidth(500);
        snackbar.setStyle("-fx-font-size: 20;");
        snackbar.toFront();
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new Label(message), PseudoClass.getPseudoClass(type)));
    }

    public void alertCreator(String title, String header) {
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        Label head = new Label(header == null ? title : title);
        head.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
        head.setStyle("-fx-text-fill: white;");
        dialogContent.setHeading(head);

        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setVgap(10);

        Label context = new Label("You finished the quiz with the score of");
        context.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 29));
        context.setWrapText(true);
        context.setStyle("-fx-text-fill: white;");

        Label scoreTotal = new Label(score + " / " + xmlQuestionsList.size());
        scoreTotal.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 39));
        scoreTotal.setWrapText(true);
        scoreTotal.setStyle("-fx-text-fill: white;");

        flowPane.getChildren().add(context);
        flowPane.getChildren().add(scoreTotal);

        dialogContent.setBody(flowPane);
        JFXButton close = new JFXButton("Close");
        close.getStyleClass().add("JFXButton");
        dialogContent.setActions(close);
        JFXDialog dialog = new JFXDialog(pane, dialogContent, JFXDialog.DialogTransition.CENTER);
        close.setTextFill(Paint.valueOf("#ffffff"));
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent __) {
                dialog.close();
                try {
                    Main.setRoot("main");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if (header.equals("Incorrect")) {
            dialogContent.setStyle("-fx-background-color: red;");
        } else {
            dialogContent.setStyle("-fx-background-color: green;");
        }
        dialog.show();

    }

    public void explanationCreator(String title, String header, String explanation, String correctAnswer) {
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        Label head = new Label(header == null ? title : title);
        head.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
        head.setStyle("-fx-text-fill: white;");
        dialogContent.setHeading(head);

        FlowPane flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setVgap(10);

        if (header.equals("Incorrect")) {
            Label correctAns = new Label(correctAnswer + " is the correct answer.");
            correctAns.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 25));
            correctAns.setWrapText(true);
            correctAns.setStyle("-fx-text-fill: white;");

            Label context = new Label(explanation);
            context.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
            context.setWrapText(true);
            context.setStyle("-fx-text-fill: white;");

            flowPane.getChildren().add(correctAns);
            flowPane.getChildren().add(context);
        } else {
            Label context = new Label(explanation);
            context.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
            context.setWrapText(true);
            context.setStyle("-fx-text-fill: white;");

            flowPane.getChildren().add(context);
        }

        dialogContent.setBody(flowPane);
        JFXButton next_question = new JFXButton(currentNode == xmlQuestionsList.size()-1 ? "Finish" : "Next Question");
        next_question.getStyleClass().add("JFXButton");
        dialogContent.setActions(next_question);
        JFXDialog dialog = new JFXDialog(pane, dialogContent, JFXDialog.DialogTransition.CENTER);
        next_question.setTextFill(Paint.valueOf("#ffffff"));
        next_question.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent __) {
                // Toggle to next question
                if (currentNode == xmlQuestionsList.size()-1) {
                    XMLGetHelper.insertScore(score + "/" + xmlQuestionsList.size());
                    playCorrect();

                    try {
                        Main.setRoot("menu_select_controller");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    currentNode += 1;
                }
                updateQuestion();

                dialog.close();
            }
        });
        if (header.equals("Incorrect")) {
            dialogContent.setStyle("-fx-background-color: red;");
        } else {
            dialogContent.setStyle("-fx-background-color: green;");
        }
        dialog.setOverlayClose(false);
        dialog.show();
    }

    public void playCorrect() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(CORRECT_SRC);
                    final Media media = new Media(file.toURI().toString());
                    final MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playCorrectBell() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(CORRECT_BELL_SRC);
                    final Media media = new Media(file.toURI().toString());
                    final MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playWrong() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(WRONG_SRC);
                    final Media media = new Media(file.toURI().toString());
                    final MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

