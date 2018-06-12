package com.jrw35outlook.headphonedndtoggle;

import java.util.Calendar;

public class ReenableState {
    public boolean toReenable;
    private boolean[] days;
    private Calendar startTime;
    private Calendar endTime;

    public ReenableState(){
        toReenable = false;
        days = new boolean[7];
        setStartTime("1200a");
        setEndTime("0100a");
    }

    public boolean[] getDaysArray() {
        return days;
    }

    public String getDaysString() {
        String toReturn = "";
        for(int i=0; i<7; i++){
            if(days[i]){
                toReturn += Day.values()[i].toString() + " ";
            }
        }
        return toReturn;
    }

    public String getStartTime(){
        return getTime(startTime);
    }

    public String getEndTime() {
        return getTime(endTime);
    }

    private String getTime(Calendar calendar){
        return "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
    }

    public void setDays(String theDays){
        days = new boolean[7];
        for(int i=0; i<7; i++){
            days[i] = theDays.substring(i, i+1).equals(String.valueOf(R.string.renable));
        }
    }

    public void updateDays(boolean[] newDays){
        days = newDays;
    }

    public void setStartTime(String theStartTime) {
        setTime(startTime, theStartTime);
    }

    public void setEndTime(String theEndTime) {
        setTime(endTime, theEndTime);
    }

    private void setTime(Calendar calendar, String time){
        calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR, Integer.parseInt(time.substring(0, 2))-1);
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4))-1);
        if (time.substring(4, 5).equals("a")) {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        } else {
            calendar.set(Calendar.AM_PM, Calendar.PM);
        }
    }

    public String toString(){
        String toReturn = "";
        toReturn += toReenable ? "1\n" : "0\n";
        for(boolean bool : days){
            toReturn += bool ? "1" : "0";
        }
        toReturn += "\n" + addZero(startTime.get(Calendar.HOUR)) + addZero(startTime.get(Calendar.MINUTE));
        toReturn += "\n" + addZero(endTime.get(Calendar.HOUR)) + addZero(endTime.get(Calendar.MINUTE));
        return toReturn;
    }

    private String addZero(int num){
        return (num<10) ? "0" + num : "" + num;
    }
}
