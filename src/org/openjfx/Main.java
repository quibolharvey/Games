package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    public static final String BACKGROUND_MUSIC = "res/trks/background.mp3";
    MediaPlayer mediaPlayer;

    private static Scene scene;
    public static String chapterSelectEdit, modeSelect;

    public Main() {}

    public static void setChapterSelectEdit(String chapterSelectEditX) {
        chapterSelectEdit = chapterSelectEditX;
    }

    public static String getChapterSelectEdit() {
        return chapterSelectEdit;
    }

    public static String getModeSelect() {
        return modeSelect;
    }

    public static void setModeSelect(String modeSelect) {
        Main.modeSelect = modeSelect;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Questions");

        playBGMusic();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-50);

//        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Press ESC to exit fullscreen, Press F11 to enter full screen");
        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F11) {
                if (!primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(true);
                } else {
                    primaryStage.setFullScreen(false);
                }
            }

            // Go to edit mode
            if (keyEvent.isControlDown() && keyEvent.getCode()==KeyCode.E) {
                try {
                    Main.setRoot("edit_chapters_controller");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public void setRoot(String fxml, String chapter) throws IOException {
        scene.setRoot(loadFXML(fxml));
        setChapterSelectEdit(chapter);
    }

    static void setRoot(String fxml, String chapter, String mode) throws IOException {
        scene.setRoot(loadFXML(fxml));
        setChapterSelectEdit(chapter);
        setModeSelect(mode);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource( fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void playBGMusic() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(BACKGROUND_MUSIC);
                    final Media media = new Media(file.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    mediaPlayer.setVolume(.5);
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.seek(Duration.ZERO);
                            mediaPlayer.play();
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
