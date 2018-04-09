package co.com.soaint.digitalizacion.services;


import co.com.soaint.digitalizacion.services.integration.services.Cron;
import co.com.soaint.digitalizacion.services.integration.services.IProcesarFichero;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


/**
 * Created by amartinez on 01/03/2018.
 */
@Component
@Log4j2
public  class Main   {
    @EventListener(ContextRefreshedEvent.class)

    public void contextRefreshedEvent() {

        try {
            JobDetail job = JobBuilder.newJob(Cron.class)
                    .withIdentity("procesarFicherosJobName1", "group1").build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("procesarFicherosTriggerName", "group1")
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(50).repeatForever())
                    .build();
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();

            boolean flag = scheduler.checkExists(trigger.getKey());
            if (!flag)
            {
                scheduler.start();
                scheduler.scheduleJob(job, trigger);
            }
            else
            {

                scheduler.start();
                scheduler.scheduleJob(job,  TriggerBuilder
                        .newTrigger()
                        .withIdentity("procesarFicherosTriggerName", "group1")
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(50).repeatForever())
                        .build());
            }


        } catch (SchedulerException se) {
            se.printStackTrace();
        }


//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info("**** Iniciando proceso de lectura ****");
//        ProcesarFichero procesarFichero = new ProcesarFichero();
//        try {
//            //procesarFichero.obtenerCodigoBarra(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR),SystemParameters.getParameter(SystemParameters.DIR_PROCESADAS)) ;
//            procesarFichero.leerDirectorio();
//        } catch (Exception ex) {
//            log.error("*** Error al procesar los ficheros *** ", ex);
//        }




    }


//    public static void main(String[] args)  {
//
//        try {
//            JobDetail job = JobBuilder.newJob(Cron.class)
//                    .withIdentity("procesarFicherosJobName", "group1").build();
//            Trigger trigger = TriggerBuilder
//                    .newTrigger()
//                    .withIdentity("procesarFicherosTriggerName", "group1")
//                    .withSchedule(
//                            SimpleScheduleBuilder.simpleSchedule()
//                                    .withIntervalInSeconds(50).repeatForever())
//                    .build();
//            SchedulerFactory sf = new StdSchedulerFactory();
//            Scheduler scheduler = sf.getScheduler();
//            scheduler.start();
//            scheduler.scheduleJob(job, trigger);
//        } catch (SchedulerException se) {
//            se.printStackTrace();
////        }
//
//    }
}
