import {Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {getAuthenticatedFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {Sandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {Subscription} from "rxjs/Subscription";
import {
  getAgragarObservacionesDialogVisible,
  getJustificationDialogVisible,
  getRejectDialogVisible
} from "app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors";

@Component({
  selector: 'app-recibir-gestionar-comunicaciones',
  templateUrl: './recibir-gestionar-comunicaciones.component.html',
  styleUrls: ['./recibir-gestionar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RecibirGestionarComunicacionesComponent implements OnInit, OnDestroy {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  selectedComunications: ComunicacionOficialDTO[] = [];

  justificationDialogVisible$: Observable<boolean>;

  agregarObservacionesDialogVisible$: Observable<boolean>;

  rejectDialogVisible$: Observable<boolean>;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  comunicacionesSubcription: Subscription;

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('popupReject') popupReject;

  constructor(private _store: Store<RootState>,
              private _comunicacionOficialApi: CominicacionOficialSandbox,
              private _asignacionSandbox: AsignacionSandbox,
              private _funcionarioSandbox: Sandbox,
              private formBuilder: FormBuilder) {
    // this.comunicaciones$ = this._store.select(ComunicacionesArrayData);
    this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(ComunicacionesArrayData).subscribe(() => {
      this.selectedComunications = [];
    });
    this.initForm();
  }

  ngOnInit() {
    this.listarComunicaciones();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null],
      'estadoCorrespondencia': [null],
    });
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    let d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  hideJustificationPopup() {
    this._asignacionSandbox.setVisibleJustificationDialogDispatch(false);
  }

  showAddObservationsDialog(idDocuemento: number) {
    this.popupAgregarObservaciones.form.reset();
    this.popupAgregarObservaciones.setData({
      idDocumento: idDocuemento,
      idFuncionario: this.funcionarioLog.id,
    });
    this.popupAgregarObservaciones.loadObservations();
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(true);
  }

  hideAddObservationsPopup() {
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(false);
  }

  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      cod_estado: this.form.get('estadoCorrespondencia').value
    });
  }
}

