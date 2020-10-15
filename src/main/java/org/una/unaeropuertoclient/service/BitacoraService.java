package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.RequestHTTP;

import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class BitacoraService {

    public void create(String descripcion){

        descripcion = "se ingresa al sistma";

        Gson gson = new GsonBuilder()
                .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

        AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");
        BitacoraDto bitacoraDto =  new BitacoraDto(new Timestamp(System.currentTimeMillis()), descripcion,  authentication.getUsuario());
        System.out.println(bitacoraDto.getFechaModificacion().getTime());
        RequestHTTP requestHTTP = new RequestHTTP();

        HttpResponse response =   requestHTTP.post("biatacoras/create", gson.toJson(bitacoraDto));

        System.out.println(response.statusCode() + ": " + response.body());
    }
}
