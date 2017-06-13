// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
// const host = 'http://192.168.1.81:28080/soaint-sgd-web-api-gateway/apis';
const host = 'http://10.42.0.1:8081/soaint-sgd-web-api-gateway/apis';
export const environment = {
  production: false,
  security_endpoint : `${host}/securidad-gateway-api`,
  product_endpoint : `${host}/productos-gateway-api`,
  tipoDestinatario_endpoint : `${host}/tipo-destinatario-gateway-api`,
  pais_endpoint : `${host}/pais-gateway-api`,
  unidadTiempo_endpoint : `${host}/unidad-tiempo-gateway-api`,
  tipologiaDocumental_endpoint : `${host}/tipologia-documental-gateway-api`,
  tipoTelefono_endpoint : `${host}/tipo-telefono-gateway-api`,
  tipoPersona_endpoint : `${host}/tipo-persona-gateway-api`,
  tipoDocumento_endpoint : `${host}/tipo-documento-gateway-api`,
  tipoComunicacion_endpoint: `${host}/tipo-comunicacion-gateway-api`,
  tipoAnexos_endpoint: `${host}/tipo-anexos-gateway-api`,
  sedeAdministrativa_endpoint: `${host}/sede-administrativa-gateway-api`,
  mediosRecepcion_endpoint: `${host}/medios-recepcion-gateway-api`,
  dependenciaGrupo_endpoint: `${host}/dependencia-grupo-gateway-api`
};
