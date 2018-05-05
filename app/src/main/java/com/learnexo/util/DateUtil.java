package com.learnexo.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static String convertDateToAgo(Date date){
        if(null!=date) {
            long millisecond = date.getTime();
            String dateString = DateFormat.format("yyyy.MM.dd 'AD at' HH:mm:ss",
                    new Date(millisecond)).toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date past = null;
            try {
                past = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                return seconds + " sec ago";
            } else if (minutes < 60) {
                return minutes + " min ago";
            } else if (hours < 24) {
                return hours + " hr ago";
            } else {
                if (days > 365) {
                    long years = days / 365;
                    if (years > 1)
                        return years + " years ago";
                    else
                        return years + " year ago";
                }
                return days + " days ago";
            }
        }
        return null;
    }

}
