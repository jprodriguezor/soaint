import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild, Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL, TPDOC_CEDULA_CIUDADANIA, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';
import {Subscription} from 'rxjs/Subscription';
import { ConstanteApiService } from '../../../infrastructure/api/constantes.api';

@Injectable()
export class DatosRemitenteStateService {

    form: FormGroup;
    validations: any = {};
    visibility: any = {};
    display = false;


    // Observables
    tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
    tipoDocumentoSuggestions$: Observable<ConstanteDTO[]>;
    actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
    sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;

    // Listas de subscripcion
    contacts: Array<any> = [];
    dependenciasGrupoList: Array<any> = [];
    subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];
    subscribers: Array<Subscription> = [];

    disabled = true;
    dataform = null;
    tipoComunicacion: any;

    constructor(private _store: Store<State>,
                private formBuilder: FormBuilder,
                private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
                private constanteService: ConstanteApiService) {
    }

    Init(): void {
      this.initForm();
      this.setTipoComunicacion();
      this.listenForChanges();
      this.listenForErrors();
    }

    initLoadTipoComunicacionExterna(): void {
      this.tipoPersonaSuggestions$ = this.constanteService.Listar({key: 'tipoPersona'});
      this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
      this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
    }

    initLoadTipoComunicacionInterna(): void {
      this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    }

    initForm(): void {
        this.form = this.formBuilder.group({
            'tipoPersona': [{value: this.dataform.tipoPersona.codigo, disabled: !this.disabled}, Validators.required],
            'nit': [{value: this.dataform.nit, disabled: !this.disabled}],
            'actuaCalidad': [{value: this.dataform.actuaCalidad.codigo, disabled: !this.disabled}],
            'tipoDocumento': [{value: this.dataform.tipoDocumento.codigo, disabled: !this.disabled}],
            'razonSocial': [{value: this.dataform.razonSocial, disabled: !this.disabled}, Validators.required],
            'nombreApellidos': [{value: this.dataform.nombreApellidos, disabled: !this.disabled}, Validators.required],
            'nroDocumentoIdentidad': [{value: this.dataform.nroDocumentoIdentidad, disabled: !this.disabled}],
            'sedeAdministrativa': [{value: this.dataform.sedeAdministrativa, disabled: !this.disabled}, Validators.required],
            'dependenciaGrupo': [{value: this.dataform.dependenciaGrupo, disabled: !this.disabled}, Validators.required],
        });
    }

    listenForChanges() {
      this.subscribers.push(this.form.get('sedeAdministrativa').valueChanges.distinctUntilChanged().subscribe((sede) => {
        if (this.disabled && sede) {
          this.form.get('dependenciaGrupo').reset();
          const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
            this.dependenciasGrupoList = dependencias.organigrama;
            depedenciaSubscription.unsubscribe();
          });
        }
      }));

      this.subscribers.push(this.form.get('tipoPersona').valueChanges.distinctUntilChanged().subscribe(value => {
        if (value !== null) {
          this.onSelectTipoPersona(value);
        }
      }));
    }

    listenForErrors() {
      this.bindToValidationErrorsOf('sedeAdministrativa');
      this.bindToValidationErrorsOf('dependenciaGrupo');
      this.bindToValidationErrorsOf('tipoPersona');
    }

    onSelectTipoPersona(value) {
      if (!this.visibility.tipoPersona) {
        return;
      } else {
        this.visibility = {
          tipoPersona: true
        };
      }

      if (value === PERSONA_ANONIMA) {
        this.visibility['tipoPersona'] = true;

      } else if (value === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['nit'] = true;
        this.visibility['actuaCalidad'] = true;
        this.visibility['razonSocial'] = true;
        this.visibility['nombreApellidos'] = true;
        this.visibility['datosContacto'] = true;
        this.visibility['inactivo'] = true;
        this.visibility['tipoDocumento'] = true;
        this.tipoDocumentoSuggestions$.subscribe(docs => {
          this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
          this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0]);
        }).unsubscribe();
        this.visibility['personaJuridica'] = true;
      } else if (value === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['nombreApellidos'] = true;
        this.visibility['departamento'] = true;
        this.visibility['nroDocumentoIdentidad'] = true;
        this.visibility['datosContacto'] = true;
        this.visibility['tipoDocumento'] = true;

        this.tipoDocumentoSuggestions$.subscribe(docs => {
          this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
          this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0]);
        }).unsubscribe();

      }
    }

    setTipoComunicacion() {
      if (this.tipoComunicacion) {
        this.visibility = {};
        if (this.tipoComunicacion === COMUNICACION_INTERNA) {
          this.visibility['sedeAdministrativa'] = true;
          this.visibility['dependenciaGrupo'] = true;
          this.initLoadTipoComunicacionInterna();
        } else {
          this.visibility['tipoPersona'] = true;
          this.initLoadTipoComunicacionExterna();
        }
      }
    }

    listenForBlurEvents(control: string) {
      const ac = this.form.get(control);
      if (ac.touched && ac.invalid) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      }
    }

    bindToValidationErrorsOf(control: string) {
      const ac = this.form.get(control);
      ac.valueChanges.subscribe(value => {
        if ((ac.touched || ac.dirty) && ac.errors) {
          const error_keys = Object.keys(ac.errors);
          const last_error_key = error_keys[error_keys.length - 1];
          this.validations[control] = VALIDATION_MESSAGES[last_error_key];
        } else {
          delete this.validations[control];
        }
      });
    }


}
