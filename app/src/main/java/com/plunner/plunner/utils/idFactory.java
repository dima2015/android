package com.plunner.plunner.utils;

/**
 * Created by giorgiopea on 24/02/16.
 */
public class idFactory {

    private static long reservoir;

    public static long generate(){
        return reservoir++;
    }
}
