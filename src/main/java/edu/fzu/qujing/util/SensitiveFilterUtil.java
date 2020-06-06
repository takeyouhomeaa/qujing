package edu.fzu.qujing.util;

import edu.fzu.qujing.component.sensitivewords.SensitiveFilter;

/**
 * @author ozg
 */
public class SensitiveFilterUtil {
    private static SensitiveFilter sensitiveFilter = new SensitiveFilter();

    public static SensitiveFilter getSensitiveFilter() {
        return sensitiveFilter;
    }

    public static String filter(String sentence){
        String filter = sensitiveFilter.filter(sentence, '*');
        return filter;
    }


}
