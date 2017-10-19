import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../domain/constanteDTO';

@Injectable()
export class ProduccionDocumentalApiService {

    constructor(private _api: ApiBase) {
    }

    getTiposComunicacion(payload: {}) {
      return this._api.list(environment.tipoComunicacion_endpoint, payload).map(res => res.constantes);
    }

    getFuncionariosPorDependenciaRol(dependencia: string, payload: {}) {
        return this._api.list(environment.listarFuncionarios_endpoint + '/' + dependencia, payload).map(res => res.funcionarios);
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

    getTiposPlantilla(payload: {}): ConstanteDTO[] {
        const tiposPlantilla: ConstanteDTO[] = JSON.parse(`[
          {"codigo":"TL-DOCOF","nombre":"Oficio","codPadre":"TL-DOC","id":49},
          {"codigo":"TL-DOCA","nombre":"Acta","codPadre":"TL-DOC","id":59},
          {"codigo":"TL-DOCC","nombre":"Circular","codPadre":"TL-DOC","id":60},
          {"codigo":"TL-DOCM","nombre":"Memorando","codPadre":"TL-DOC","id":61}
        ]`);

        return tiposPlantilla;
    }

}
