package co.com.soaint.digitalizacion.services.integration.services;

import co.com.soaint.digitalizacion.services.util.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by amartinez on 06/03/2018.
 */

@Log4j2
@NoArgsConstructor
public class Cron implements Job {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring/core-config.xml");
    IProcesarFichero procesarFichero = context.getBean(IProcesarFichero.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("**** Iniciando proceso de lectura ****");
        try {
           // procesarFichero.leerDirectorio(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR),SystemParameters.getParameter(SystemParameters.DIR_PROCESADAS));
            procesarFichero.leerDirectorioEvento(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR),SystemParameters.getParameter(SystemParameters.DIR_PROCESADAS));
        } catch (Exception ex) {
            log.error("*** Error al procesar los ficheros *** ", ex);
        }
    }

}
