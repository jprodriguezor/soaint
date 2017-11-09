import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../domain/constanteDTO';
import {RolDTO} from '../../domain/rolesDTO';

@Injectable()
export class ProduccionDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  ejecutarProyeccionMultiple(payload: {}) {
    return this._api.post(environment.pd_ejecutar_proyeccion_multiple, payload).map(response => response);
  }

  getTiposComunicacion(payload: {}) {
    return this._api.list(environment.tipoComunicacion_endpoint, payload).map(res => res.constantes);
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

  getTiposPlantilla(payload: {}): Observable<ConstanteDTO[]> {
    return Observable.of(JSON.parse(`[
          {"codigo":"TL-DOCOF","nombre":"Oficio","codPadre":"TL-DOC","id":49},
          {"codigo":"TL-DOCA","nombre":"Acta","codPadre":"TL-DOC","id":59},
          {"codigo":"TL-DOCC","nombre":"Circular","codPadre":"TL-DOC","id":60},
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
