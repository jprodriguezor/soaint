import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Observable} from 'rxjs/Observable';
import {ProduccionDocumentalApiService} from '../../../infrastructure/api/produccion-documental.api';
import {PaisDTO} from '../../../domain/paisDTO';
import {DepartamentoDTO} from '../../../domain/departamentoDTO';
import {MunicipioDTO} from '../../../domain/municipioDTO';
import {AgentDTO} from '../../../domain/agentDTO';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as PaisSandbox} from '../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from '../../../infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from '../../../infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';

import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {DESTINATARIO_PRINCIPAL} from '../../../shared/bussiness-properties/radicacion-properties';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {ConfirmationService} from 'primeng/primeng';
import {StatusDTO} from "../produccion-documental/models/StatusDTO";
import {Sandbox as AsiganacionDTOSandbox} from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';



import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';


import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA,COMUNICACION_INTERNA_ENVIADA,COMUNICACION_EXTERNA_ENVIADA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL, TPDOC_CEDULA_CIUDADANIA, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';

import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {any} from "codelyzer/util/function";

@Component({
  selector: 'datos-destinatario-externo',
  templateUrl: 'datos-destinatario-externo.component.html'
})
export class DatosDestinatarioExternoComponent implements OnInit, OnDestroy {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};
  display = false;

  // Observables
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestions$: Observable<ConstanteDTO[]>;
  actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;

  // Listas de subscripcion
  contacts: Array<any> = [];
  dependenciasGrupoList: Array<any> = [];

  subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];

  subscribers: Array<Subscription> = [];

  tipoComunicacion: any;

  contact: Array<any> = [];
  pais: Array<any> = [];
  departamento: Array<any> = [];
  municipio: Array<any> = [];

  //@ViewChild('datosDireccion') datosDireccion;

  @Input() editable = true;
  //@Input() test = any;
  @Input() responseToRem = false;
  @Input() objTipoComunicacion: any;
  @Input() destinatario: AgentDTO;

  contactsDefault: Array<any> = [];

  @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _changeDetectorRef: ChangeDetectorRef
            ) {
  }

  ngOnInit(): void {
    this.initForm();
    this.form.disable();
    this.listenForChanges();
    this.listenForErrors();
    this.visibility['tipoPersona'] = true;
    console.log(this.destinatario);
    if(this.destinatario){

      this.setTipoComunicacion(this.objTipoComunicacion);
      this.onSelectTipoPersona(this.destinatario.codTipoPers);

      if(this.destinatario.datosContactoList.length > 0){

        this.destinatario.datosContactoList.forEach(departamentoArrayData => {

          this.contact = [];
          this.pais = [];
          this.municipio = [];
          this.departamento = [];

          this.contact['tipoVia'] = departamentoArrayData.codTipoVia;
          this.contact['noViaPrincipal'] = departamentoArrayData.nroViaGeneradora;
          this.contact['prefijoCuadrante'] = departamentoArrayData.codPrefijoCuadrant;
          this.contact['bis'] = "";
          this.contact['orientacion'] = "";
          this.contact['noVia'] = departamentoArrayData.nroViaGeneradora;
          this.contact['prefijoCuadrante_se'] = departamentoArrayData.codPrefijoCuadrant;
          this.contact['placa'] = departamentoArrayData.nroPlaca;
          this.contact['orientacion_se'] = "";
          this.contact['complementoTipo'] = "";
          this.contact['complementoAdicional'] = "";
          this.contact['celular'] = departamentoArrayData.celular;
          this.contact['numeroTel'] = departamentoArrayData.telFijo;
          this.contact['correoEle'] = departamentoArrayData.corrElectronico;

          this.contact['pais'] =  null;
          this.paisSuggestions$.subscribe((values) => {
            this.contact['pais'] = values.find(value => value.codigo == departamentoArrayData.codPais);
          });
          //this._departamentoSandbox.loadDispatch({codPais: departamentoArrayData.codPais});

          this.contact['departamento'] = departamentoArrayData.codDepartamento;
          //this.departamentoSuggestions$.subscribe((values) => {
          //  this.contact['departamento'] = values.find(value => value.codigo == departamentoArrayData.codDepartamento);
          //});

          //this._municipioSandbox.loadDispatch({codDepar: departamentoArrayData.codDepartamento});
          this.contact['municipio'] = departamentoArrayData.codMunicipio;
          //this.municipioSuggestions$.subscribe((values) => {
          //  this.contact['municipio'] = values.find(value => value.codigo == departamentoArrayData.codMunicipio);
          //});

          this.contact['principal'] = departamentoArrayData.principal;
          this.contact['provinciaEstado'] = "";
          this.contact['direccionText'] = departamentoArrayData.direccion;
          this.contact['ciudad'] = "";

          console.log("objeto departamentoArrayData");
          console.log(departamentoArrayData);

          this.contactsDefault.push(this.contact);

        });

      }
    }
    this.refreshView();
  }

  handleCargarNuevosContactos(contacts){
    this.destinatario.datosContactoList = contacts;
  }

  ngAfterViewInit() {
     this._store.dispatch(new LoadDatosRemitenteAction());
  }


  //findPais(code: string){
  //  let pais;
  //
  //  this.paisSuggestions$.take(1).subscribe((values) => {
  //    console.log(values);
  //    pais = values.find(value => value.codigo === code);
  //  });
  //
  //  console.log('pais');
  //  console.log(pais);
  //
  //  return pais;
  //}

  //getPais(codigoPais): string {
  //  const pais = this.municipiosList.find((municipio) => municipio.departamento.pais.codigo === codigoPais);
  //  if (pais) {
  //    return pais.departamento.pais.nombre;
  //  }
  //  return '';
  //}

  initLoadTipoComunicacionExterna() {
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);

    this.paisSuggestions$ = this._store.select(paisArrayData);
    this.municipioSuggestions$ = this._store.select(municipioArrayData);
    this.departamentoSuggestions$ = this._store.select(departamentoArrayData);
  }

  initLoadTipoComunicacionInterna() {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombreApellidos': [{value: null, disabled: !this.editable}, Validators.required],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}, Validators.required],
    });
  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sedeAdministrativa').valueChanges.distinctUntilChanged().subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependenciaGrupo').reset();
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
          this.dependenciasGrupoList = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    }));

    /*this.subscribers.push(this.form.get('tipoPersona').valueChanges.distinctUntilChanged().subscribe(value => {
      if (value !== null) {
        this.onSelectTipoPersona(value);
      }
    }));*/
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
    this.bindToValidationErrorsOf('tipoPersona');
  }

  onSelectTipoPersona(value) {

    // const value = event.value;
    if (!this.visibility.tipoPersona) {
      return;
    } else {
      this.visibility = {
        tipoPersona: true
      };
    }

    if (value === PERSONA_ANONIMA) {
      this.visibility['tipoPersona'] = true;

    } else if (value === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA_ENVIADA) {
      console.log("es juridica y externo");
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
    } else if (value === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA_ENVIADA) {
      console.log("es natural y externo");
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

    console.log("visibilidad");
    console.log(this.visibility);
  }

  setTipoComunicacion(value) {

    if (value) {
      //this.visibility = {};
      this.tipoComunicacion = value.codigo;
      if (this.tipoComunicacion === COMUNICACION_INTERNA_ENVIADA) {
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

  ngOnDestroy() {
    this.subscribers.forEach(subscriber => {
      subscriber.unsubscribe();
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}
