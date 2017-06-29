package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * @author sarias
 *
 */
public final class Configuracion {

	private static Properties propiedades = null;
	static final Logger LOGGER = Logger.getLogger(Configuracion.class.getName());
	
	public static void inicializacion(){
		
		if(propiedades == null){
			propiedades = new Properties();
		}

	}
	
	public static String getPropiedad(String name) throws IOException {
		
		String propiedad = null;
		try {
			inicializacion();
		    propiedades.load(new FileInputStream("ecm-integration-services/src/main/resources/configurationServices.properties"));
		    propiedad = propiedades.getProperty(name);
		  } catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Error al leer properties de configuracion");
		  }
		  return propiedad;
		 }
}
