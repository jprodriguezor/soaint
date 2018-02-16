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
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  validations: any = {};
  //test: any = { };

  remitenteExterno: AgentDTO;
  defaultDestinatarioInterno: any;
  listaDestinatariosInternos: DestinatarioDTO[] = [];

  @ViewChild('datosDestinatarioExterno') datosDestinatarioExterno;
  @ViewChild('destinatarioInterno') destinatarioInterno;
  //@ViewChild('datosRemitente') datosRemitente;
  @Input() taskData: TareaDTO;

  canInsert = false;
  responseToRem = false;
  hasNumberRadicado = false;

  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService,
              private _dependenciaSandbox: DependenciaSandbox) {

    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {

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
    });

    this.initForm();
  }

  ngOnInit(): void {

    //this.hasNumberRadicado = !!this.taskData.variables.numeroRadicado;

    console.log("Tarea de entrada");
    console.log(this.taskData);

    this.form.get('responderRemitente').valueChanges.subscribe(responderRemitente => {
      this.responseToRem = responderRemitente;

    });

  }

  updateStatus(currentStatus: StatusDTO) {

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
  }

  initForm() {
    this.form = this.formBuilder.group({
      // Datos destinatario
      'responderRemitente': [null],
      'distribucion': [null],
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}

