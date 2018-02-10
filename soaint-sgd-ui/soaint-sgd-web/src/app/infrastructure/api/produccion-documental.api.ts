import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../domain/constanteDTO';
import {RolDTO} from '../../domain/rolesDTO';
import {SUCCESS_ADJUNTAR_DOCUMENTO} from "../../shared/lang/es";
import {PushNotificationAction} from "../state-management/notifications-state/notifications-actions";

@Injectable()
export class ProduccionDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  guardarEstadoTarea(payload: any) {
    return this._api.post(environment.taskStatus_endpoint, payload).map(response => response);
  }

  obtenerEstadoTarea(payload: {idInstanciaProceso:string, idTareaProceso:string}) {
    return this._api.list(`${environment.taskStatus_endpoint}/${payload.idInstanciaProceso}/${payload.idTareaProceso}`, {}).map(response => response.payload);
  }

  subirVersionDocumento(formData:FormData, payload:{nombre:string,sede:string,dependencia:string,tipo:string,id:string}) {
    return this._api.sendFile(
      environment.pd_gestion_documental.subirDocumentoVersionado, formData,
      [payload.nombre,payload.sede,payload.dependencia,payload.tipo,payload.id,'PD']
    );
  }

  subirAnexo(formData:FormData, payload:{nombre:string,sede:string,dependencia:string}) {
    return this._api.sendFile(
      environment.pd_gestion_documental.subirAnexo, formData,
      [payload.sede, payload.dependencia, payload.nombre]);
  }

  obtenerVersionDocumento(payload:{id:string,version:string}) {
    return this._api.list(environment.pd_gestion_documental.obtenerVersionDocumento,{identificadorDoc:payload.id,version:payload.version});
  }

  eliminarVersionDocumento(payload:{id:string}) {
    return this._api.post(`${environment.pd_gestion_documental.eliminarVersionDocumento}/${payload.id}`,{});
  }



  ejecutarProyeccionMultiple(payload: {}) {
    return this._api.post(environment.pd_ejecutar_proyeccion_multiple, payload).map(response => response);
  }

  getTiposComunicacion(payload: {}) {
    return this._api.list(environment.tipoComunicacion_endpoint, payload).map(res => res.constantes);
  }

  getTiposComunicacionSalida(payload: {}) {
    return this._api.list(environment.tipoComunicacionSalida_endpoint, payload).map(res => res.constantes);
  }

  getFuncionariosPorDependenciaRol(payload) {
    return this._api.list(environment.listarFuncionarios_endpoint + '/' + payload.codDependencia, payload);
  }

  getTiposAnexo(payload: {}) {
    return this._api.list(environment.tipoAnexos_endpoint, payload).map(res => res.constantes);
  }

  getTiposDestinatario(payload: {}) {
    return this._api.list(environment.tipoDestinatario_endpoint, payload).map(res => res.constantes);
  }

  getTiposDocumento(payload: {}) {
    return this._api.list(environment.tipoDocumento_endpoint, payload).map(res => res.constantes);
  }

  getTiposPersona(payload: {}) {
    return this._api.list(environment.tipoPersona_endpoint, payload).map(res => res.constantes);
  }

  getActuaEnCalidad(payload: {}) {
    return this._api.list(environment.actuaCalidad_endpoint, payload).map(res => res.constantes);
  }

  getSedes(payload: {}) {
    return this._api.list(environment.sedeAdministrativa_endpoint, payload).map(res => res.organigrama);
  }

  getDependencias(payload: {}) {
    return this._api.list(environment.dependencias_endpoint, payload).map(res => res.dependencias);
  }

  getTipoPlantilla(payload) : Observable<string> {
      return this._api.list(`${environment.tipoPlantilla_endpoint}/obtener/${payload.codigo}`, payload).map(res => res.text);
  }

  generarPdf(payload) : Observable<{success:boolean,text:string}> {
    return this._api.post(`${environment.tipoPlantilla_endpoint}/generar-pdf`, payload).map(res => res);
  }

  getTiposPlantilla(payload: {}): Observable<ConstanteDTO[]> {
    return Observable.of(JSON.parse(`[
          {"codigo":"TL-DOCOF","nombre":"Oficio","codPadre":"TL-DOC","id":49},
          {"codigo":"TL-DOCM","nombre":"Memorando","codPadre":"TL-DOC","id":61}
        ]`));
  }

  getRoles(payload: {}): Observable<RolDTO[]> {
    return Observable.of(JSON.parse(`[
          {"id":1,"rol":"administrador","nombre":"Administrador"},
          {"id":2,"rol":"proyector","nombre":"Proyector"},
          {"id":3,"rol":"revisor","nombre":"Revisor"},
          {"id":4,"rol":"aprobador","nombre":"Aprobador"}
        ]`));
  }

}
