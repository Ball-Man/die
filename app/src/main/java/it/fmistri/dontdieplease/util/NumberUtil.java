package it.fmistri.dontdieplease.util;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Container for static methods regarding numbers(in particular, decimal conversion).
 */
public class NumberUtil {
    /**
     * Parse a double from a string. A context is required to gather locale information.
     * @param context The context(required for locale information).
     * @param str The string to parse.
     * @return A double.
     */
    public static double parseDouble(Context context, String str) throws ParseException {
        // Get current locale
        Locale loc = context.getResources().getConfiguration().locale;
        return NumberFormat.getInstance(loc).parse(str).doubleValue();
    }
}
