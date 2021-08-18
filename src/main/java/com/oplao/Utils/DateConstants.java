package com.oplao.Utils;

import com.oplao.service.LanguageService;
import org.joda.time.DateTimeConstants;

import java.util.ResourceBundle;

public class DateConstants {

    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;


    public static String convertTimeToAmPm(int time){

        if(time>=1200){
            time = time-1200;

            String timeStr = "" + time;

            if(timeStr.length()==3){
                return timeStr.substring(0,1) + " PM";
            }else if(timeStr.length()==4){
                return timeStr.substring(0,2) + " PM";
            }
            else if(timeStr.length()==1){
                return "12 PM";
            }
        }else {
            String timeStr = "" + time;

            if(timeStr.length()==3){
                return timeStr.substring(0,1) + " AM";
            }else if(timeStr.length()==4){
                return timeStr.substring(0,2) + " AM";
            }else if(timeStr.length()==1){
                return "12 AM";
            }
        }
        return null;
    }


    public static String convertDayOfWeek(int day, ResourceBundle bundle) {
        switch (day) {
            case DateTimeConstants.MONDAY
                    : return bundle.getString("monday");
            case DateTimeConstants.TUESDAY
                    : return bundle.getString("tuesday");

            case DateTimeConstants.WEDNESDAY
                    : return bundle.getString("wednesday");

            case DateTimeConstants.THURSDAY
                    : return bundle.getString("thursday");

            case DateTimeConstants.FRIDAY
                    : return bundle.getString("friday");

            case DateTimeConstants.SATURDAY
                    : return bundle.getString("saturday");

            case DateTimeConstants.SUNDAY
                    : return bundle.getString("sunday");
            default:return "wrong value for field 'day of week' ";

        }
    }
    public static String[] getDayTimes(ResourceBundle bundle){
        String[] times = new String[4];
        times[0] = LanguageService.encode(bundle.getString("timeOfDay.Night"));
        times[1] = LanguageService.encode(bundle.getString("timeOfDay.Morning"));
        times[2] = LanguageService.encode(bundle.getString("timeOfDay.Midday"));
        times[3] = LanguageService.encode(bundle.getString("timeOfDay.Evening"));
        return times;
    }
    public static String convertMonthOfYear(int month, ResourceBundle bundle) {
        switch (month) {
            case DateConstants.JANUARY
                    :
                return bundle.getString("january");

            case DateConstants.FEBRUARY
                    :
                return bundle.getString("february");
            case DateConstants.MARCH
                    :
                return bundle.getString("march");
            case DateConstants.APRIL
                    :
                return bundle.getString("april");
            case DateConstants.MAY
                    :
                return bundle.getString("mayLong");
            case DateConstants.JUNE
                    :
                return bundle.getString("june");
            case DateConstants.JULY
                    :
                return bundle.getString("july");
            case DateConstants.AUGUST
                    :
                return bundle.getString("august");
            case DateConstants.SEPTEMBER
                    :
                return bundle.getString("september");
            case DateConstants.OCTOBER
                    :
                return bundle.getString("october");
            case DateConstants.NOVEMBER
                    :
                return bundle.getString("november");
            case DateConstants.DECEMBER
                    :
                return bundle.getString("december");
            default:
                return "Wrong value for field 'month'";
        }
    }

    public static String convertMonthOfYearShort(int month, ResourceBundle bundle) {
        switch (month) {
            case DateConstants.JANUARY
                    :
                return bundle.getString("jan");

            case DateConstants.FEBRUARY
                    :
                return bundle.getString("feb");
            case DateConstants.MARCH
                    :
                return bundle.getString("mar");
            case DateConstants.APRIL
                    :
                return bundle.getString("apr");
            case DateConstants.MAY
                    :
                return bundle.getString("may");
            case DateConstants.JUNE
                    :
                return bundle.getString("jun");
            case DateConstants.JULY
                    :
                return bundle.getString("jul");
            case DateConstants.AUGUST
                    :
                return bundle.getString("aug");
            case DateConstants.SEPTEMBER
                    :
                return bundle.getString("sep");
            case DateConstants.OCTOBER
                    :
                return bundle.getString("oct");
            case DateConstants.NOVEMBER
                    :
                return bundle.getString("nov");
            case DateConstants.DECEMBER
                    :
                return bundle.getString("dec");
            default:
                return "Wrong value for field 'month'";
        }
    }
}
