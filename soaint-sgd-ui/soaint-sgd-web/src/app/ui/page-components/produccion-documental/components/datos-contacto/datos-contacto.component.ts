import {
  ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output,
  ViewChild
} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {DestinatarioDTO} from '../../../../../domain/destinatarioDTO';
import {ProduccionDocumentalApiService} from '../../../../../infrastructure/api/produccion-documental.api';
import {Sandbox as DependenciaSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';

import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {LoadAction as LoadMunicipioAction} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-actions';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {LoadAction as LoadPaisAction} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-actions';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {LoadAction as LoadDepartamentoAction} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-actions';

import {Sandbox as DepartamentoSandbox} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';

import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {isNullOrUndefined} from 'util';
import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {
  DESTINATARIO_PRINCIPAL,
  TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../../../../../shared/bussiness-properties/radicacion-properties';
import {Observable} from "rxjs/Observable";
import {ViewFilterHook} from "../../../../../shared/ViewHooksHelper";

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html',
  styleUrls: ['datos-contacto.component.css'],
})

export class PDDatosContactoComponent implements OnInit, OnDestroy,OnChanges {
  form: FormGroup;

  subscription: Subscription;

  @Output() onSelectDistribucionElectronica:EventEmitter<boolean> = new EventEmitter;

  validations: any = {};

  listaDestinatariosInternos: DestinatarioDTO[] = [];
  listaDestinatariosExternos: DestinatarioDTO[] = [];

  destinatarioInterno: DestinatarioDTO = null;
  destinatarioExterno: DestinatarioDTO = null;

  hasNumberRadicado = false;
  editable = true;
  defaultDestinatarioTipoComunicacion = '';
  hasDestinatarioPrincipal = false;
  responderRemitente = false;
  issetListDestinatarioBacken = false;
  indexSelectExterno: -1;
  indexSelectInterno: -1;

  destinatarioExternoDialogVisible = false;
  destinatarioInternoDialogVisible = false;

  @ViewChild('datosRemitentesExterno') datosRemitentesExterno;
  @ViewChild('datosRemitentesInterno') datosRemitentesInterno;

  @Input() taskData: TareaDTO;

 private showFormFilter:string;



  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService,
              private _dependenciaSandbox: DependenciaSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _store: Store<State>) {

    /*this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {

     this.tipoComunicacionSelected = tipoComunicacion;
     this.responseToRem = false;
     this.form.get('responderRemitente').setValue(false);

     if(this.taskData.variables.numeroRadicado){

     this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
     nroRadicado: this.taskData.variables.numeroRadicado
     }).subscribe( contacto => {
     console.log(contacto);

     this.hasNumberRadicado = false;

     if(contacto.codTipoRemite == "EXT" && this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SE'){
     this.hasNumberRadicado = true;
     }else if(contacto.codTipoRemite == "INT" && this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SI'){
     this.hasNumberRadicado = true;
     }

     if(this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SE'){
     this.remitenteExterno = contacto;
     this.datosDestinatarioExterno.getDestinatarioDefault(this.remitenteExterno);
     }else if(this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SI'){
     //this.listaDestinatariosInternos.push(contacto);
     console.log("entro por contacto " + contacto );

     this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
     console.log(results.dependencias.find((element) => element.codigo === contacto.codDependencia));
     console.log(results.dependencias.find((element) => element.codSede === contacto.codSede));
     this.defaultDestinatarioInterno = {
     sede: results.dependencias.find((element) => element.codigo === contacto.codDependencia),
     depedencia: results.dependencias.find((element) => element.codSede === contacto.codSede)
     }
     });

     this.defaultDestinatarioInterno = contacto;
     }

     });
     }
     });*/

    this.initForm();
  }

  dispatchSelectDistElectronica(selected:boolean){

    this.onSelectDistribucionElectronica.emit(selected);
  }

  ngOnInit(): void {

    this._store.dispatch(new SedeAdministrativaLoadAction());
    this._store.dispatch(new LoadPaisAction());
    if (this.taskData.variables.numeroRadicado) {
      this.hasNumberRadicado = true;
    }

    if(!this.showForm())
      this.form = null;

  }

  ngOnChanges(){

    if(this.taskData){

      this.showFormFilter = this.taskData.nombre+'-datos-contactos-show-form';

      let newControllers:any = ViewFilterHook.applyFilter(this.taskData.nombre+'-dataContact',{});

      Object.keys(newControllers).forEach(key => {

      if(this.form.get(key) === null){

        this.form.addControl(key, new FormControl(newControllers[key][0], newControllers[key].length > 1 ? newControllers[key][1]: null,newControllers[key].length > 2 ? newControllers[key][2] : null));
        }
      });
    }

  }


  updateStatus(currentStatus: StatusDTO) {
    this.listaDestinatariosExternos = [...currentStatus.datosContacto.listaDestinatariosExternos];
    this.listaDestinatariosInternos = [...currentStatus.datosContacto.listaDestinatariosInternos];
    this.hasDestinatarioPrincipal = currentStatus.datosContacto.hasDestinatarioPrincipal;
    this.responderRemitente = currentStatus.datosContacto.responderRemitente;
    this.form.get('distribucion').setValue(currentStatus.datosContacto.distribucion);
    this.issetListDestinatarioBacken = currentStatus.datosContacto.issetListDestinatarioBackend;
    this.refreshView();
  }

  onChangeResponderRemitente(value) {
    if (value) {
      if (this.taskData.variables.numeroRadicado) {
        this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
          nroRadicado: this.taskData.variables.numeroRadicado
        }).subscribe(agente => {
          console.log('Objeto que viene del backen ', agente);
          if (agente) {
            this.defaultDestinatarioTipoComunicacion = agente.codTipoRemite;
            const tempDestinatario = <DestinatarioDTO> {};
            tempDestinatario.interno = false;
            tempDestinatario.tipoDestinatario = this.seachTipoDestinatario(agente.indOriginal);
            tempDestinatario.tipoPersona = this.searchTipoPersona(agente.codTipoPers);
            tempDestinatario.nombre = (agente.nombre) ? agente.nombre : '';
            tempDestinatario.nroDocumentoIdentidad = agente.nroDocuIdentidad;
            tempDestinatario.tipoDocumento = this.searchTipoDocumento(agente.codTipDocIdent);
            tempDestinatario.nit = (agente.nit) ? agente.nit : '';
            tempDestinatario.actuaCalidad = (agente.codEnCalidad) ? agente.codEnCalidad : null;
            tempDestinatario.actuaCalidadNombre = (agente.codEnCalidad) ? agente.codEnCalidad : '';
            tempDestinatario.sede = this.searchSede(agente.codSede);
            tempDestinatario.dependencia = this.searchDependencia(agente.codDependencia) ? agente.codDependencia : null;
            tempDestinatario.funcionario = null;
            tempDestinatario.email = '';
            tempDestinatario.mobile = '';
            tempDestinatario.phone = '';
            tempDestinatario.pais = null;
            tempDestinatario.departamento = null;
            tempDestinatario.municipio = null;
            tempDestinatario.isBacken = true;
            const contactos = [];




            this.transformToDestinatarioContacts(agente.datosContactoList || [])
              .subscribe(contact => {  console.log("Enter here");
                contactos.push(contact);
              }, null, () => {
                tempDestinatario.datosContactoList = contactos;
                if (agente.codTipoRemite === TIPO_REMITENTE_EXTERNO) {
                  tempDestinatario.interno = false;
                  this.destinatarioExterno = tempDestinatario;
                  this.datosRemitentesExterno.initFormByDestinatario(this.destinatarioExterno);
                  this.indexSelectExterno = -1;
                  this.destinatarioExternoDialogVisible = true;
                  console.log(this.destinatarioExternoDialogVisible);
                }
                else if (agente.codTipoRemite === TIPO_REMITENTE_INTERNO) {
                  tempDestinatario.interno = true;
                  this.destinatarioInterno = tempDestinatario;
                  this.datosRemitentesInterno.initFormByDestinatario(this.destinatarioInterno);
                  this.indexSelectInterno = -1;
                  this.destinatarioInternoDialogVisible = true;
                }
              });


          }
        });
      }
      this.form.get('responderRemitente').setValue(true);
    }
  }



  transformToDestinatarioContacts(contacts): Observable<any[]> {

    //console.log(contacts);

    return  Observable.create(obs => {
      contacts.forEach(contact => obs.next(contact));
      obs.complete();
    })
      .flatMap(contact => {

        let obs = Observable.combineLatest(
          Observable.of(contact),
          this.searchPais(contact.codPais).take(1),
          this.searchDepartamento(contact.codDepartamento).take(1),
          this.searchMunicipio(contact.codMunicipio).take(1),
          (contact, pais, dpto, mncpio) =>{

            console.log("contact",contact);

            return {
              pais: pais,
              departamento: dpto,
              municipio: mncpio,
              numeroTel: isNullOrUndefined(contact.telFijo) ? '' : contact.telFijo,
              celular: isNullOrUndefined(contact.celular) ? '' : contact.celular,
              correoEle: isNullOrUndefined(contact.corrElectronico) ? '' : contact.corrElectronico,
              direccion: isNullOrUndefined(contact.direccion) ? '' : contact.direccion,
              principal:contact.principal
            };
          }
        );

        return obs;
      });

  }

  seachTipoDestinatario(indOriginal) {
    let result = null;
    if (!isNullOrUndefined(indOriginal)) {
      this._store.select(sedeAdministrativaArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === indOriginal)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchTipoPersona(codTipoPers) {
    let result = null;
    if (!isNullOrUndefined(codTipoPers)) {
      this._store.select(getTipoPersonaArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codTipoPers)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchTipoDocumento(codTipDocIdent) {
    let result = null;
    if (!isNullOrUndefined(codTipDocIdent)) {
      this._store.select(getTipoDocumentoArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codTipDocIdent)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchSede(codSede) {
    let result = null;
    if (!isNullOrUndefined(codSede)) {
      this._store.select(sedeAdministrativaArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codSede)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchDependencia(codDependencia) {
    let result = null;
    if (!isNullOrUndefined(codDependencia)) {
      this._produccionDocumentalApi.getDependencias({}).subscribe(values => {
        result = values.find((element) => element.codigo === codDependencia)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchPais(codPais) {
    if (isNullOrUndefined(codPais)) return Observable.of(null);
    this._departamentoSandbox.loadDispatch({codPais: codPais});
    return this._store.select(paisArrayData)
      .map(values => values.find((element) => element.codigo === codPais));
  }

  searchDepartamento(codDepto) {
    if (isNullOrUndefined(codDepto)) return Observable.of(null);
    this._municipioSandbox.loadDispatch({codDepar: codDepto});
    return this._store.select(departamentoArrayData).map(values =>
      values.find((element) => element.codigo === codDepto)
    );
  }

  searchMunicipio(codMunicipio) {
    if (isNullOrUndefined(codMunicipio)) return Observable.of(null)
    return this._store.select(municipioArrayData).map(values =>
      values.find((element) => element.codigo === codMunicipio));
  }

  initForm() {
      this.form = this.formBuilder.group({
        // Datos destinatario
        'responderRemitente': [{value: false, disabled: this.issetListDestinatarioBacken}],
        'distribucion': ['electronica'],
      });

  }

  showAddDestinatarioExternoPopup() {
    this.datosRemitentesExterno.reset();
    this.indexSelectExterno = -1;
    this.destinatarioExternoDialogVisible = true;
  }

  showAddDestinatarioInternoPopup() {
    this.datosRemitentesInterno.reset();
    this.indexSelectInterno = -1;
    this.destinatarioInternoDialogVisible = true;
  }

  hideAddDestinatarioExternoPopup() {
    this.destinatarioExternoDialogVisible = false;
  }

  hideAddDestinatarioInternoPopup() {
    this.destinatarioInternoDialogVisible = false;
  }

  addDestinatario(newDestinatario) {

    console.log("nuevo destinatario",newDestinatario);

    if (newDestinatario) {

      if (!this.hasDestinatarioPrincipal) {
        this.hasDestinatarioPrincipal = (newDestinatario.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) ? true : false;
      }

      if (!this.issetListDestinatarioBacken) {
        this.issetListDestinatarioBacken = (newDestinatario.isBacken) ? true : false;
      }

      if (newDestinatario.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {

        if (newDestinatario.interno) {
          this.deleteDestinatario(this.indexSelectInterno, TIPO_REMITENTE_INTERNO);
        } else {
          this.deleteDestinatario(this.indexSelectExterno, TIPO_REMITENTE_EXTERNO);
        }

        const newList1 = this.listaDestinatariosInternos.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);

        const newList = this.listaDestinatariosExternos.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);

        if (newDestinatario.interno) {
          newList1.unshift(newDestinatario);
        } else {
          newList.unshift(newDestinatario);
        }
        this.listaDestinatariosInternos = [...newList1];
        this.listaDestinatariosExternos = [...newList];

      } else {

        if (newDestinatario.interno) {
          this.deleteDestinatario(this.indexSelectInterno, TIPO_REMITENTE_INTERNO);
          this.listaDestinatariosInternos = [newDestinatario, ...this.listaDestinatariosInternos];
          this.destinatarioInternoDialogVisible = !(this.indexSelectInterno > -1);

        } else {
          this.deleteDestinatario(this.indexSelectExterno, TIPO_REMITENTE_EXTERNO);
          this.listaDestinatariosExternos = [newDestinatario, ...this.listaDestinatariosExternos];
          this.destinatarioExternoDialogVisible = !(this.indexSelectExterno > -1);
        }
      }
    }

    this.hideAddDestinatarioInternoPopup();
    this.hideAddDestinatarioExternoPopup();
  }

  editDestinatario(index, op) {

    if (index > -1 && op === TIPO_REMITENTE_EXTERNO) {
      this.indexSelectExterno = index;
      this.datosRemitentesExterno.initFormByDestinatario(this.listaDestinatariosExternos[index]);
      this.destinatarioExternoDialogVisible = true;
    } else if (index > -1 && op === TIPO_REMITENTE_INTERNO) {
      this.indexSelectInterno = index;
      this.datosRemitentesInterno.initFormByDestinatario(this.listaDestinatariosInternos[index]);
      this.destinatarioInternoDialogVisible = true;
    }

  }

  deleteDestinatario(index, op) {

    if (index > -1 && op === TIPO_REMITENTE_EXTERNO) {
      this.hasDestinatarioPrincipal = (this.listaDestinatariosExternos[index].tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) ? false : true;
      this.issetListDestinatarioBacken = (this.listaDestinatariosExternos[index].isBacken) ? false : true;
      const radref = [...this.listaDestinatariosExternos];
      radref.splice(index, 1);
      this.listaDestinatariosExternos = radref;

    } else if (index > -1 && op === TIPO_REMITENTE_INTERNO) {

      this.hasDestinatarioPrincipal = (this.listaDestinatariosInternos[index].tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) ? false : true;
      this.issetListDestinatarioBacken = (this.listaDestinatariosInternos[index].isBacken) ? false : true;
      const radref = [...this.listaDestinatariosInternos];
      radref.splice(index, 1);
      this.listaDestinatariosInternos = radref;
    }
  }

  ngOnDestroy() {
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  showForm(): boolean{

    return ViewFilterHook.applyFilter(this.showFormFilter,true);
  }


}
