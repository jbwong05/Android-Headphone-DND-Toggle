package com.jrw35outlook.headphonedndtoggle;

public enum Day {
    SUNDAY("Sun"), MONDAY("Mon"), TUESDAY("Tues"), WEDNESDAY("Wed"), THURSDAY("Thurs"), FRIDAY("Fri"), SATURDAY("Sat");

    private final String abbr;
    Day(String theString) {
        this.abbr = theString;
    }

    public String toString(){
        return abbr;
    }
}
