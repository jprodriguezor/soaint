// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  security_endpoint: 'http://localhost:8081/soaint-sgd-web-api-gateway/apis/securidad-gateway-api',
  product_endpoint: 'http://localhost:8081/soaint-sgd-web-api-gateway/apis/productos-gateway-api'
};
