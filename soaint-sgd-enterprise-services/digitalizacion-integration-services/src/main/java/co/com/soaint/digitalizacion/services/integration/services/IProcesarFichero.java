package co.com.soaint.digitalizacion.services.integration.services;

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

    void leerDirectorioEvento() throws IOException, FormatException, ChecksumException;

    void leerDirectorio(String filePathOrigen, String filePathDestino) throws IOException, FormatException, ChecksumException;
}
