package com.jbion.leekwars.utils;

public class PrintUtils {

    public static String toCamelCase(String constantName) {
        String lowercase = constantName.toLowerCase();
        int index = lowercase.indexOf('_');
        while (index >= 0) {
            if (index < lowercase.length() - 1) {
                String capChar = String.valueOf(lowercase.charAt(index + 1)).toUpperCase();
                lowercase = lowercase.substring(0, index) + capChar + lowercase.substring(index + 2);
            } else {
                lowercase = lowercase.substring(0, index);
            }
            index = lowercase.indexOf('_');
        }
        return lowercase;
    }
    
}
