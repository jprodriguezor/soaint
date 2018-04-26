 import {RadicacionFormInterface} from "./radicacionForm.interface";

 export  interface RadicacionSalidaFormInterface extends RadicacionFormInterface{

   destinatarioInterno:Array<{
     tipoDestinatario: any,
     sedeAdministrativa: any,
     dependenciaGrupo: any,
     destinatarioPrincipal: any
   }>,
   destinatarioExt:Array<{
     tipoDestinatario: any,
     tipoPersona: any,
     Nombre: any,
     TipoDocumento:any,
     destinatarioPrincipal: any,
     datosContactoList:any[],
   }>,
   datosEnvio?:{
     clase_envio:any,
     modalidad_correo: any,
   }

 }
