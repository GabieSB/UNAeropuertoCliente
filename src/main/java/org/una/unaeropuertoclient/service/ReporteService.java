/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author LordLalo
 */
public class ReporteService {

    public Respuesta getReporteServicio(LocalDate fechaInicial, LocalDate fechaFinal, String nombre) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            String n = nombre.replace(' ', '_');
            HttpResponse respuesta = requestHTTP.get("reporte/generateReportService/" + fechaInicial + "/" + fechaFinal + "/" + n);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Informacion generada incorrectamente", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());

            return new Respuesta(true, "", "", "data", respuesta.body().toString());

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " Reportes->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

    public Respuesta getReporteDepartamento(LocalDate fechaInicial, LocalDate fechaFinal, String nombreD) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            String pNombreD= nombreD.replace(' ', '_');
            HttpResponse respuesta = requestHTTP.get("reporte/generateReportMantenimiento/" + fechaInicial + "/" + fechaFinal + "/" + pNombreD);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Informacion generada incorrectamente", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());

            return new Respuesta(true, "", "", "data", respuesta.body().toString());

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " Reportes->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

    public Respuesta getReporteVuelo(LocalDate fechaInicial, LocalDate fechaFinal, String area,String tipoVuel) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            String pArea = area.replace(' ', '_');
            HttpResponse respuesta = requestHTTP.get("reporte/generateReportVuelo/" + fechaInicial + "/" + fechaFinal + "/" + pArea+"/"+tipoVuel);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Informacion generada incorrectamente", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());

            return new Respuesta(true, "", "", "data", respuesta.body().toString());

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " Reportes->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

}
