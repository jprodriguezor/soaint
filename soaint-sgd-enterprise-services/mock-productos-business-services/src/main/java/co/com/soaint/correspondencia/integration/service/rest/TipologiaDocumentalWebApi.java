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


@Path("/tipologia-documental-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipologiaDocumentalWebApi {

    public TipologiaDocumentalWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ListaSeleccionSimpleDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ListaSeleccionSimpleDTO> response = new ArrayList<>();
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(1).nombre("DEMANDA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(2).nombre("DENUNCIAS").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(3).nombre("DERECHO DE PETICIÓN").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(4).nombre("FACTURA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(5).nombre("INVESTIGACIÓN").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(6).nombre("INVITACIÓN").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(7).nombre("OFICIO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(8).nombre("PQRSD").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(9).nombre("RECURSOS").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(10).nombre("TUTELA").build());

		return response;
	}

}