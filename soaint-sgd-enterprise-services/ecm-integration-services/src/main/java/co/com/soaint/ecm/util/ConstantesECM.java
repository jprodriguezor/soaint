package co.com.soaint.ecm.util;

public final class ConstantesECM {

    public static final String ERROR_COD_MENSAJE = "1224";

    // Class Properties ECM
    public static final String CLASE_BASE = "claseBase";
    public static final String CLASE_SEDE = "claseSede";
    public static final String CLASE_DEPENDENCIA = "claseDependencia";
    public static final String CLASE_SERIE = "claseSerie";
    public static final String CLASE_SUBSERIE = "claseSubserie";
    public static final String CLASE_UNIDAD_DOCUMENTAL = "claseUnidadDocumental";
    public static final String CMCOR = "cmcor:";

    //******************
    //* properties ECM *
    //******************

    // CM_Unidad_Administrativa
    public static final String CMCOR_DEP_CODIGO = CMCOR + "CodigoDependencia";
    public static final String CMCOR_DEP_CODIGO_UAP = CMCOR + "CodigoUnidadAdminPadre";

    //CM_Unidad_Documental
    public static final String CMCOR_UD_ACCION = CMCOR + "accion";
    public static final String CMCOR_UD_FECHA_INICIAL = CMCOR + "fechaInicial";
    public static final String CMCOR_UD_INACTIVO = CMCOR + "inactivo";
    public static final String CMCOR_UD_UBICACION_TOPOGRAFICA = CMCOR + "ubicacionTopografica";
    public static final String CMCOR_UD_FECHA_FINAL = CMCOR + "fechaFinal";
    public static final String CMCOR_UD_FECHA_CIERRE = CMCOR + "fechaCierre";
    public static final String CMCOR_UD_ID = CMCOR + "id";
    public static final String CMCOR_UD_FASE_ARCHIVO = CMCOR + "faseArchivo";
    public static final String CMCOR_UD_SOPORTE = CMCOR + "soporte";
    public static final String CMCOR_UD_CODIGO = CMCOR + "codigoUnidadDocumental";
    public static final String CMCOR_UD_DESCRIPTOR_2 = CMCOR + "descriptor2";
    public static final String CMCOR_UD_DESCRIPTOR_1 = CMCOR + "descriptor1";
    public static final String CMCOR_UD_CERRADA = CMCOR + "cerrada";
    public static final String CMCOR_UD_OBSERVACIONES = CMCOR + "observaciones";
    public static final String CMCOR_UD_DISPOSICION = CMCOR + "disposicion";
    public static final String CMCOR_UD_ESTADO = CMCOR + "estado";

    //CM_Unidad_Base
    public static final String CMCOR_UB_CODIGO = CMCOR + "CodigoBase";

    //CM_Serie
    public static final String CMCOR_SER_CODIGO = CMCOR + "CodigoSerie";

    //CM_SubSerie
    public static final String CMCOR_SS_CODIGO = CMCOR + "CodigoSubserie";

    //CM_DocumentoPersonalizado
    public static final String CMCOR_NRO_RADICADO = CMCOR + "NroRadicado";
    public static final String CMCOR_NOMBRE_REMITENTE = CMCOR + "NombreRemitente";
    public static final String CMCOR_TIPOLOGIA_DOCUMENTAL = CMCOR + "TipologiaDocumental";
    public static final String CMCOR_TIPO_DOCUMENTO = CMCOR + "xTipo";
    public static final String CMCOR_NUMERO_REFERIDO = CMCOR + "xNumeroReferido";
    public static final String CMCOR_ID_DOC_PRINCIPAL = CMCOR + "xIdentificadorDocPrincipal";

    // ECM sms Error
    public static final String ECM_ERROR = "ECM_ERROR";
    public static final String ECM_ERROR_DUPLICADO = "ECM ERROR DUPLICADO";
    public static final String EXISTE_CARPETA = "Existe la Carpeta: ";

    // ECM sms
    public static final String COMUNICACIONES_INTERNAS_RECIBIDAS = "Comunicaciones Oficiales Internas Recibidas ";
    public static final String COMUNICACIONES_INTERNAS_ENVIADAS = "Comunicaciones Oficiales Internas Enviadas ";
    public static final String COMUNICACIONES_EXTERNAS_RECIBIDAS = "Comunicaciones Oficiales Externas Recibidas ";
    public static final String COMUNICACIONES_EXTERNAS_ENVIADAS = "Comunicaciones Oficiales Externas Enviadas ";

    public static final String TIPO_COMUNICACION_INTERNA = "0231.02311_Comunicaciones Oficiales Internas";
    public static final String TIPO_COMUNICACION_EXTERNA = "0231.02312_Comunicaciones Oficiales Externas";

    public static final String DOCUMENTOS_POR_ARCHIVAR = "DOCUMENTOS POR ARCHIVAR";

    public static final String ERROR_TIPO_EXCEPTION = "### Error tipo Exception----------------------------- :";
    public static final String ERROR_TIPO_IO = "### Error tipo IO----------------------------- :";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String DOCUMENTO = "documento";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String PRODUCCION_DOCUMENTAL = "PRODUCCION DOCUMENTAL ";
    public static final String DOCUMENTOS_APOYO = "DOCUMENTOS DE APOYO ";
    public static final String AVISO_CREA_DOC = "### Se va a crear el documento..";
    public static final String AVISO_CREA_DOC_ID = "### Documento creado con id ";
    public static final String NO_EXISTE_DEPENDENCIA = "En la estructura no existe la Dependencia: ";
    public static final String NO_EXISTE_SEDE = "En la estructura no existe la sede: ";
    public static final String SEPARADOR = "---";
    public static final String SUCCESS_COD_MENSAJE = "0000";
    public static final String OPERACION_COMPLETADA_SATISFACTORIAMENTE = "Operacion completada satisfactoriamente";
    public static final String SUCCESS = "SUCCESS";
    public static final String NO_RESULT_MATCH = "Ningun resultado coincide con el criterio de busqueda";
    public static final String RMC_X_IDENTIFICADOR = "rmc:xIdentificador";

    private ConstantesECM(){}
}
