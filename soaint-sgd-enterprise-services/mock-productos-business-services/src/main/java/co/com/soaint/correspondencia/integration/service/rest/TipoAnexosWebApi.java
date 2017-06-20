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


@Path("/tipo-anexos-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoAnexosWebApi {

    public TipoAnexosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ListaSeleccionSimpleDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ListaSeleccionSimpleDTO> response = new ArrayList<>();
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(1).nombre("CD").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(2).nombre("CAJA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(3).nombre("EXPEDIENTE").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(4).nombre("FOLLETO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(5).nombre("FOTOGRADFIA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(6).nombre("HOJAS DE VIDA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(7).nombre("INVITACIONES").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(8).nombre("LIBRO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(9).nombre("OFICIOS").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(10).nombre("OTRO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(11).nombre("PERIODICOS").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(12).nombre("PRUEBAS").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(13).nombre("REVISTA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(14).nombre("SOBRE SELLADO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(15).nombre("USB").build());

		return response;
	}

}