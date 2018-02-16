import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
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

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  validations: any = {};

  remitenteExterno: AgentDTO;
  listaDestinatariosInternos: DestinatarioDTO[] = [];

  @ViewChild('destinatarioExterno') destinatarioExterno;
  @ViewChild('destinatarioInterno') destinatarioInterno;
  //@ViewChild('datosRemitente') datosRemitente;
  @Input() taskData: TareaDTO;

  canInsert = false;
  responseToRem = false;
  hasNumberRadicado = false;

  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService) {

    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {

      this.tipoComunicacionSelected = tipoComunicacion;
      this.responseToRem = false;
      this.form.get('responderRemitente').setValue(false);


    });

    this.initForm();
  }

  ngOnInit(): void {

    this.hasNumberRadicado = !!this.taskData.variables.numeroRadicado;

    this.form.get('responderRemitente').valueChanges.subscribe(responderRemitente => {
      this.responseToRem = responderRemitente;

      if(this.responseToRem && this.taskData.variables.numeroRadicado){

          this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
            nroRadicado: this.taskData.variables.numeroRadicado
          }).subscribe( contacto => {

            if(this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SE'){
              this.remitenteExterno = contacto;
            }else if(this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SI'){
              this.listaDestinatariosInternos.push(contacto);
            }
          });
      }

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

