import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from '../../../environments/environment';

@Injectable()
export class ProduccionDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  getTiposComunicacion(payload: {}) {
    return this._api.list(environment.tipoComunicacion_endpoint, payload).map(res => res.constantes);
  }

  getTiposAnexo(payload: {}) {
    return this._api.list(environment.tipoAnexos_endpoint, payload).map(res => res.constantes);
  }

  getTiposDestinatario(payload: {}) {
    return this._api.list(environment.tipoDestinatario_endpoint, payload).map(res => res.constantes);
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

  getFuncionarios(payload: {}) {
    return this._api.list(environment.listarFuncionarios_endpoint, payload).map(res => res.constantes);
  }

}
