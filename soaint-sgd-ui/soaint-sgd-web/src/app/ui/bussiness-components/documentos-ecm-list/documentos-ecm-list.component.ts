import {Component, EventEmitter, Input, OnInit, Output,ViewChild, ViewEncapsulation} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getMediosRecepcionArrayData,
  getTipoAnexosArrayData,
  getTipoComunicacionArrayData,
  getTipologiaDocumentalArrayData,
  getUnidadTiempoArrayData,
  getSoporteAnexoArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Dropdown } from "primeng/components/dropdown/dropdown";
import 'rxjs/add/operator/single';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {DatosGeneralesApiService} from '../../../infrastructure/api/datos-generales.api';
import {createSelector} from 'reselect';
import {getUnidadTiempoEntities} from '../../../infrastructure/state-management/constanteDTO-state/selectors/unidad-tiempo-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {MEDIO_RECEPCION_EMPRESA_MENSAJERIA} from '../../../shared/bussiness-properties/radicacion-properties';
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';


@Component({
  selector: 'app-documentos-ecm-list',
  templateUrl: './documentos-ecm-list.component.html',
  //changeDetection: ChangeDetectionStrategy.OnPush
})
export class DocumentosECMListComponent implements OnInit {


  @Input()
  versionar = false;

  @Input()
  comunicacion: ComunicacionOficialDTO;

  documentsList: any;

  uploadUrl: String;


  constructor(private _store: Store<State>, private _api: ApiBase) {
  }

  ngOnInit(): void {
  }



  loadDocumentos() {
    console.log(this.comunicacion);
    const idDocumentECM = this.comunicacion.ppdDocumentoList[0].ideEcm;
    const endpoint = `${environment.digitalizar_doc_upload_endpoint}` + '/obtenerdocumentosasociados/' + idDocumentECM;
    console.log(endpoint);
    this._api.list(endpoint).map(value => {
      console.log(value);
      /*return {
        success: ruleCheck,
        agente: agente
      };*/
    });

    //this._asignacionSandbox.obtenerObservaciones(this.idDocumento).subscribe((results) => {
    //  this.observaciones = [...results.observaciones];
    //  this.countObservaciones.emit(this.observaciones.length);
    //  this._changeDetectorRef.detectChanges();
    //});
  }

  setDataDocument(data: any) {
    this.comunicacion = data.comunicacion;
    this.versionar = data.versionar;
  }

}
