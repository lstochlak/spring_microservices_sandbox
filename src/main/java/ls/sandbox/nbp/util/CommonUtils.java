//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.util
//
// File Name : CommonUtils.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.log4j.Log4j2;

/**
 * Common utilities.
 *
 * @author Lukasz.Stochlak
 */
@Log4j2
public class CommonUtils
{
    /**
     * Empty hidden constructor.
     */
    private CommonUtils()
    {
        //empty hidden constructor
    }

    /**
     * Parses date from string. It expects "yyyy-MM-dd" format.
     *
     * @param date date as string in "yyyy-MM-dd" pattern (ISO 8601 standard)
     * @return
     */
    public static Date parseDateFromString(String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            return formatter.parse(date);
        }
        catch (ParseException e)
        {
            log.error("Unexpected exception! " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Formats date to string in "yyyy-MM-dd" pattern (ISO 8601 standard).
     *
     * @param date date to be formatted.
     * @return date formatted as stringin "yyyy-MM-dd" pattern (ISO 8601 standard).
     */
    public static String toStringFromDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(date);
    }
}
//------------------------------------------------------------------------------
