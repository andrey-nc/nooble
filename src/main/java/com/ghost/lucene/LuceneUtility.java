package com.ghost.lucene;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class LuceneUtility {

    /**
     * Transforms given time period in milliseconds to string representation like:
     * 8105340 ms -> "2 hours 15 minutes 5,34 seconds"
     * @param timeInMillis is a time period to be converted
     * @return formatted time string
     */
    public static String formatTime(long timeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
        long allMinutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
        long minutes = allMinutes - TimeUnit.HOURS.toMinutes(hours);
        long milliseconds = timeInMillis - TimeUnit.MINUTES.toMillis(allMinutes);
        String secondsString = new DecimalFormat("#.##").format(milliseconds / 1000.0) + " seconds";
        String minutesString = minutes + " minutes " + secondsString;
        String hoursString = hours + " hours " + minutesString;
        return hours > 0 ? hoursString : minutes > 0 ? minutesString : secondsString;
    }
}
