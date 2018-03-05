// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

const host = 'http://192.168.1.81:28080/soaint-sgd-web-api-gateway/apis';
const ecmHost = 'http://192.168.1.81:28080/ecm-integration-services/apis/ecm';

// const host = 'http://192.168.1.181:28080/soaint-sgd-web-api-gateway/apis';
// const ecmHost = 'http://192.168.1.181:28080/ecm-integration-services/apis/ecm';

 // const host = 'http://192.168.3.242:28080/soaint-sgd-web-api-gateway/apis';
 // const ecmHost = 'http://192.168.3.242:28080/ecm-integration-services/apis/ecm';

export const environment = {
  production: false,
  security_endpoint: `${host}/securidad-gateway-api`,
  product_endpoint: `${host}/productos-gateway-api`,
  tipoDestinatario_endpoint: `${host}/tipo-destinatario-gateway-api`,
  pais_endpoint: `${host}/pais-gateway-api`,
  unidadTiempo_endpoint: `${host}/unidad-tiempo-gateway-api`,
  tipologiaDocumental_endpoint: `${host}/tipologia-documental-gateway-api`,
  tipoTelefono_endpoint: `${host}/tipo-telefono-gateway-api`,
  tipoPersona_endpoint: `${host}/tipo-persona-gateway-api`,
  tipoPlantilla_endpoint: `${host}/tipo-plantilla-gateway-api`,
  tipoDocumento_endpoint: `${host}/tipo-documento-gateway-api`,
  tipoComunicacion_endpoint: `${host}/tipo-comunicacion-gateway-api`,
  tipoComunicacionSalida_endpoint: `${host}/tipo-comunicacion-salida-gateway-api`,
  tipoAnexos_endpoint: `${host}/tipo-anexos-gateway-api`,
  soporteAnexo_endpoint: `${host}/soporte-anexos-gateway-api`,
  sedeAdministrativa_endpoint: `${host}/sede-administrativa-gateway-api`,
  mediosRecepcion_endpoint: `${host}/medios-recepcion-gateway-api`,
  dependenciaGrupo_endpoint: `${host}/dependencia-grupo-gateway-api`,
  dependencias_endpoint: `${host}/dependencia-grupo-gateway-api/all-dependencias`,
  tratamientoCortesia_endpoint: `${host}/tratamiento-cortesia-gateway-api`,
  actuaCalidad_endpoint: `${host}/actua-calidad-gateway-api`,
  departamento_endpoint: `${host}/departamento-gateway-api`,
  municipio_endpoint: `${host}/municipio-gateway-api`,
  proceso_endpoint: `${host}/proceso-gateway-api`,
  startProcess_endpoint: `${host}/proceso-gateway-api/iniciar`,
  instancesProcess_endpoint: `${host}/proceso-gateway-api/listar-instancias`,
  tasksInsideProcess_endpoint: `${host}/proceso-gateway-api/listar/estados-instancia`,
  tasksForStatus_endpoint: `${host}/proceso-gateway-api/tareas/listar/estados`,
  tasksStats_endpoint: `${host}/proceso-gateway-api/tareas/listar/usuario`,
  tasksStartProcess: `${host}/proceso-gateway-api/tareas/iniciar/`,
  tasksReserveProcess: `${host}/proceso-gateway-api/tareas/reservar`,
  tasksCompleteProcess: `${host}/proceso-gateway-api/tareas/completar/`,
  tasksAbortProcess: `${host}/proceso-gateway-api/tareas/abortar/`,
  taskStatus_endpoint: `${host}/tarea-gateway-api/tarea`,
  bis_endpoint: `${host}/bis-gateway-api`,
  tipoComplemento_endpoint: `${host}/tipo-complemento-gateway-api`,
  prefijoCuadrante_endpoint: `${host}/prefijo-cuadrante-gateway-api`,
  orientacion_endpoint: `${host}/orientacion-gateway-api`,
  tipoVia_endpoint: `${host}/tipo-via-gateway-api`,
  radicarComunicacion_endpoint: `${host}/correspondencia-gateway-api/radicar`,
  listarCorrespondencia_endpoint: `${host}/correspondencia-gateway-api/listar-comunicaciones`,
  obtenerFuncionario_endpoint: `${host}/funcionario-gateway-api`,
  listarFuncionarios_endpoint: `${host}/funcionario-gateway-api/funcionarios`,
  updateFuncionarios_roles_endpoint: `${host}/funcionario-gateway-api/funcionarios`,
  obtenerFuncionarios_roles_endpoint: `${host}/funcionario-gateway-api/funcionarios/roles`,
  buscarFuncionarios_endpoint: `${host}/funcionario-gateway-api/funcionarios/buscar`,
  redireccionarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/redireccionar`,
  devolverComunicaciones_endpoint: `${host}/correspondencia-gateway-api/devolver`,
  devolverComunicacionesAsigancion_endpoint: `${host}/correspondencia-gateway-api/devolver/asignacion`,
  digitalizar_doc_upload_endpoint: `${host}/digitalizar-documento-gateway-api`,
  metricasTiempoRadicacion_rule_endpoint: `${host}/correspondencia-gateway-api/metricasTiempo`,
  verificarRedirecciones_rule_endpoint: `${host}/correspondencia-gateway-api/verificar-redirecciones`,

  // Carga masiva endpoint
  carga_masiva_endpoint_listar: `${host}/carga-masiva-gateway-api/listadocargamasiva`,
  carga_masiva_endpoint_estado: `${host}/carga-masiva-gateway-api/estadocargamasiva`,
  carga_masiva_endpoint_upload: `${host}/carga-masiva-gateway-api/cargar-fichero`,

  // Asignacion Enpoints
  asignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/asignar`,
  reasignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/reasignar`,
  obtenerObservaciones_endpoint: `${host}/correspondencia-gateway-api/obtenerObservaciones/`,
  obtenerDocumento_endpoint: `${host}/digitalizar-documento-gateway-api/obtener-documento/`,
  obtenerComunicacion_endpoint: `${host}/correspondencia-gateway-api/obtener-comunicacion/`,
  obtenerContactoDestinatarioExterno_endpoint: `${host}/correspondencia-gateway-api/contactos-destinatario-externo`,
  registrarObservaciones_endpoint: `${host}/correspondencia-gateway-api/registrarObservacion`,
  obtenerConstantesPorCodigo_endpoint: `${host}/correspondencia-gateway-api/constantes`,
  obtenerDependenciasPorCodigo_endpoint: `${host}/dependencia-grupo-gateway-api/dependencias`,
  obtenerMunicipiosPorCodigo_endpoint: `${host}/municipio-gateway-api/municipios`,
  obtenerDocumento: `${ecmHost}/descargarDocumentoECM/?identificadorDoc=`,
  obtenerVariablesTarea: `${host}/proceso-gateway-api/tareas/obtener-variables`,
  listarDistrubucion_endpoint: `${host}/correspondencia-gateway-api/listar-distribucion`,
  listarPlanillas_endpoint: `${host}/correspondencia-gateway-api/listar-planillas`,
  exportarPlanilla_endpoint: `${host}/correspondencia-gateway-api/exportar-plantilla/`,
  generarPlanilla_endpoint: `${host}/correspondencia-gateway-api/generar-plantilla`,
  cargarPlanilla_endpoint: `${host}/correspondencia-gateway-api/cargar-plantilla`,

  salvarCorrespondenciaEntrada_endpoint: `${host}/correspondencia-gateway-api/salvar-correspondencia-entrada`,

  actualizarComunicacion_endpoint: `${host}/correspondencia-gateway-api/actualizar-comunicacion`,
  restablecerCorrespondenciaEntrada_endpoint: `${host}/correspondencia-gateway-api/restablecer_correspondencia_entrada`,

    // Produccion Documental
    pd_ejecutar_proyeccion_multiple: `${host}/produccion-documental-gateway-api/ejecutar-proyeccion-multiple`,
    pd_obtenerDatosDocXnroRadicado: `${host}/produccion-documental-gateway-api/datos-documento`,
    pd_obtener_documentos_asociados: `${host}/obtenerdocumentosasociados`,
    pd_gestion_documental : {
        subirAnexo: `${ecmHost}/subirDocumentoRelacionECM`,
        obtenerAnexo: `${ecmHost}/obtenerDocumentosAdjuntosECM`,
        eliminarAnexo: ``,
        subirDocumentoVersionado : `${ecmHost}/subirVersionarDocumentoGeneradoECM`,
        obtenerVersionDocumento : `${ecmHost}/descargarDocumentoVersionECM`,
        obtenerListaVersionesDocumento : `${ecmHost}/obtenerVersionesDocumentos`,
        eliminarVersionDocumento : `${ecmHost}/eliminarDocumentoECM`,
        descargarDocumentoPorId : `${ecmHost}/descargarDocumentoECM`,
      obtenerDocumentoPorId: `${ecmHost}/descargarDocumentoECM`,
    },
  listar_serie_subserie: `${host}/unidad-documental-gateway-api/listado-serie-subserie`,

  //Archivar Documento

  ad_obtener_serie_subserie : `${ecmHost}/devolverSerieOSubserie`
    // http://192.168.1.81:28080/ecm-integration-services/apis/ecm/descargarDocumentoECM/?identificadorDoc=02f2f035-b791-4ec3-b6c0-714dc3dfe95f
};

export const process_info = {
  'proceso.correspondencia-entrada': {
    displayValue: 'Correspondencia de entrada',
    show: true
  },
  'proceso.correspondencia-salida': {
    displayValue: 'Correspondencia de salida',
    show: false
  },
  'proceso.gestion-planillas': {
    displayValue: 'Gestion de planillas',
    show: false
  },
  'proceso.gestor-devoluciones': {
    displayValue: 'Gestor de devoluciones',
    show: false
  },
  'proceso.produccion-documental': {
    displayValue: 'Producción documental',
    show: false
  },
  'proceso.produccion-multiples-documentos': {
    displayValue: 'Producción de multiples documentos',
    show: true
  },
  'proceso.recibir-gestionar-doc': {
    displayValue: 'Recibir y gestionar documento',
    show: false
  },
  'proceso.gestion-unidades-documentales': {
    displayValue: 'Gestión de unidades documentales',
    show: false
  }

};
