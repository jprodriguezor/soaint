import {Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation, OnDestroy, ChangeDetectorRef} from '@angular/core';
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
import { Dropdown } from 'primeng/components/dropdown/dropdown';
import 'rxjs/add/operator/single';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {DatosGeneralesApiService} from '../../../infrastructure/api/datos-generales.api';
import {createSelector} from 'reselect';
import {getUnidadTiempoEntities} from '../../../infrastructure/state-management/constanteDTO-state/selectors/unidad-tiempo-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {MEDIO_RECEPCION_EMPRESA_MENSAJERIA} from '../../../shared/bussiness-properties/radicacion-properties';
import { DatosGeneralesStateService } from './datos-generales-state-service';


@Component({
  selector: 'app-datos-generales-edit',
  templateUrl: './datos-generales-edit.component.html',
  styles: [`
    .ui-datalist-header, .ui-datatable-header {
      text-align: left !important;
    }
  `]
})
export class DatosGeneralesEditComponent implements OnInit {

  @Input()
  state: DatosGeneralesStateService = null;

  constructor(
   ) {

  }

  ngOnInit(): void {

  }

}

