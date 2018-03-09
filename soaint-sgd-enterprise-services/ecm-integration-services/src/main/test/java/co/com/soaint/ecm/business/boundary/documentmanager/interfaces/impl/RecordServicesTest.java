package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config1.xml"})
public class RecordServicesTest {

    @Autowired
    RecordServices recordServices;

    @Test
    public void crearEstructuraRecord() throws Exception {
        System.out.println(recordServices);
    }

    @Test
    public void crearCarpetaRecord() throws Exception {
        EntradaRecordDTO entrada=new EntradaRecordDTO();
        entrada.setDependencia("100100");
        entrada.setNombreCarpeta("Test1");
        entrada.setSerie("10000");
        entrada.setSubSerie("345");
        recordServices.crearCarpetaRecord(entrada);
    }

    @Test
    public void declararRecord() throws Exception {
    }

    @Test
    public void completeRecord() throws Exception {
    }

    @Test
    public void abrirCerrarRecordFolder() throws Exception {
    }

}