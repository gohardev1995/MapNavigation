package com.invotech.runshuffle.Models;

public class Options
{
    private Round_trip round_trip;

    public Options(Round_trip round_trip) {
        this.round_trip = round_trip;
    }

    public Round_trip getRound_trip ()
    {
        return round_trip;
    }

    public void setRound_trip (Round_trip round_trip)
    {
        this.round_trip = round_trip;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [round_trip = "+round_trip+"]";
    }
}

