export const BAD_AUTHENTICATION = 'No se reconoce la combinación de usuario y contraseña';

const DEVOLUCIONES = 'devoluciones';
const DEVOLUCION = 'devolución';
const REDIRECCION = 'redirección';
const REDIRECCIONES = 'redirecciones';

export const SUCCESS_REDIRECTION = `La ${REDIRECCION} fue realizada Correctamente`;
export const SUCCESS_REASIGNACION = `Ha sido reasignado el documento No. `;
export const SUCCESS_ADJUNTAR_DOCUMENTO = `Se ha adjuntado correctamente el documento `;
export const ERROR_ADJUNTAR_DOCUMENTO = `Ha ocurrido un error adjuntando el documento `;
export const SUCCESS_DEVOLUTION = `La ${DEVOLUCION} fue realizada Correctamente`;
export const WARN_DEVOLUTION = `No todas las ${REDIRECCIONES} pudieron ser ejecutadas`;
export const WARN_REDIRECTION = `No todas las ${DEVOLUCIONES} pudieron ser ejecutadas`;
export const FAIL_REDIRECTION = `No se pudieron realizar ${REDIRECCIONES}`;
export const FAIL_DEVOLUTION = `No se pudieron realizar ${DEVOLUCIONES}`;
export const FAIL_ADJUNTAR_PRINCIPAL = `Debe seleccionar un documento como principal`;
export const FAIL_ADJUNTAR_ANEXOS = `El radicado realizado, no tiene anexos electrónicos registrados`;
export const WARN_DESTINATARIOS_REPETIDOS = `Ya existe el destinatario en la lista`;
