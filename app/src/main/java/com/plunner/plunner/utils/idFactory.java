package com.plunner.plunner.utils;

/**
 * A singleton class that provides integer progressive ids
 * @author Giorgio Pea
 */
public class idFactory {

    private static long reservoir;

    /**
     * Generates an integer progressive id
     * @return The generated id
     */
    public static long generate(){
        return reservoir++;
    }

    private idFactory(){}
}
