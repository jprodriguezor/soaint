
import {
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  AfterViewInit,
  OnInit,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
  ViewChildren, QueryList
} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getPrefijoCuadranteArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/prefijo-cuadrante-selectors';
import {getTipoViaArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-via-selectors';
import {getOrientacionArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/orientacion-selectors';
import {getBisArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/bis-selectors';
import {getTipoComplementoArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-complemento-selectors';
import {tassign} from 'tassign';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as PaisSandbox} from '../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from '../../../infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from '../../../infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import {PaisDTO} from '../../../domain/paisDTO';
import {DepartamentoDTO} from '../../../domain/departamentoDTO';
import {MunicipioDTO} from '../../../domain/municipioDTO';
import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import "rxjs/add/operator/filter";
import {AutoComplete} from "primeng/components/autocomplete/autocomplete";
import {isNullOrUndefined} from 'util';
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {Dropdown} from "primeng/primeng";
import {
  DATOS_CONTACTO_PRINCIPAL,
  DATOS_CONTACTO_SECUNDARIO
} from "../../../shared/bussiness-properties/radicacion-properties";
import { LocalizacionApiService } from '../../../infrastructure/api/localizacion.api';
enum FormContextEnum {
  SAVE,
  CREATE
}

@Component({
  selector: 'app-datos-direccion',
  templateUrl: './datos-direccion.component.html',
})
export class DatosDireccionComponent implements OnInit, OnDestroy, AfterViewInit  {

  form: FormGroup;
  display = false;
  @Input() editable = true;

  @Input() contactsDefault: Array<any> = [];

  @Output()
  nuevosContactos = new EventEmitter();

  @Output() loadComponent:EventEmitter<DatosDireccionComponent> = new EventEmitter;

  @ViewChild('paisAutoComplete') paisAutoComplete: AutoComplete;
  @ViewChild('departamentoAutoComplete') departamentoAutoComplete: AutoComplete;
  @ViewChild('municipioAutoComplete') municipioAutoComplete: AutoComplete;
  @ViewChildren(Dropdown) dropdownsChilds: QueryList<Dropdown>;



  validations: any = {};
  visibility: any = {};

  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;
  prefijoCuadranteSuggestions$: Observable<ConstanteDTO[]>;
  tipoViaSuggestions$: Observable<ConstanteDTO[]>;
  orientacionSuggestions$: Observable<ConstanteDTO[]>;
  bisSuggestons$: Observable<ConstanteDTO[]>;
  tipoComplementoSuggestions$: Observable<ConstanteDTO[]>;
  paises$: Observable<PaisDTO[]>;
  departamentos$: Observable<DepartamentoDTO[]>;
  municipios$: Observable<MunicipioDTO[]>;

  contacts: Array<any> = [];
  paises: Array<any> = [];
  contactPrincial = false;
  showDireccionForm = false;
  showCheckDireccionForm = false;
  showContactForm = false;
  formContext: FormContextEnum;
  editIndexContext: number;

  subscribers: Array<Subscription> = [];


  constructor(private _store: Store<State>,
              private _paisSandbox: PaisSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _localizacionService: LocalizacionApiService
            ) {

    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }


  ngOnInit(): void {
    this._paisSandbox.loadDispatch();
    this.prefijoCuadranteSuggestions$ = this._store.select(getPrefijoCuadranteArrayData);
    this.tipoViaSuggestions$ = this._store.select(getTipoViaArrayData);
    this.orientacionSuggestions$ = this._store.select(getOrientacionArrayData);
    this.bisSuggestons$ = this._store.select(getBisArrayData);
    this.tipoComplementoSuggestions$ = this._store.select(getTipoComplementoArrayData);
    this.municipios$ = this._store.select(municipioArrayData);
    this.departamentos$ = this._store.select(departamentoArrayData);
    this.paises$ = this._store.select(paisArrayData);

    this.contacts = this.contactsDefault;

    this.addColombiaByDefault();

    this.paisSuggestions$ = this.paisAutoComplete.completeMethod
      .combineLatest(this.paises$, (event: any, paises) => paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.departamentoSuggestions$ = this.departamentoAutoComplete.completeMethod
      .combineLatest(this.departamentos$, (event: any, departamentos) => departamentos.filter(departamento => departamento.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.municipioSuggestions$ = this.municipioAutoComplete.completeMethod
      .combineLatest(this.municipios$, (event: any, municipios) => municipios.filter(municipio => municipio.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.loadComponent.emit(this);

    // para el caso de lista con datos incompletos
    if (this.contactsDefault && this.contactsDefault.length > 0) {

      console.log("contact default",this.contactsDefault);

      this.CompletarDatosContacto();
    }

  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoVia': [null],
      'noViaPrincipal': [null],
      'prefijoCuadrante': [null],
      'bis': [null],
      'orientacion': [null],
      'noVia': [null],
      'prefijoCuadrante_se': [null],
      'placa': [null],
      'orientacion_se': [null],
      'complementoTipo': [null],
      'complementoAdicional': [null],
      'celular': [null],
      'numeroTel': [null],
      'correoEle': [null],
      'pais': [null],
      'departamento': [null],
      'municipio': [null],
      'principal': [null],
      'provinciaEstado': [null],
      'direccionText': [null],
      'ciudad': [null],
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoVia');
    this.bindToValidationErrorsOf('noViaPrincipal');
    this.bindToValidationErrorsOf('noVia');
    this.bindToValidationErrorsOf('placa');
    this.bindToValidationErrorsOf('correoEle');
  }

  listenForChanges() {
    const paisControl = this.form.get('pais');
    const departamentoControl = this.form.get('departamento');
    const municipioControl = this.form.get('municipio');

    this.subscribers.push(paisControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        departamentoControl.enable();
      } else {
        departamentoControl.reset();
        departamentoControl.disable();
      }
    }));

    paisControl.valueChanges.subscribe(value => {
      this.visibility.selectedColombia = true;
      if (value && value.codigo &&(value.codigo.toUpperCase() !== 'CO')) {
        this.visibility.selectedColombia = false;
        this.showCheckDireccionForm = false;
        departamentoControl.reset();
        departamentoControl.disable();
        municipioControl.reset();
        municipioControl.disable();
      } else {
        this.showCheckDireccionForm = true;
      }

    });

    this.subscribers.push(departamentoControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        municipioControl.enable();
      } else {
        municipioControl.reset();
        municipioControl.disable();
      }
    }));

  }


  onDropdownClickPais(event?) {
    this._paisSandbox.loadDispatch();
  }

  onDropdownClickDepartamento($event) {
    const pais = this.form.get('pais').value;
    if (pais) {
      this._departamentoSandbox.loadDispatch({codPais: pais.codigo});
      this.form.get('municipio').reset();
    }
  }

  onDropdownClickMunicipio($event) {
    const departamento = this.form.get('departamento').value;
    if (departamento) {
      this._municipioSandbox.loadDispatch({codDepar: departamento.codigo});
    }
  }

  // listenForBlurEvents(control: string) {
  //   const ac = this.form.get(control);
  //   if (ac.touched && ac.invalid) {
  //     const error_keys = Object.keys(ac.errors);
  //     const last_error_key = error_keys[error_keys.length - 1];
  //     this.validations[control] = VALIDATION_MESSAGES[last_error_key];
  //   }
  // }

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

  saveAndRetriveContact(): any {

    const pais = this.form.get('pais');
    const departamento = this.form.get('departamento');
    const municipio = this.form.get('municipio');
    const numeroTel = this.form.get('numeroTel');
    const celular = this.form.get('celular');
    const email = this.form.get('correoEle');
    const provinciaEstado = this.form.get('provinciaEstado');
    const ciudad = this.form.get('ciudad');
    const direccionText = this.form.get('direccionText');
    const principal = this.form.get('principal');
    const toSave = tassign({
      pais: pais.value,
      departamento: departamento.value,
      municipio: municipio.value,
      numeroTel: numeroTel.value,
      celular: celular.value,
      correoEle: email.value,
      provinciaEstado: provinciaEstado.value,
      ciudad: ciudad.value,
      direccionText: direccionText.value,
      principal: (principal.value === true ? true : false)
    }, this.saveDireccionData());

    pais.reset();
    provinciaEstado.reset();
    ciudad.reset();
    direccionText.reset();
    departamento.reset();
    municipio.reset();
    numeroTel.reset();
    celular.reset();
    email.reset();
    principal.reset();

    this.showContactForm = false;
    this.showDireccionForm = false;

    return toSave;

  }

  saveDireccionData() {
    let direccion = '';
    const tipoVia = this.form.get('tipoVia');
    const noViaPrincipal = this.form.get('noViaPrincipal');
    const prefijoCuadrante = this.form.get('prefijoCuadrante');
    const bis = this.form.get('bis');
    const orientacion = this.form.get('orientacion');
    const noVia = this.form.get('noVia');
    const prefijoCuadrante_se = this.form.get('prefijoCuadrante_se');
    const placa = this.form.get('placa');
    const orientacion_se = this.form.get('orientacion_se');
    const tipoComplemento = this.form.get('complementoTipo');
    const complementoAdicional = this.form.get('complementoAdicional');

    const pais = this.form.get('pais');
    const direccionText = this.form.get('direccionText');

    const value = {};

    if (pais.value && pais.value.codigo.toUpperCase() === 'CO') {

      if (tipoVia.value) {
        direccion += tipoVia.value.nombre;
        value['tipoVia'] = tipoVia.value;

        this.addToDireccionAdicional(value,"tipoVia",{codigo:tipoVia.value.codigo});

        tipoVia.reset();
      }
      if (noViaPrincipal.value) {
        direccion += ' ' + noViaPrincipal.value;
        value['noViaPrincipal'] = noViaPrincipal.value;

        this.addToDireccionAdicional(value,"noViaPrincipal",noViaPrincipal.value);

        noViaPrincipal.reset();
      }
      if (prefijoCuadrante.value) {
        direccion += ' ' + prefijoCuadrante.value.nombre;
        value['prefijoCuadrante'] = prefijoCuadrante.value;
        this.addToDireccionAdicional(value,"prefijoCuadrante",{codigo:prefijoCuadrante.value.codigo});

        prefijoCuadrante.reset();
      }
      if (bis.value) {
        direccion += ' ' + bis.value.nombre;
        value['bis'] = bis.value;
        this.addToDireccionAdicional(value,"bis",{codigo:bis.value.codigo});

        bis.reset();
      }
      if (orientacion.value) {
        direccion += ' ' + orientacion.value.nombre;
        value['orientacion'] = orientacion.value;

        this.addToDireccionAdicional(value,"orientacion",{codigo:orientacion.value.codigo});
        orientacion.reset();
      }
      if (noVia.value) {
        direccion += ' ' + noVia.value;
        value['noVia'] = noVia.value;
        this.addToDireccionAdicional(value,"noVia",noVia.value);

        noVia.reset();
      }
      if (prefijoCuadrante_se.value) {
        direccion += ' ' + prefijoCuadrante_se.value.nombre;
        value['prefijoCuadrante_se'] = prefijoCuadrante_se.value;

        this.addToDireccionAdicional(value,"prefijoCuadrante_se",{codigo:prefijoCuadrante_se.value.codigo});

        prefijoCuadrante_se.reset();
      }
      if (placa.value) {
        direccion += ' ' + placa.value;
        value['placa'] = placa.value;

        this.addToDireccionAdicional(value,"placa",placa.value);

        placa.reset();
      }
      if (orientacion_se.value) {
        direccion += ' ' + orientacion_se.value.nombre;
        value['orientacion_se'] = orientacion_se.value;

        this.addToDireccionAdicional(value,"orientacion_se",{codigo:orientacion_se.value.codigo});

        orientacion_se.reset();
      }
      if (tipoComplemento.value) {
        direccion += ' ' + tipoComplemento.value.nombre;
        value['complementoTipo'] = tipoComplemento.value;

        this.addToDireccionAdicional(value,"tipoComplemento",{codigo:tipoComplemento.value.codigo});

        tipoComplemento.reset();
      }
      if (complementoAdicional.value) {
        direccion += ' ' + complementoAdicional.value;
        value['complementoAdicional'] = complementoAdicional.value;

        this.addToDireccionAdicional(value,"complementoAdicional",tipoComplemento.value);

        complementoAdicional.reset();
      }

      value['direccion'] = direccion === '' ? null : direccion;

    } else {
      value['direccion'] = direccionText.value;
    }
    return value;
  }

  private addToDireccionAdicional(target,key:string,value){

    if(target.direccionAdicional === undefined)
      target.direccionAdicional = {};

    target.direccionAdicional[key] = value;

  }

  deleteContact(index) {
    const radref = [...this.contacts];
    radref.splice(index, 1);
    this.contacts = radref;
    this.nuevosContactos.emit(this.contacts);
  }

  editContact(index) {
    const values = this.contacts[index];
    this.form.patchValue(values);
    this.showContactForm = true;
    this.showDireccionForm = !!values['tipoVia'];
    this.formContext = FormContextEnum.SAVE;
    this.editIndexContext = index;
  }

  addContact() {
    this.showContactForm = true;
    this.formContext = FormContextEnum.CREATE;
    this.addColombiaByDefault();
  }

  hasDireccionPrincipal(index: number): boolean {
    let result = false;
    if(this.contacts.length > 0){
      this.contacts.forEach(values => {
        if (values.principal === true && this.contacts.indexOf(values) !== index) {
          result = true;
        }
      });
    }
    return result;
  }
  onFilterPais(event) {

    //this.paisSuggestions$ = this.paisAutoComplete.completeMethod
    //  .combineLatest(this.paises$, (event: any, paises) => paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));


    //this.paisSuggestions$.map(paises => {
    //  paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 );
    //})
    /*this.paisSuggestions$.take(2).subscribe((values) => {
      console.log(values);
    })
    console.log("Lista ", this.paisSuggestions$);
    console.log(event);*/
  }

  onFilterDepartamento(event){

  }

  onFilterMinicipio(event){

  }

  save() {
    if (this.form.valid) {

      const principal = this.form.get('principal');
      if(principal.value === true && this.hasDireccionPrincipal() === true ){

        this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: 'Recuerde que únicamente puede existir una dirección principal'
        }));

      }else {
        if (this.formContext === FormContextEnum.CREATE) {
          this.contacts = [this.saveAndRetriveContact(), ...this.contacts];
        } else {
          const temp = [...this.contacts];
          temp[this.editIndexContext] = this.saveAndRetriveContact();
          this.contacts = temp;
        }
        this.formContext = null;
        this.editIndexContext = null;

        this.nuevosContactos.emit(this.contacts);
      }
    }
  }

  toggleDireccionVisibility() {
    this.showDireccionForm = !this.showDireccionForm;
  }

  ngOnDestroy() {
    this.subscribers.forEach(subscriber => {
      subscriber.unsubscribe();
    });
  }

  ngAfterViewInit() {
    this.refreshView();
  }

  addColombiaByDefault() {
    const subscription = this.paises$

     .filter(values => values.length > 0)

     .subscribe((values) => {
       this.paises = values;
       this.form.get('pais').setValue(values.find(value => value.codigo === 'CO'));

       setTimeout(() => subscription.unsubscribe());
     });

    this._paisSandbox.loadDispatch();

    this.visibility.selectedColombia = true;
  }


  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  toggleDireccionForm(checked:boolean){

    if(!checked || this.formContext != FormContextEnum.SAVE)
      return ;


    let direccionData = this.contacts[this.editIndexContext].direccionAdicional;



    if(isNullOrUndefined(direccionData))
      return;

    Object.keys(direccionData).forEach( key => {

      if(isNullOrUndefined(direccionData[key]))
        return;

      let control = this.form.get(key);

      if(key == "principal"){

        control.setValue( direccionData[key] ? DATOS_CONTACTO_PRINCIPAL : DATOS_CONTACTO_SECUNDARIO);
        return;

      }

      if(!isNullOrUndefined(direccionData[key].codigo)){

        const options = this.dropdownsChilds
          .find(d => d.inputId == key)
          .options;

        const valtoSelect = options.map( option => option.value).find( val =>  val.codigo == direccionData[key].codigo);


        control.setValue(valtoSelect);

        return;
      }

      control.setValue(direccionData[key]);

    });


  }

  CompletarDatosContacto() {



 this._localizacionService.ListarMunicipiosActivos({})

    .subscribe((result: any) => {

       this.contacts = this.contacts
      .reduce((_listado, _contact) => {
        if (result) {
          if(!isNullOrUndefined(_contact.municipio)) {
            const municipio = result.find(_item => _item.codigo === _contact.municipio.codigo);
            if (municipio) {
              const departamento = municipio.departamento;
              const pais = departamento.pais;
              if (!isNullOrUndefined(_contact.municipio) )
                _contact.municipio.nombre = _contact.municipio.nombre ?  _contact.municipio.nombre : (municipio) ? municipio.nombre : '';
              if (!isNullOrUndefined(_contact.departamento) )
                _contact.departamento.nombre =_contact.departamento.nombre ? _contact.departamento.nombre : (departamento) ? departamento.nombre : '';
              if (!isNullOrUndefined(_contact.pais) )
                _contact.pais.nombre =  _contact.pais.nombre ? _contact.pais.nombre : (pais) ? pais.nombre : '';
              _listado.push(_contact);
            }
          }
          return _listado;
        } else {
          return this.contacts;
        }
      }, []);
      this._changeDetectorRef.detectChanges();
    });
  }
}
