package net.devcats.recordtracker;

import javafx.scene.paint.Color;

public class ColorModel {
    private Color backgroundColor;
    private Color textColor;
    private boolean showRank;

    public ColorModel() { }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public boolean isShowRank() {
        return showRank;
    }

    public void setShowRank(boolean showRank) {
        this.showRank = showRank;
    }
}
