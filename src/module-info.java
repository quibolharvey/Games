module ElementaryQs {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires java.xml;
    requires javafx.base;
    requires javafx.media;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}