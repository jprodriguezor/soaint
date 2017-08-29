// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
const host = 'http://192.168.1.81:28080/soaint-sgd-web-api-gateway/apis';
const ecmHost = 'http://192.168.1.81:28080/ecm-integration-services/apis/ecm';
// const host = 'http://192.168.99.100:8080/soaint-sgd-web-api-gateway/apis';
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
  tipoDocumento_endpoint: `${host}/tipo-documento-gateway-api`,
  tipoComunicacion_endpoint: `${host}/tipo-comunicacion-gateway-api`,
  tipoAnexos_endpoint: `${host}/tipo-anexos-gateway-api`,
  sedeAdministrativa_endpoint: `${host}/sede-administrativa-gateway-api`,
  mediosRecepcion_endpoint: `${host}/medios-recepcion-gateway-api`,
  dependenciaGrupo_endpoint: `${host}/dependencia-grupo-gateway-api`,
  tratamientoCortesia_endpoint: `${host}/tratamiento-cortesia-gateway-api`,
  actuaCalidad_endpoint: `${host}/actua-calidad-gateway-api`,
  departamento_endpoint: `${host}/departamento-gateway-api`,
  municipio_endpoint: `${host}/municipio-gateway-api`,
  proceso_endpoint: `${host}/proceso-gateway-api`,
  startProcess_endpoint: `${host}/proceso-gateway-api/iniciar`,
  instancesProcess_endpoint: `${host}/proceso-gateway-api/listar-instancias`,
  tasksInsideProcess_endpoint: `${host}/proceso-gateway-api/listar/estados-instancia`,
  tasksForStatus_endpoint: `${host}/proceso-gateway-api/tareas/listar/estados`,
  tasksStartProcess: `${host}/proceso-gateway-api/tareas/iniciar/`,
  tasksCompleteProcess: `${host}/proceso-gateway-api/tareas/completar/`,
  bis_endpoint: `${host}/bis-gateway-api`,
  tipoComplemento_endpoint: `${host}/tipo-complemento-gateway-api`,
  prefijoCuadrante_endpoint: `${host}/prefijo-cuadrante-gateway-api`,
  orientacion_endpoint: `${host}/orientacion-gateway-api`,
  tipoVia_endpoint: `${host}/tipo-via-gateway-api`,
  radicarComunicacion_endpoint: `${host}/correspondencia-gateway-api/radicar`,
  listarCorrespondencia_endpoint: `${host}/correspondencia-gateway-api/listar-comunicaciones`,
  obtenerFuncionario_endpoint: `${host}/funcionario-gateway-api`,
  listarFuncionarios_endpoint: `${host}/funcionario-gateway-api/funcionarios`,
  redireccionarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/redireccionar`,
  digitalizar_doc_upload_endpoint: `${host}/digitalizar-documento-gateway-api`,
  metricasTiempoRadicacion_rule_endpoint: `${host}/correspondencia-gateway-api/metricasTiempo`,

  // Asignacion Enpoints
  asignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/asignar`,
  reasignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/reasignar`,
  obtenerObservaciones_endpoint: `${host}/correspondencia-gateway-api/obtenerObservaciones/`,
  obtenerDocumento_endpoint: `${host}/digitalizar-documento-gateway-api/obtener-documento/`,
  obtenerComunicacion_endpoint: `${host}/correspondencia-gateway-api/obtener-comunicacion/`,
  registrarObservaciones_endpoint: `${host}/correspondencia-gateway-api/registrarObservacion`,
  obtenerConstantesPorCodigo_endpoint: `${host}/correspondencia-gateway-api/constantes`,
  obtenerDependenciasPorCodigo_endpoint: `${host}/dependencia-grupo-gateway-api/dependencias`,
  obtenerDocumento: `${ecmHost}/descargarDocumentoECM/?identificadorDoc=`,
  obtenerVariablesTarea: `${host}/proceso-gateway-api/tareas/obtener-variables`,
};
