import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './cargarPlanillasDTO-actions';
import {ApiBase} from '../../api/api-base';
import { IfObservable } from 'rxjs/observable/IfObservable';
import {Observable } from 'rxjs/Observable';
import { PlanAgenDTO } from '../../../domain/PlanAgenDTO';
import { PlanAgentesDTO } from '../../../domain/PlanAgentesDTO';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {
    return this._api.list(environment.listarPlanillas_endpoint, payload);
  }

  loadPlanillasSalida(payload: any): Observable<PlanAgentesDTO> {
    return Observable.of({
      pagente: [
        {
          idePlanAgen:null,
          estado:null,
          idePlanilla: null,
          varPeso: '3',
          varValor:'4',
          varNumeroGuia:'4',
          fecObservacion: null,
          nroPlanilla: null,
          codNuevaSede:"",
          codNuevaDepen: "",
          fecGeneracion: null,
          codTipoPlanilla: null,
          observaciones:'',
          codCauDevo:null,
          fecCarguePla:null,
          usuario:'',
          ideAgente:null,
          ideDocumento:null,
          nroRadicado: null,
          tipoPersona: null,
          tipologiaDocumental: null,
          nit: 'null',
          nroDocuIdentidad: 'null',
          nombre: 'string',
          razonSocial: 'string',
          folios: 2,
          anexos: 2,
          codFuncGenera: 2,
          codSedeOrigen: '1000',
          codDependenciaOrigen: '10001040',
          codSedeDestino: '1040',
          codDependenciaDestino: '10401040',
          codClaseEnvio: null,
          codModalidadEnvio: null,
          pagentes: {
            pagente: [
              {
                idePlanAgen: null,
                estado: null,
                varPeso: null,
                varValor: null,
                varNumeroGuia: null,
                fecObservacion: null,
                codNuevaSede: null,
                codNuevaDepen: null,
                observaciones: null,
                codCauDevo: null,
                fecCarguePla: null,
                ideAgente: 3170,
                ideDocumento: 1566,
                nroRadicado: null,
                tipoPersona: null,
                tipologiaDocumental: null,
                nit: null,
                nroDocuIdentidad: null,
                nombre: null,
                razonSocial: null,
                folios: null,
                anexos: null
              }
            ]
          },
          ideEcm: null
        }
      ]
    });

  }
  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

