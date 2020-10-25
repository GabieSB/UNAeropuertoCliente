/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javafx.scene.control.DatePicker;

/**
 *
 * @author Roberth :)
 */
public class VuelosUtilis {

    public static LocalDateTime toLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(DatePicker dp, String hours, String minuts) {
        LocalDateTime locaDT = dp.getValue().atTime(Integer.valueOf(hours), Integer.valueOf(minuts));
        return Date.from(locaDT.atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean isfechaSinCambios(DatePicker dp, String hours, String minuts, Date fechaVuelo) {
        if (fechaVuelo != null) {
            return toDate(dp, hours, minuts).equals(fechaVuelo);
        }
        return true;
    }
}
