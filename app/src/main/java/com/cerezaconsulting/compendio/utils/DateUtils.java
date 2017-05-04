package com.cerezaconsulting.compendio.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by junior on 04/04/17.
 */

public class DateUtils {

    public static String getFormant(Date date) {


        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }


    public static String getFormantSingle(Date date) {


        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static boolean dateIsCurrent(Date date) {
        Date todayDate = new Date();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        return dateFormatter.format(todayDate).equals(dateFormatter.format(date));
    }

    public static boolean comparteDates(Date date) {
        Date todayDate = new Date();



        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        Log.d("DATE",dateFormatter.format(todayDate).equals(dateFormatter.format(date))+"**");
        return todayDate.after(date);
    }
}
