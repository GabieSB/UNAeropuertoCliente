package org.una.unaeropuertoclient.utils;

import javafx.scene.control.Alert;

public class Validar {

    public static boolean isLongNumber(String numero){

        try {
            long a = Long.parseLong(numero);
            return  true;
        }catch(NumberFormatException ex){
            return  false;
        }
    }
}
