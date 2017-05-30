package co.com.soaint.correspondencia.integration.service.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import co.com.soaint.foundation.canonical.mock.ProductoDTO;

/**
 * Created by esanchez on 5/24/2017.
 */

@Path("/productos-web-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductosWebApi {

    public ProductosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


	@GET
	@Path("/")
	public List<ProductoDTO> list() {

		//TODO: get information from database using business boundaries
		
		List<ProductoDTO> Productos = new ArrayList<>();
		Productos.add(ProductoDTO.newBuilder().id(1).nombre("card subscription").costo(20000).build());
		Productos.add(ProductoDTO.newBuilder().id(2).nombre("online gift").costo(25000).build());
		Productos.add(ProductoDTO.newBuilder().id(3).nombre("dron online").costo(3000).build());
		Productos.add(ProductoDTO.newBuilder().id(4).nombre("others").costo(4000).build());

		return Productos;
	}

}