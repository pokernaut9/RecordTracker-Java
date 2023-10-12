package net.devcats.recordtracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class RecordTrackerApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RecordTrackerApplication.class.getResource("record-tracker-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 525, 65);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = stage.getX() - mouseEvent.getScreenX();
            yOffset = stage.getY() - mouseEvent.getScreenY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + xOffset);
            stage.setY(mouseEvent.getScreenY() + yOffset);
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Record Tracker");
        stage.setScene(scene);

        Rectangle rect = new Rectangle(525,65);
        rect.setArcHeight(10.0);
        rect.setArcWidth(10.0);
        scene.getRoot().setClip(rect);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}