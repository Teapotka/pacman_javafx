module com.example.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.pacman to javafx.fxml;
    exports com.example.pacman;
}