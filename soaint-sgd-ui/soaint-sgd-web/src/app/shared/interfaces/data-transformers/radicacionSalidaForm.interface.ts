 import {RadicacionFormInterface} from "./radicacionForm.interface";

 export  interface RadicacionSalidaFormInterface extends RadicacionFormInterface{

   destinatarioInterno:Array<{
     tipoDestinatario: any,
     sedeAdministrativa: any,
     dependenciaGrupo: any,
     destinatarioPrincipal: any,
     funcionario:any
   }>,
   destinatarioExt:Array<{
     tipoDestinatario: any,
     tipoPersona: any,
     nombre: any,
     tipoDocumento:any,
     destinatarioPrincipal: any,
     datosContactoList:any[],
     razonSocial?:any,
     actuaCalidad?:any,
     nit?:any,
     nroDocumentoIdentidad?:any,
   }>,
   datosEnvio?:{
     clase_envio:any,
     modalidad_correo: any,
   }

 }
