package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by amartinez on 10/01/2018.
 */
public  class test {


    public static void main (String [] args) throws SystemException {


       EntradaProcesoDTO entrada = EntradaProcesoDTO.newInstance()
                .idDespliegue("co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT")
                .idProceso("proceso.correspondencia-entrada")
                .usuario("krisv")
                .pass("krisv")
                .idTarea(Long.valueOf(111111))
                .build();

            System.out.print(entrada.getIdDespliegue());



    }
}
