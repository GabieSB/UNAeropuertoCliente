package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioMantenimientoService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();


    public Respuesta create(ServicioMantenimientoDto servicio){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("servicios_mantenimientos/update", gson.toJson(servicio));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            //List<AuthenticationResponse> users = new Gson().fromJson(respuesta.body().toString(), new TypeToken<>() {}.getType());
            ServicioMantenimientoDto servicioMantenimientoDto = gson.fromJson(respuesta.body().toString(), ServicioMantenimientoDto.class);



            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta update(ServicioMantenimientoDto servicio){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("servicios_mantenimientos/create", gson.toJson(servicio));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            //List<AuthenticationResponse> users = new Gson().fromJson(respuesta.body().toString(), new TypeToken<>() {}.getType());
            ServicioMantenimientoDto servicioMantenimientoDto = gson.fromJson(respuesta.body().toString(), ServicioMantenimientoDto.class);



            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarEntreFechas(LocalDate fehaInicio , LocalDate fechaFinal){
        try {
            System.out.printf("En crear servicio");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findEntreFechas/"+formatter.format(fehaInicio)+"/"+formatter.format(fechaFinal));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorMatricula(String matricula){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByEstadoAvionesMatricula/"+matricula);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorTipoNombre(String tipo){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByTiposServiciosNombre/"+tipo);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorEstadoFinalizacion(Boolean estado){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByEstadoFinalizacion/"+estado.toString());
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorEstadoPago(Boolean estado){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByEstadoPago/"+estado.toString());
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorEstado(Boolean estado){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByEstado/"+estado.toString());
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorNumeroFactura(Long numero){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/findByNumeroFactura/"+numero);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            List<ServicioMantenimientoDto> servicioMantenimientoDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<ServicioMantenimientoDto>>() {}.getType());
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
    public Respuesta buscarPorID(int numero){
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("servicios_mantenimientos/"+numero);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }

            ServicioMantenimientoDto servicioMantenimientoDto = gson.fromJson(respuesta.body().toString(), ServicioMantenimientoDto.class);
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
