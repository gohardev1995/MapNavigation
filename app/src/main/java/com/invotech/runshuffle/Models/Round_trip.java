package com.invotech.runshuffle.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Round_trip {

    @SerializedName("seed")
    private String seed;
    @SerializedName("length")
    private String length;

    public Round_trip(String seed, String length) {
        this.seed = seed;
        this.length = length;
    }

    public String getSeed ()
    {
        return seed;
    }

    public void setSeed (String seed)
    {
        this.seed = seed;
    }

    public String getLength ()
    {
        return length;
    }

    public void setLength (String length)
    {
        this.length = length;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [seed = "+seed+", length = "+length+"]";
    }

}
