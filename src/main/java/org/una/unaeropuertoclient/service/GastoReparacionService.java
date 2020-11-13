package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.GastoReparacionDto;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.utils.RequesUtils;

import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;

public class GastoReparacionService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta create(GastoReparacionDto gastoReparacion) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("gastos_reparaciones/create", gson.toJson(gastoReparacion));
            System.out.println(gson.toJson(gastoReparacion));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            GastoReparacionDto gastoReparacionDto = gson.fromJson(respuesta.body().toString(), GastoReparacionDto.class);

            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " CrearGasto() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta update(GastoReparacionDto servicio) {
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("gastos_reparaciones/update", gson.toJson(servicio));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            //List<AuthenticationResponse> users = new Gson().fromJson(respuesta.body().toString(), new TypeToken<>() {}.getType());
            GastoReparacionDto gastoReparacionDto = gson.fromJson(respuesta.body().toString(), GastoReparacionDto.class);

            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarEntreFechas(LocalDate fehaInicio, LocalDate fechaFinal) {
        try {
            System.out.printf("En crear servicio");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findEntreFechas/" + formatter.format(fehaInicio) + "/" + formatter.format(fechaFinal));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarEntreDiasDurabilidad(String inicio, String fin) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findEntreDiasDuracion/" + inicio + "/" + fin);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarEntreDiasPeriocidad(String inicio, String fin) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findEntreDiasPeriocidad/" + inicio + "/" + fin);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, "BuscarEntreDias() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorTipoNombre(String tipo) {
        try {
            System.out.print("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findByTipoReparacionNombre/" + tipo);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorEstadoPago(Boolean estado) {
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findByEstadoPago/" + estado.toString());
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorEstado(Boolean estado) {
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/findByEstado/" + estado.toString());
            System.out.println(respuesta.body().toString());
            System.out.println(respuesta.statusCode());

            if (respuesta.statusCode() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            List<GastoReparacionDto> gastoReparacionDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<GastoReparacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " BuscarMedianteEstado() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorNumeroContrato(Long numero) {
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/getByNumeroContrato/" + numero);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            GastoReparacionDto gastoReparacionDto = gson.fromJson(respuesta.body().toString(), GastoReparacionDto.class);
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " BuscarNumeroDeContrato->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());

        }
    }

    public Respuesta buscarPorID(String numero) {
        try {
            System.out.printf("En crear servicio");
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/" + numero);
            System.out.println(respuesta.body().toString());

            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            GastoReparacionDto gastoReparacionDto = gson.fromJson(respuesta.body().toString(), GastoReparacionDto.class);
            return new Respuesta(true, "", "", "data", gastoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " BuscarMedianteID() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta findByIdUsingListParam(List<Long> idList) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("gastos_reparaciones/findByIdUsingListParam/", gson.toJson(idList));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<GastoReparacionDto>asList(respuesta, GastoReparacionDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta filter(String numeroContrato, String tipo, String proveedor, String activo,  String pago,String fechaI, String fechaF, String diasDurabilidadI, String diasDurabilidadF, String diasPeriocidadI, String diasPeriocidadF) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            numeroContrato = (numeroContrato.isBlank()) ? "none" : numeroContrato.trim();
            tipo = (tipo.isBlank()) ? "none" : tipo.trim();
            proveedor = (proveedor.isBlank())? "none" : proveedor.trim();

            HttpResponse respuesta = requestHTTP.get("gastos_reparaciones/filter/" + numeroContrato + "/" + tipo + "/" + proveedor + "/"+ activo + "/" + pago  + "/" + fechaI + "/" + fechaF + "/"+ diasDurabilidadI + "/" + diasDurabilidadF+ "/" + diasPeriocidadI+ "/" + diasPeriocidadF  );
            System.out.println(respuesta.body().toString());
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar los servicios, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay servicios que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.asList(respuesta, GastoReparacionDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }
}
