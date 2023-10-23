package net.devcats.recordtracker;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;

public class ColorPickerController {

    @FXML
    private VBox backgroundColorPickerContainer;
    @FXML
    private VBox textColorPickerContainer;
    @FXML
    private CheckBox chkShowRank;

    private ColorModel colorModel;
    private ColorPicker backgroundColorPicker;
    private ColorPicker textColorPicker;

    public void initialize() {
        backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setOnAction(actionEvent -> colorModel.setBackgroundColor(backgroundColorPicker.getValue()));
        backgroundColorPickerContainer.getChildren().addAll(backgroundColorPicker);

        textColorPicker = new ColorPicker();
        textColorPicker.setOnAction(actionEvent -> colorModel.setTextColor(textColorPicker.getValue()));
        textColorPickerContainer.getChildren().addAll(textColorPicker);

        chkShowRank.setOnAction(actionEvent -> colorModel.setShowRank(chkShowRank.isSelected()));
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
        backgroundColorPicker.setValue(colorModel.getBackgroundColor());
        textColorPicker.setValue(colorModel.getTextColor());
        chkShowRank.setSelected(colorModel.isShowRank());
    }

    public ColorModel getColorModel() {
        return this.colorModel;
    }
}
