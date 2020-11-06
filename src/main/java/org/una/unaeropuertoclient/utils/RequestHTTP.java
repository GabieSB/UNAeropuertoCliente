package org.una.unaeropuertoclient.utils;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.utils.AppContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHTTP {
    private String urlApi ="http://localhost:1820/";
    private HttpResponse respuesta = null;
    private HttpRequest.Builder requestBuilder;

    HttpClient client;

    public void request(String url){
        AuthenticationResponse  authentication = (AuthenticationResponse) AppContext.getInstance().get("token");
        client = HttpClient.newHttpClient();
        requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(urlApi + url));


        if(authentication!=null) requestBuilder.header("Authorization", "Bearer " + authentication.getJwt());
        requestBuilder.header("content-type", "application/json");

    }
    public HttpResponse post(String url, String body){

        try {
            request(url);
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
            HttpRequest request = requestBuilder.build();
            this.respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
          
            return respuesta;

        } catch (IOException  | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        return respuesta;

    }


    public HttpResponse put(String url, String body ){

        try {
            request(url);
            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
            HttpRequest request = requestBuilder.build();
            this.respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());

            return respuesta;

        } catch (IOException  | InterruptedException ex) {
            // handle exception
            System.out.println(ex.getMessage());

        }

        return respuesta;

    }

    public HttpResponse get(String url){

        try {
            request(url);
            requestBuilder.GET();
            HttpRequest request = requestBuilder.build();
            this.respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());

            return respuesta;

        } catch (IOException  | InterruptedException ex) {
            // handle exception
            System.out.println(ex.getMessage());

        }

        return respuesta;

    }

    public Boolean isError() {
        return respuesta.statusCode() < 200 || respuesta.statusCode() > 299;
    }
    public int getStatus() {
        return respuesta.statusCode();
    }
}
