package com.demo.starwars.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Suresh on 6/13/2018.
 */
public class Converter
{
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final static String REQUIRED_DATE_FORMAT_PATTERN = "dd MMM, yyyy HH:mm a";

    public static String parseDate(String stringToParse) {
        String stringTo ="";
        try {
            Date date  = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(stringToParse);
            stringTo = new SimpleDateFormat(REQUIRED_DATE_FORMAT_PATTERN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringTo;
    }

    public static String cmToMeter (String stringToParse)
    {
        String stringTo ="";

        Double data = null;
        try {
            data = (Integer.parseInt(stringToParse)/100.00);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            data=0.0;
        }
        return String.valueOf(data);
    }

}