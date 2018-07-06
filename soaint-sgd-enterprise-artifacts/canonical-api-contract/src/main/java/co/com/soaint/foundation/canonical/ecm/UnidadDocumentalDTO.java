package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:22-Feb-2018
 * Author: dotero
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreUnidadDocumental")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/unidad-documental/1.0.0")
public class UnidadDocumentalDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String accion;
    private Boolean inactivo;
    private String ubicacionTopografica;
    private Calendar fechaCierre;
    private String id;
    private String faseArchivo; //archivo central, archivo gestion
    private Calendar fechaExtremaInicial;
    private String soporte;
    private String codigoUnidadDocumental;
    private String nombreUnidadDocumental;
    private String descriptor2;
    private String descriptor1;
    private String estado; // aprobado/rechazado
    private String tipoTransferencia; //primaria/secundaria
    private String disposicion; //tipos disposiciones
    private Calendar fechaExtremaFinal;
    private LocalDateTime fechaArchivoRetencion;
    private String fechaAutoCierre;
    private Boolean cerrada;

    //heredadas
    private String codigoSubSerie;
    private String nombreSubSerie;

    private String codigoSerie;
    private String nombreSerie;

    private String codigoDependencia;
    private String nombreDependencia;

    private String codigoSede;
    private String nombreSede;

    private String observaciones;

    //Agregacion
    private List<DocumentoDTO> listaDocumentos;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public UnidadDocumentalDTO(String codigoBase, String nombreBase, String accion, Boolean inactivo,
                               String ubicacionTopografica, Calendar fechaCierre, String id, String faseArchivo,
                               Calendar fechaExtremaInicial, String soporte, String codigoUnidadDocumental,
                               String nombreUnidadDocumental, String descriptor2, String descriptor1,
                               String estado, String disposicion, Calendar fechaExtremaFinal, Boolean cerrada,
                               String codigoSubSerie, String nombreSubSerie, String codigoSerie, String nombreSerie,
                               String codigoDependencia, String nombreDependencia, String codigoSede, String nombreSede,
                               String observaciones, List<DocumentoDTO> listaDocumentos) {
        super(codigoBase, nombreBase);
        this.accion = accion;
        this.inactivo = inactivo;
        this.ubicacionTopografica = ubicacionTopografica;
        this.fechaCierre = fechaCierre;
        this.id = id;
        this.faseArchivo = faseArchivo;
        this.fechaExtremaInicial = fechaExtremaInicial;
        this.soporte = soporte;
        this.codigoUnidadDocumental = codigoUnidadDocumental;
        this.nombreUnidadDocumental = nombreUnidadDocumental;
        this.descriptor2 = descriptor2;
        this.descriptor1 = descriptor1;
        this.estado = estado;
        this.disposicion = disposicion;
        this.fechaExtremaFinal = fechaExtremaFinal;
        this.cerrada = cerrada;
        this.codigoSubSerie = codigoSubSerie;
        this.nombreSubSerie = nombreSubSerie;
        this.codigoSerie = codigoSerie;
        this.nombreSerie = nombreSerie;
        this.codigoDependencia = codigoDependencia;
        this.nombreDependencia = nombreDependencia;
        this.codigoSede = codigoSede;
        this.nombreSede = nombreSede;
        this.observaciones = observaciones;
        this.listaDocumentos = listaDocumentos;
    }
}