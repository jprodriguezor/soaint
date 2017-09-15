package co.com.soaint.correspondencia.config;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: Config
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

import co.com.soaint.correspondencia.integration.service.rest.*;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class WebApiConfig extends Application {
    public WebApiConfig() {
        //Init Rest Api
        initSwagger();
    }

    private void initSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Probando uso de Swagger con JAX-RS en Wildfly");
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("192.168.3.242:28080");
        beanConfig.setBasePath("/correspondencia-business-services/services");
        beanConfig.setResourcePackage("co.com.soaint.correspondencia.integration.service.rest");
        beanConfig.setScan(true);
    }
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AgenteWebApi.class);
        resources.add(AsignacionWebApi.class);
        resources.add(ConstantesWebApi.class);
        resources.add(CorrespondenciaWebApi.class);
        resources.add(DepartamentosWebApi.class);
        resources.add(DependenciasWebApi.class);
        resources.add(DocumentoWebApi.class);
        resources.add(FuncionariosWebApi.class);
        resources.add(MunicipiosWebApi.class);
        resources.add(OrganigramaAdministrativoWebApi.class);
        resources.add(PaisesWebApi.class);
        resources.add(PlanillasWebApi.class);
        resources.add(TareaWebApi.class);

        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        return resources;
    }
}