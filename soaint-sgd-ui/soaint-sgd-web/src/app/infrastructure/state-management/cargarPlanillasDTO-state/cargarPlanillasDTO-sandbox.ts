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

  loadPlanillasSalida(payload: any): Observable<any> {
    return Observable.of({
      idePlanilla: 102,
      nroPlanilla: "10402018000006",
      fecGeneracion: 1527865801780,
      codTipoPlanilla: null,
      codFuncGenera: "2",
      codSedeOrigen: "1040",
      codDependenciaOrigen: "10401040",
      codSedeDestino: "1040",
      codDependenciaDestino: "10401040",
      codClaseEnvio: null,
      codModalidadEnvio: null,
      ideEcm: null,
      pagentes: {
          pagente: [
              {
                  idePlanAgen: 123,
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
                  agente: {
                      ideAgente: 3170,
                      codTipoRemite: "INT",
                      codTipoPers: null,
                      nombre: null,
                      razonSocial: null,
                      nit: null,
                      codCortesia: null,
                      codEnCalidad: null,
                      ideFunci: 2,
                      codTipDocIdent: null,
                      nroDocuIdentidad: null,
                      codSede: "1040",
                      codDependencia: "10401040",
                      codEstado: "SA",
                      fecAsignacion: null,
                      codTipAgent: "TP-AGEI",
                      indOriginal: "TP-DESP",
                      numRedirecciones: null,
                      numDevoluciones: null,
                      datosContactoList: []
                  },
                  correspondencia: {
                      ideDocumento: 1566,
                      descripcion: "ergerg",
                      tiempoRespuesta: "10",
                      codUnidadTiempo: "UNID-TID",
                      codMedioRecepcion: "ME-RECVN",
                      fecRadicado: 1527528817584,
                      nroRadicado: "1000EE2018000214",
                      fecDocumento: null,
                      codTipoDoc: null,
                      codTipoCmc: "EE",
                      ideInstancia: "175231",
                      reqDistFisica: "1",
                      codFuncRadica: "2",
                      codSede: "1000",
                      codDependencia: "10001040",
                      reqDigita: "0",
                      codEmpMsj: null,
                      nroGuia: null,
                      fecVenGestion: 1528923637584,
                      codEstado: "AS",
                      inicioConteo: null,
                      codClaseEnvio: null,
                      codModalidadEnvio: null
                  },
                  tipoPersona: {
                      codigo: "TP-PERA",
                      nombre: "Anónimo",
                      codPadre: "TP-PER",
                      id: 37
                  },
                  nit: null,
                  nroDocuIdentidad: null,
                  nombre: null,
                  razonSocial: null,
                  tipologiaDocumental: {
                      codigo: "TL-DOCOF",
                      nombre: "Oficio",
                      codPadre: "TL-DOC",
                      id: 49
                  },
                  folios: 3445345,
                  anexos: 1
              }
          ]
      }
  });

  }
  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

