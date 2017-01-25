package com.s_space.kernel;

/**
 * Created by zksun on 25/01/2017.
 */
public class SpaceUtilities {


    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(String s, boolean isWithTrim) {
        if (null == s) {
            return true;
        } else {
            if (isWithTrim) {
                s = s.trim();
            }

            return s.length() == 0;
        }
    }

}
