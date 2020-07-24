package com.invotech.runshuffle.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Round {
    @SerializedName("coordinates")
    private String[][] coordinates;

    private Options options;


    public String[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String[][] coordinates) {
        this.coordinates = coordinates;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "ClassPojo [coordinates = " + coordinates + ", options = " + options + "]";
    }
}
