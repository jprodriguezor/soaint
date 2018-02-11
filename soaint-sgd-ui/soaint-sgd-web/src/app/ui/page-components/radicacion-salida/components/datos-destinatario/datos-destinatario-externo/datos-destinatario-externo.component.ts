import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms'
import {ConstanteDTO} from '../../../../../../domain/constanteDTO';
import {Observable} from 'rxjs/Observable';
import {PaisDTO} from '../../../../../../domain/paisDTO';
import {DepartamentoDTO} from '../../../../../../domain/departamentoDTO';
import {MunicipioDTO} from '../../../../../../domain/municipioDTO';
import {DestinatarioDTO} from '../../../../../../domain/destinatarioDTO';
import {ConfirmationService} from 'primeng/primeng';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {Sandbox as PaisSandbox} from '../../../../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from '../../../../../../infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from '../../../../../../infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';

import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {ProduccionDocumentalApiService} from '../../../../../../infrastructure/api/produccion-documental.api';
import {DESTINATARIO_PRINCIPAL} from '../../../../../../shared/bussiness-properties/radicacion-properties';
import {PushNotificationAction} from '../../../../../../infrastructure/state-management/notifications-state/notifications-actions';

@Component({
  selector: 'rs-datos-destinatario-externo',
  templateUrl: './datos-destinatario-externo.component.html',
  styleUrls: ['./datos-destinatario-externo.component.css']
})
export class DatosDestinatarioExternoComponent implements OnInit, OnDestroy {

  form: FormGroup;

  @Input('principal') principal: boolean;
  @Output() change: EventEmitter<Boolean> = new EventEmitter<Boolean>();

  tipoPersonaSelected: ConstanteDTO;

  tiposPersona$: Observable<ConstanteDTO[]>;
  tiposDestinatario$: Observable<ConstanteDTO[]>;
  actuanEnCalidad$: Observable<ConstanteDTO[]>;
  tiposDocumento$: Observable<ConstanteDTO[]>;

  paises$: Observable<PaisDTO[]>;
  municipios$: Observable<MunicipioDTO[]>;
  departamentos$: Observable<DepartamentoDTO[]>;

  visibility: any = {};
  validations: any = {};

  @Input() listaDestinatarios: DestinatarioDTO[] = [];
  canInsert = false;

  constructor(private formBuilder: FormBuilder,
              private _store: Store<State>,
              private _paisSandbox: PaisSandbox,
              private confirmationService: ConfirmationService,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService) {

    this.initForm();

    Observable.combineLatest(
      this.form.get('tipoDestinatario').valueChanges,
      this.form.get('tipoPersona').valueChanges,
      this.form.get('nombre').valueChanges,
      this.form.get('tipoDocumento').valueChanges,
      this.form.get('nit').valueChanges,
      this.form.get('actuaCalidad').valueChanges,
      this.form.get('actuaCalidadNombre').valueChanges,
      this.form.get('email').valueChanges,
      this.form.get('phone').valueChanges,
      this.form.get('mobile').valueChanges,
      this.form.get('pais').valueChanges,
      this.form.get('departamento').valueChanges,
      this.form.get('municipio').valueChanges
    ).do(() => this.canInsert = false)
      .filter(([tipoDestinatario, tipoPersona, nombre, tipoDocumento,
                 nit, actuaCalidad, actuaCalidadNombre, email, phone, mobile, pais, departamento, municipio]) =>
        tipoDestinatario && tipoPersona && nombre && tipoDocumento && nit && actuaCalidad && actuaCalidadNombre
        && email && phone && mobile && pais && departamento && municipio)
      .zip(([tipoDestinatario, tipoPersona, nombre, tipoDocumento,
              nit, actuaCalidad, actuaCalidadNombre, email, phone, mobile, pais, departamento, municipio]) => {
        return {
          tipoDestinatario: tipoDestinatario, tipoPersona: tipoPersona, nombre: nombre, tipoDocumento: tipoDocumento,
          nit: nit, actuaCalidad: actuaCalidad, actuaCalidadNombre: actuaCalidadNombre, email: email, phone: phone,
          mobile: mobile, pais: pais, departamento: departamento, municipio: municipio
        }
      })
      .subscribe(() => {
        this.canInsert = true
      });
  }

  checkDestinatarioInList(newone: DestinatarioDTO, lista: DestinatarioDTO[]) {
    return lista.filter(current => current.email === newone.email).length > 0;
  }

  adicionarDestinatario() {
    if (!this.form.valid) {
      return false;
    }

    const destinatarios = this.listaDestinatarios;
    const newone: DestinatarioDTO = this.form.value;
    newone.interno = false;

    if (newone.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL && this.principal) {
      this._store.dispatch(new PushNotificationAction({
        severity: 'success',
        summary: 'Existe un destinatario principal en los destinatarios externos'
      }));
      return false;
    }

    if (this.checkDestinatarioInList(newone, destinatarios)) {
      return false;
    }

    if (newone.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL
      && destinatarios.filter(value => value.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL).length > 0) {
      this.confirmationService.confirm({
        message: `<p style="text-align: center">¿Está seguro desea substituir el destinatario principal?</p>`,
        accept: () => {
          const newList = destinatarios.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);
          newList.unshift(newone);
          this.listaDestinatarios = [...newList];
          this.form.reset();
          this.principal = true;
          this.change.emit(this.principal);
        }
      });
      return true;
    }

    if (newone.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {
      destinatarios.unshift(newone);
    } else {
      destinatarios.push(newone);
    }

    this.listaDestinatarios = [...destinatarios];
    this.form.reset();
    this.principal = true;
    this.change.emit(this.principal);
    return true;
  }

  eliminarDestinatario(index: number) {
    if (index > -1) {
      const destinatarios = this.listaDestinatarios;
      destinatarios.splice(index, 1);
      this.listaDestinatarios = [...destinatarios];
    }
  }

  tipoPersonaChange(event) {
    this.tipoPersonaSelected = event.value;
  }

  initForm() {
    this.form = this.formBuilder.group({
      // Datos destinatario
      'tipoDestinatario': [null, Validators.required],
      'tipoPersona': [{value: null}],
      'nombre': [null],
      'tipoDocumento': [{value: null}],
      'nit': [null],
      'actuaCalidad': [{value: null}],
      'actuaCalidadNombre': [null],

      'email': [null],
      'phone': [null],
      'mobile': [null],
      'pais': [{value: null}],
      'departamento': [{value: null}],
      'municipio': [{value: null}],
    });
  }


  ngOnInit(): void {
    this.tiposPersona$ = this._produccionDocumentalApi.getTiposPersona({});
    this.tiposDestinatario$ = this._produccionDocumentalApi.getTiposDestinatario({});
    this.actuanEnCalidad$ = this._produccionDocumentalApi.getActuaEnCalidad({});
    this.tiposDocumento$ = this._produccionDocumentalApi.getTiposDocumento({});
    this.paises$ = this._store.select(paisArrayData);
    this.municipios$ = this._store.select(municipioArrayData);
    this.departamentos$ = this._store.select(departamentoArrayData);

    this._paisSandbox.loadDispatch();
    this.refreshView();
  }

  onPaisChange($event) {
    const pais = this.form.get('pais').value;
    if (pais) {
      this._departamentoSandbox.loadDispatch({codPais: pais.codigo});
    }
  }

  onDepartamentoChange($event) {
    const departamento = this.form.get('departamento').value;
    if (departamento) {
      this._municipioSandbox.loadDispatch({codDepar: departamento.codigo});
    }
  }

  ngOnDestroy(): void {
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}
