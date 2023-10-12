package net.devcats.recordtracker;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RecordTrackerController {
    private static final String FILE_PATH = "record_data.json";

    @FXML
    private BorderPane root;
    @FXML
    private ImageView imgClose;
    @FXML
    private ImageView imgColorPicker;
    @FXML
    private Label lblWins;
    @FXML
    private Label lblLosses;
    @FXML
    private Label lblDraws;
    @FXML
    private Label lblRank;

    private int wins;
    private int losses;
    private int draws;
    private String rank;

    private ColorModel colorModel;

    public void initialize() {
        readTextFile();

        setupLabel(lblWins);
        setupLabel(lblLosses);
        setupLabel(lblDraws);

        String[] menuItems = {
                "Grandmaster 1", "Grandmaster 2", "Grandmaster 3", "Grandmaster 4", "Grandmaster 5",
                "Master 1", "Master 2", "Master 3", "Master 4", "Master 5",
                "Diamond 1", "Diamond 2", "Diamond 3", "Diamond 4", "Diamond 5",
                "Platinum 1", "Platinum 2", "Platinum 3", "Platinum 4", "Platinum 5",
                "Gold 1", "Gold 2", "Gold 3", "Gold 4", "Gold 5",
                "Silver 1", "Silver 2", "Silver 3", "Silver 4", "Silver 5",
                "Bronze 1", "Bronze 2", "Bronze 3", "Bronze 4", "Bronze 5",
        };

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle("-fx-background-color: rgb(19,19,19);");
        for (String itemText : menuItems) {
            MenuItem menuItem = new MenuItem(itemText);
            menuItem.setStyle("-fx-text-fill: white;");
            menuItem.setOnAction(e -> handleMenuItemClick(itemText));
            contextMenu.getItems().add(menuItem);
        }

        // Attach the context menu to the label
        lblRank.setContextMenu(contextMenu);
        lblRank.setOnMouseClicked(this::showContextMenu);

        // setup color picker button
        imgColorPicker.setOnMouseClicked(mouseEvent -> showColorPicker());
        imgClose.setOnMouseClicked(mouseEvent -> Platform.exit());
    }

    private void setupLabel(Label label) {
        String styleString = "-fx-font-size: 2em; -fx-text-fill: #" + colorModel.getTextColor().toString().substring(2);
        label.setStyle(styleString);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                switch (label.getId()) {
                    case "lblWins" -> wins++;
                    case "lblLosses" -> losses++;
                    case "lblDraws" -> draws++;
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                switch (label.getId()) {
                    case "lblWins" -> wins = Math.max(0, wins - 1);
                    case "lblLosses" -> losses = Math.max(0, losses - 1);
                    case "lblDraws" -> draws = Math.max(0, draws - 1);
                }
            }
        });
    }

    private void handleMenuItemClick(String itemText) {
        rank = itemText;
        updateAndSave();
    }

    @FXML
    public void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            lblRank.getContextMenu().show(lblRank, event.getScreenX(), event.getScreenY());
        }
    }

    private void updateAndSave() {
        try {
            lblWins.setText("Wins: " + wins);
            lblLosses.setText("Losses: " + losses);
            lblDraws.setText("Draws: " + draws);
            lblRank.setText(rank);

            setRankColor();

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.put("wins", wins);
            jsonObject.put("losses", losses);
            jsonObject.put("draws", draws);
            jsonObject.put("rank", rank);
            jsonObject.put("backgroundColor", colorModel.getBackgroundColor().toString());
            jsonObject.put("textColor", colorModel.getTextColor().toString());

            String jsonString = objectMapper.writeValueAsString(jsonObject);
            saveToFile(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readTextFile() {
        try {
            // Use the ClassLoader to load the resource file
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(FILE_PATH);

            if (inputStream != null) {
                // Read the content from the resource
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder fileContents = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    fileContents.append(line);
                }

                // Parse the JSON content
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(fileContents.toString());
                wins = jsonNode.get("wins").asInt();
                losses = jsonNode.get("losses").asInt();
                draws = jsonNode.get("draws").asInt();
                rank = jsonNode.get("rank").asText();

                if (colorModel == null) colorModel = new ColorModel();
                colorModel.setBackgroundColor(Color.valueOf(jsonNode.get("backgroundColor").asText()));
                colorModel.setTextColor(Color.valueOf(jsonNode.get("textColor").asText()));

                setColorModel(colorModel);

                reader.close();
            } else {
                lblWins.setText("Resource file not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String newData) {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/" + FILE_PATH);
            fileWriter.write(newData);
            fileWriter.close();

            System.out.println("Data saved to " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRankColor() {
        String color;
        if (rank.startsWith("Bronze")) {
            color = "#CD7F32";
        } else if (rank.startsWith("Silver")) {
            color = "#848482";
        } else if (rank.startsWith("Gold")) {
            color = "#FFBF00";
        } else if (rank.startsWith("Platinum")) {
            color = "#72A0C1";
        } else if (rank.startsWith("Diamond")) {
            color = "#C9FFE5";
        } else if (rank.startsWith("Master")) {
            color = "#FF6619";
        } else if (rank.startsWith("Grandmaster")) {
            color = "#FF0000";
        } else {
            color = "#FFFFFF";
        }
        lblRank.setStyle("-fx-font-weight: bold; -fx-font-size: 2em; -fx-text-fill: " + color);
    }

    private void showColorPicker() {
        try {
            // Set the scene for the color picker stage
            FXMLLoader fxmlLoader = new FXMLLoader(RecordTrackerApplication.class.getResource("color-picker-view.fxml"));
            Parent colorPickerRoot = fxmlLoader.load();
            ColorPickerController colorPickerController = fxmlLoader.getController();
            colorPickerController.setColorModel(colorModel);

            Scene colorPickerScene = new Scene(colorPickerRoot, 300, 50);

            // Set the Stage
            Stage colorPickerStage = new Stage();
            colorPickerStage.setScene(colorPickerScene);
            colorPickerStage.initStyle(StageStyle.UTILITY); // Choose the stage style you prefer
            colorPickerStage.setTitle("Color Picker");
            colorPickerStage.setOnCloseRequest(windowEvent -> setColorModel(colorPickerController.getColorModel()));
            // Show the color picker stage
            colorPickerStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;

        if (colorModel.getBackgroundColor().getOpacity() == 0.0) { // leaving opacity at 0.0 causes wierd mouse dragging interaction
            String colorString = colorModel.getBackgroundColor().toString();
            String updatedColorString = colorString.substring(0, colorString.length() - 2) + "11";
            colorModel.setBackgroundColor(Color.valueOf(updatedColorString));
        }
        root.setBackground(new Background(new BackgroundFill(colorModel.getBackgroundColor(), null, null)));

        lblWins.setTextFill(colorModel.getTextColor());
        lblLosses.setTextFill(colorModel.getTextColor());
        lblDraws.setTextFill(colorModel.getTextColor());

        updateAndSave();
    }
}