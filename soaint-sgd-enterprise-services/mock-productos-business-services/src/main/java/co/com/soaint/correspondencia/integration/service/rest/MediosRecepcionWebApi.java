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


@Path("/medios-recepcion-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MediosRecepcionWebApi {

    public MediosRecepcionWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ListaSeleccionSimpleDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ListaSeleccionSimpleDTO> response = new ArrayList<>();
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(1).nombre("CORREO ELECTRONICO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(2).nombre("CORRESPONDENCIA MASIVA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(3).nombre("EMPRESA DE MENSAJERIA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(4).nombre("ESCRITO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(5).nombre("FAX SERVER").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(6).nombre("PRESENCIAL").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(7).nombre("PRODUCCION DOCUMENTAL").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(8).nombre("RADICACION CONTINGENCIA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(9).nombre("SEDE ELECTRONICA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(10).nombre("TELEFONICO").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(11).nombre("VENTANILLA").build());
		response.add(ListaSeleccionSimpleDTO.newBuilder().id(12).nombre("VIRTUAL").build());

		return response;
	}

}