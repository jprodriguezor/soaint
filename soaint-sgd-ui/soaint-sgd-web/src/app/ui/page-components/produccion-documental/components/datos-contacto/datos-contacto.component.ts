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

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html',
  styleUrls: ['datos-contacto.component.css'],
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;

  tipoComunicacionSelected = 'EE';

  subscription: Subscription;

  validations: any = {};

  listaDestinatariosInternos: DestinatarioDTO[] = [];
  listaDestinatariosExternos: DestinatarioDTO[] = [];

  destinatarioInterno: DestinatarioDTO = null;
  destinatarioExterno: DestinatarioDTO = null;

  @ViewChild('datosRemitentesExterno') datosRemitentesExterno;
  @ViewChild('datosRemitentesInterno') datosRemitentesInterno;

  @Input() taskData: TareaDTO;

  canInsert = false;
  responderRemitente = false;
  hasNumberRadicado = false;
  editable = true;
  defaultDestinatarioTipoComunicacion = "";

  valueRemitente: any;

  destinatarioExternoDialogVisible = false;
  destinatarioInternoDialogVisible = false;


  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService,
              private _dependenciaSandbox: DependenciaSandbox) {

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

  ngOnInit(): void {

    console.log("Tarea de entrada");
    console.log(this.taskData);

    if(this.taskData.variables.numeroRadicado){
      this.hasNumberRadicado = true;
    }

    this.form.get('responderRemitente').valueChanges.subscribe(responderRemitente => {
      this.responderRemitente = responderRemitente;

      if(responderRemitente) {

        if (this.taskData.variables.numeroRadicado) {
          this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
            nroRadicado: this.taskData.variables.numeroRadicado
          }).subscribe(agente => {

            console.log("Objeto que viene del backen ",agente);

            if(agente){

              this.defaultDestinatarioTipoComunicacion = agente.codTipoRemite;

               let tempDestinatario = <DestinatarioDTO> {};

                tempDestinatario.interno = false;
                tempDestinatario.tipoDestinatario = (agente.indOriginal) ? agente.indOriginal : null;
                tempDestinatario.tipoPersona = (agente.codTipoPers) ? agente.codTipoPers : null;
                tempDestinatario.nombre= (agente.nombre) ? agente.nombre : "";
                tempDestinatario.tipoDocumento=(agente.codTipDocIdent) ? agente.codTipDocIdent : null;
                tempDestinatario.nit= (agente.nit) ? agente.nit : "";
                tempDestinatario.actuaCalidad= (agente.codEnCalidad) ? agente.codEnCalidad : null;
                tempDestinatario.actuaCalidadNombre= (agente.codEnCalidad) ? agente.codEnCalidad : "";
                tempDestinatario.sede = (agente.codSede) ? agente.codSede : null;
                tempDestinatario.dependencia = (agente.codDependencia) ? agente.codDependencia : null;
                tempDestinatario.funcionario = null;
                tempDestinatario.email = "";
                tempDestinatario.mobile= "";
                tempDestinatario.phone= "";
                tempDestinatario.pais = null;
                tempDestinatario.departamento= null;
                tempDestinatario.municipio = null;
                tempDestinatario.datosContactoList=(agente.datosContactoList) ? agente.datosContactoList : null;
                tempDestinatario.principal = false;

              if(agente.codTipoRemite == "EXT"){

                tempDestinatario.interno = false;
                this.destinatarioExterno = tempDestinatario;
                this.destinatarioExternoDialogVisible = true;

              }else if(agente.codTipoRemite == "INT"){

                tempDestinatario.interno= true;
                this.destinatarioInterno = tempDestinatario;
                this.destinatarioInternoDialogVisible = true;
              }
            }

            this.refreshView();
          });
        }

      }else{

        console.log("le doy al checke ", this.destinatarioExterno);

        if(this.defaultDestinatarioTipoComunicacion == "EXT"){

          const index: number = this.listaDestinatariosExternos.indexOf(this.destinatarioExterno);
          if (index !== -1) {
            this.listaDestinatariosExternos.splice(index, 1);
          }

        }else if(this.defaultDestinatarioTipoComunicacion == "INT"){

          const index: number = this.listaDestinatariosInternos.indexOf(this.destinatarioInterno);
          if (index !== -1) {
            this.listaDestinatariosInternos.splice(index, 1);
          }
        }
      }

      //this.refreshView();
    });
  }

  //guardar para la base de datos
  /*updateStatus(currentStatus: StatusDTO) {

    console.log('entro en el updateStatus');
    console.log(currentStatus);

    this.form.get('responderRemitente').setValue(currentStatus.datosContacto.responderRemitente);
    this.form.get('distribucion').setValue(currentStatus.datosContacto.distribucion);

    if (currentStatus.datosGenerales.tipoComunicacion.codigo === 'SI') {
      this.listaDestinatariosInternos = [...currentStatus.datosContacto.listaDestinatarios];
    } else if (currentStatus.datosGenerales.tipoComunicacion.codigo === 'SE') {
      this.remitenteExterno = currentStatus.datosContacto.remitenteExterno;
    }
    this.refreshView();
  }*/

  initForm() {
    this.form = this.formBuilder.group({
      // Datos destinatario
      'responderRemitente': [null],
      'distribucion': [null],
    });
  }

  showAddDestinatarioExternoPopup(){
    this.tipoComunicacionSelected = "EE";
    this.datosRemitentesExterno.initFormByDestinatario(this.destinatarioExterno);

    this.destinatarioExternoDialogVisible = true;
  }

  showAddDestinatarioInternoPopup(){
    this.tipoComunicacionSelected = "EI";
    this.datosRemitentesInterno.initFormByDestinatario(this.destinatarioInterno);

    this.destinatarioInternoDialogVisible = true;
  }

  hideAddDestinatarioExternoPopup(){
    this.destinatarioExternoDialogVisible = false;
  }

  hideAddDestinatarioInternoPopup(){
    this.destinatarioInternoDialogVisible = false;
  }

  addDestinatario(newDestinatario){

    console.log(newDestinatario);

    if(newDestinatario.interno){
      this.listaDestinatariosInternos = [newDestinatario, ...this.listaDestinatariosInternos];
    }else{
      this.listaDestinatariosExternos = [newDestinatario, ...this.listaDestinatariosExternos];
    }

    //this.valueRemitente = this.datosRemitentes.form.value;
    //this.listaDestinatariosExternos.push(this.destinatarioExterno);

    console.log("Lo que trae el form ",this.listaDestinatariosExternos);
  }

  findDestinatarioPrincipal(listDestinatarios){}

  ngOnDestroy() {
    //this.subscription.unsubscribe();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
