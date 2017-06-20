package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.foundation.canonical.mock.ListaSeleccionSimpleDTO;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


@Path("/dependencia-grupo-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DependeciaGrupoWebApi {

    public DependeciaGrupoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ListaSeleccionSimpleDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ListaSeleccionSimpleDTO> response = new ArrayList<>();
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(1).nombre("DESPACHO DIRECCION TERRITORIAL").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(2).nombre("GRUPO DE ATENCION AL CIUDADANO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(3).nombre("GRUPO DE PREVENCION, INSPECCION, VIGILANCIA, CONTROL").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(4).nombre("GRUPO DE RESOLUCION DE CONFLICTOS-CONCILIACION").build());

		return response;
	}

}