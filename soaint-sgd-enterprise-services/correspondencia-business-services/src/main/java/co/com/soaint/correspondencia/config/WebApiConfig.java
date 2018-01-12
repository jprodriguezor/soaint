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
import lombok.extern.log4j.Log4j2;

import javax.management.ObjectName;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
@Log4j2
public class WebApiConfig extends Application {
    public WebApiConfig() {
        //Init Rest Api
        initSwagger();
    }

    private void initSwagger() {
        try {
            ObjectName socketBinding = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
            Integer port = (Integer) ManagementFactory.getPlatformMBeanServer().getAttribute(socketBinding, "port");
            String host = (String) ManagementFactory.getPlatformMBeanServer().getAttribute(socketBinding, "boundAddress");
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setTitle("Correspondencia Business Services");
            beanConfig.setVersion("1.0");
            beanConfig.setSchemes(new String[]{"http"});
            beanConfig.setHost(host + ":" + port);
            beanConfig.setBasePath("/correspondencia-business-services/services");
            beanConfig.setResourcePackage("co.com.soaint.correspondencia.integration.service.rest");
            beanConfig.setScan(true);
        } catch (Exception ex) {
            log.error("Rest Api - a system error has occurred", ex);
        }
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
        resources.add(PlantillaWebApi.class);
        resources.add(TareaWebApi.class);

        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        return resources;
    }
}