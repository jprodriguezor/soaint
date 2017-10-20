package co.com.soaint.bpm.services.util;

import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.kie.api.task.model.Status;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arce on 8/16/2017.
 */
@Component
public class Estados {

    public Estados() {
        //
    }

    /**
     * Permite permite parsear los estados que son enviados en las llamadas
     *
     * @param estadosEnviados Enum de estados
     * @return Lista de estados activos en la llamada
     * @throws MalformedURLException
     */
    public List<Status> estadosActivos(Iterator<EstadosEnum> estadosEnviados) {
        List<Status> estadosActivos = new ArrayList<>();
        while (estadosEnviados.hasNext()) {
            String estadoActivo = estadosEnviados.next().name();
            for (EstadosEnum estado : EstadosEnum.values()) {
                if (estado.name().equals(estadoActivo)) {
                    estadosActivos.add(Status.valueOf(estado.getNombre()));
                }
            }
        }
        return estadosActivos;
    }

    /**
     * Permite cambiar el idioma del estado
     *
     * @param estado cadena de texto con el estado en Ingles
     * @return estado en espannol
     * @throws MalformedURLException
     */
    public String estadoRespuesta(String estado) {
        String estadoRespuesta =  "";
                switch (estado){
                    case "DELEGATED":
                        estadoRespuesta = "DELEGADO";
                        break;
                    case "RELEASED":
                        estadoRespuesta = "LIBERADO";
                        break;
                    case "CLAIMED":
                        estadoRespuesta = "RECLAMADO";
                        break;
                    case "STARTED":
                        estadoRespuesta = "INICIADO";
                        break;
                    default :
                        for (Status enumEstado : Status.values()) {
                            if (enumEstado.name().equalsIgnoreCase(estado)) {
                                return EstadosEnum.obtenerClave(estado).toString();
                            }
                         }
                }
           return estadoRespuesta;
    }

    /**
     * Permite crear lista de parametros para realizar llamada
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de valores
     */
    public List<NameValuePair> listaEstadosProceso(EntradaProcesoDTO entrada) {
        List<NameValuePair> estadoProceso = new ArrayList<>();

        for (String key : entrada.getParametros().keySet()) {
            Object value = entrada.getParametros().get(key);
            estadoProceso.add(new BasicNameValuePair("pist", value.toString()));
        }

        return estadoProceso;
    }
}
