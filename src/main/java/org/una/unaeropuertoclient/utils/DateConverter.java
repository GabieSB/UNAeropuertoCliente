/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Roberth :)
 */
public class DateConverter {

    public static String convertToSpringBoot(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String newDateString = sdf.format(fecha);
        return newDateString;
    }
}
