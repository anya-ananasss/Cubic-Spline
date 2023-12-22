module com.example.cubicsplinefxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.example.cubicsplinefxapp to javafx.fxml;
    exports com.example.cubicsplinefxapp;
}