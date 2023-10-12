module net.devcats.recordtracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
            
                            
    opens net.devcats.recordtracker to javafx.fxml;
    exports net.devcats.recordtracker;
}