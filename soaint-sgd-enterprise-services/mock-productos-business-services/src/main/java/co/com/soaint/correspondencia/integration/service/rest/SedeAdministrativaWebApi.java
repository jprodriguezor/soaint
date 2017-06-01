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


@Path("/pais-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SedeAdministrativaWebApi {

    public SedeAdministrativaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ListaSeleccionSimpleDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ListaSeleccionSimpleDTO> response = new ArrayList<>();
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(1).nombre("D.T MAGADALENA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(2).nombre("D.T META").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(3).nombre("D.T NARIÑO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(4).nombre("D.T NORTE DE SANTANDER").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(5).nombre("D.T PUTUMAYO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(6).nombre("CENTRALES D.T").build());

		return response;
	}

}