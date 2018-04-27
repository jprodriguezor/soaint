package co.com.soaint.digitalizacion.services.integration.services;

import co.com.soaint.foundation.canonical.digitalizar.MensajeGenericoDigitalizarDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;

import java.io.File;
import java.io.IOException;

/**
 * Created by amartinez on 01/03/2018.
 */

public interface IProcesarFichero {

    String getImgText(String imagen);

    String obtenerCodigoBarra(File fileEntry) throws IOException, FormatException, ChecksumException;

    void leerDirectorioEvento(String origen, String destino) throws IOException, FormatException, ChecksumException, SystemException;

    MensajeGenericoDigitalizarDTO leerDirectorio(MensajeGenericoDigitalizarDTO entradaDigitalizar) throws IOException, FormatException, ChecksumException, SystemException;
}
