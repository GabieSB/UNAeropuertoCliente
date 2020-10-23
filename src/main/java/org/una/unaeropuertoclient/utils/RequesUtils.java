/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberth :)
 */
public class RequesUtils {

    public static boolean isError(int status) {
        return status < 200 || status > 299;
    }

    public static boolean isEmptyResult(int status) {
        return status == 204;
    }

    public static <ObjectType> List<ObjectType> asList(HttpResponse response, Class<ObjectType> ObjectTypeClass) {
        Type objType = TypeToken.getParameterized(List.class, ObjectTypeClass).getType();
        return new Gson().fromJson(response.body().toString(), objType);
    }

    public static <ObjectType> ObjectType asObject(HttpResponse resp, Class<ObjectType> t) {
        ObjectType obj = new Gson().fromJson(resp.body().toString(), t);
        return obj;
    }

}
