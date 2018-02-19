import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {DestinatarioDTO} from '../../../../../domain/destinatarioDTO';
import {ProduccionDocumentalApiService} from "../../../../../infrastructure/api/produccion-documental.api";
import {AgentDTO} from "../../../../../domain/agentDTO";
import {destinatarioOriginal} from "../../../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";
import {Sandbox as DependenciaSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {isNullOrUndefined} from 'util';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import { getTipoDocumentoArrayData, getTipoPersonaArrayData, } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {getTipoDestinatarioArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import {DESTINATARIO_PRINCIPAL, TIPO_REMITENTE_INTERNO, TIPO_REMITENTE_EXTERNO} from "../../../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html',
  styleUrls: ['datos-contacto.component.css'],
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form:FormGroup;

  subscription:Subscription;

  validations:any = {};

  listaDestinatariosInternos:DestinatarioDTO[] = [];
  listaDestinatariosExternos:DestinatarioDTO[] = [];

  destinatarioInterno:DestinatarioDTO = null;
  destinatarioExterno:DestinatarioDTO = null;

  responderRemitente = false;
  hasNumberRadicado = false;
  editable = true;
  defaultDestinatarioTipoComunicacion = '';
  hasDestinatarioPrincipal = false;
  issetListDestinatarioBacken = false;
  indexSelectExterno:number = -1;
  indexSelectInterno:number = -1;

  destinatarioExternoDialogVisible = false;
  destinatarioInternoDialogVisible = false;

  @ViewChild('datosRemitentesExterno') datosRemitentesExterno;
  @ViewChild('datosRemitentesInterno') datosRemitentesInterno;

  @Input() taskData:TareaDTO;

  constructor(private formBuilder:FormBuilder,
              private _changeDetectorRef:ChangeDetectorRef,
              private _produccionDocumentalApi:ProduccionDocumentalApiService,
              private pdMessageService:PdMessageService,
              private _dependenciaSandbox:DependenciaSandbox,
              private _store:Store<State>) {

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

  ngOnInit():void {

    console.log('Tarea de entrada');
    console.log(this.taskData);

    this._store.dispatch(new SedeAdministrativaLoadAction());

    if (this.taskData.variables.numeroRadicado) {
      this.hasNumberRadicado = true;
    }

    this.form.get('responderRemitente').valueChanges.subscribe(responderRemitente => {
      this.responderRemitente = responderRemitente;

      if (responderRemitente) {

        if (this.taskData.variables.numeroRadicado) {
          this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
            nroRadicado: this.taskData.variables.numeroRadicado
          }).subscribe(agente => {
            console.log('Objeto que viene del backen ', agente);
            if (agente) {
              this.defaultDestinatarioTipoComunicacion = agente.codTipoRemite;
              let tempDestinatario = <DestinatarioDTO> {};
              tempDestinatario.interno = false;
              tempDestinatario.tipoDestinatario = this.seachTipoDestinatario(agente.indOriginal);
              tempDestinatario.tipoPersona = this.searchTipoPersona(agente.codTipoPers);
              tempDestinatario.nombre = (agente.nombre) ? agente.nombre : '';
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
              tempDestinatario.datosContactoList = (agente.datosContactoList) ? agente.datosContactoList : null;
              tempDestinatario.isBacken = true;
              if (agente.codTipoRemite === TIPO_REMITENTE_EXTERNO) {

                tempDestinatario.interno = false;
                this.destinatarioExterno = tempDestinatario;
                this.datosRemitentesExterno.initFormByDestinatario(this.destinatarioExterno);
                this.indexSelectExterno = -1;
                this.destinatarioExternoDialogVisible = true;
              } else if (agente.codTipoRemite === TIPO_REMITENTE_INTERNO) {

                tempDestinatario.interno = true;
                this.destinatarioInterno = tempDestinatario;
                this.datosRemitentesInterno.initFormByDestinatario(this.destinatarioInterno);
                this.indexSelectInterno = -1;
                this.destinatarioInternoDialogVisible = true;
              }
            }
            this.refreshView();
          });
        }

      } else {
        /*if (this.defaultDestinatarioTipoComunicacion === TIPO_REMITENTE_EXTERNO) {
          const index:number = this.listaDestinatariosExternos.indexOf(this.destinatarioExterno);
          if (index !== -1) {
            this.listaDestinatariosExternos.splice(index, 1);
          }
        } else if (this.defaultDestinatarioTipoComunicacion === TIPO_REMITENTE_INTERNO) {
          const index:number = this.listaDestinatariosInternos.indexOf(this.destinatarioInterno);
          if (index !== -1) {
            this.listaDestinatariosInternos.splice(index, 1);
          }
        }*/
      }
    });
  }


  seachTipoDestinatario(indOriginal) {
    let result = null;
    this._store.select(sedeAdministrativaArrayData).subscribe(values => {
      result = values.find((element) => element.codigo === indOriginal)
    });
    return result
  }

  searchTipoPersona(codTipoPers) {
    let result = null;
    this._store.select(getTipoPersonaArrayData).subscribe(values => {
      result = values.find((element) => element.codigo === codTipoPers)
    });
    return result
  }

  searchTipoDocumento(codTipDocIdent) {
    let result = null;
    this._store.select(getTipoDocumentoArrayData).subscribe(values => {
      result = values.find((element) => element.codigo === codTipDocIdent)
    });
    return result
  }

  searchSede(codSede) {
    let result = null;
    this._store.select(sedeAdministrativaArrayData).subscribe(values => {
      result = values.find((element) => element.codigo === codSede)
    });
    return result
  }

  searchDependencia(codDependencia) {
    let result = null;
    this._produccionDocumentalApi.getDependencias({}).subscribe(values => {
      result = values.find((element) => element.codigo === codDependencia)
    });
    return result
  }

  initForm() {
    this.form = this.formBuilder.group({
      // Datos destinatario
      'responderRemitente': [null],
      'distribucion': [null],
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

    console.log(newDestinatario);

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
        }else{
          this.deleteDestinatario(this.indexSelectExterno, TIPO_REMITENTE_EXTERNO);
        }

        const newList1 = this.listaDestinatariosInternos.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);

        const newList = this.listaDestinatariosExternos.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);

        if (newDestinatario.interno) {
          newList1.unshift(newDestinatario);
        }else{
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
    //this.subscription.unsubscribe();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
