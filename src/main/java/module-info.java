module mazebobot.mazeboot {
    requires javafx.controls;
    requires javafx.fxml;


    opens mazebobot.mazeboot to javafx.fxml;
    exports mazebobot.mazeboot;
}