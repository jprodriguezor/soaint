// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
const host = 'http://localhost:8081';
export const environment = {
  production: false,
  security_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/securidad-gateway-api`,
  product_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/productos-gateway-api`,
  tipo_comunicacion_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/tipo-comunicacion-gateway-api`,
  medio_recepcion_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/medio-recepcion-gateway-api`,
  tipologia_documental_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/tipologia-documental-gateway-api`,
  unidad_tiempo_endpoint: `${host}/soaint-sgd-web-api-gateway/apis/unidad-tiempo-gateway-api`
};
