package com.jrw35outlook.headphonedndtoggle;

public enum Day {
    SUNDAY(0, "Sun"), MONDAY(1, "Mon"), TUESDAY(2, "Tues"), WEDNESDAY(3, "Wed"), THURSDAY(4, "Thurs"), FRIDAY(5, "Fri"), SATURDAY(6, "Sat");

    private final int value;
    private final String abbr;
    Day(int value, String theString) {
        this.value = value;
        this.abbr = theString;
    }

    public int toInt(){
        return value;
    }

    public String toString(){
        return abbr;
    }
}
