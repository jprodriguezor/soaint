package co.com.soaint.digitalizacion.services.integration.services;

import co.com.soaint.digitalizacion.services.integration.services.IProcesarFichero;
import co.com.soaint.digitalizacion.services.integration.services.impl.ProcesarFichero;
import co.com.soaint.digitalizacion.services.util.SystemParameters;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by amartinez on 06/03/2018.
 */

@Log4j2
@NoArgsConstructor
public class Cron implements Job {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring/core-config.xml");
    IProcesarFichero procesarFichero = context.getBean(IProcesarFichero.class);
   // private IProcesarFichero procesarFichero;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("**** Iniciando proceso de lectura ****");


       // ProcesarFichero procesarFichero = new ProcesarFichero();
        try {
          procesarFichero.leerDirectorio(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR),SystemParameters.getParameter(SystemParameters.DIR_PROCESADAS));
           //procesarFichero.leerDirectorio("C:\\sgd\\procesar\\","C:\\sgd\\procesados\\");

        } catch (Exception ex) {
            log.error("*** Error al procesar los ficheros *** ", ex);
        }
    }

//    public static void main(String[] args) throws ParseException {
//        ProcesarFichero procesarFichero = new ProcesarFichero();
//        try {
//            procesarFichero.leerDirectorio("C:\\sgd\\procesar\\","C:\\sgd\\procesados\\");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FormatException e) {
//            e.printStackTrace();
//        } catch (ChecksumException e) {
//            e.printStackTrace();
//        }
//
//    }
}
