module mazebobot.mazeboot {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens mazebobot.mazeboot to javafx.fxml;
    exports mazebobot.mazeboot;
}