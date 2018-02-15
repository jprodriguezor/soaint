import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {DestinatarioDTO} from '../../../../../domain/destinatarioDTO';
import {ProduccionDocumentalApiService} from "../../../../../infrastructure/api/produccion-documental.api";

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  validations: any = {};

  listaDestinatariosExternos: DestinatarioDTO[] = [];
  listaDestinatariosInternos: DestinatarioDTO[] = [];

  @ViewChild('destinatarioExterno') destinatarioExterno;
  @ViewChild('destinatarioInterno') destinatarioInterno;
  @ViewChild('datosRemitente') datosRemitente;
  @Input() taskData: TareaDTO;

  canInsert = false;
  responseToRem = true;
    hasNumberRadicado = false;

  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
      // this.datosRemitente.setTipoComunicacion(this.tipoComunicacionSelected);
    });

    this.initForm();
  }

  ngOnInit(): void {}

  updateStatus(currentStatus: StatusDTO) {

    this.form.get('responderRemitente').setValue(currentStatus.datosContacto.responderRemitente);
    this.form.get('distribucion').setValue(currentStatus.datosContacto.distribucion);
    if (currentStatus.datosGenerales.tipoComunicacion && currentStatus.datosGenerales.tipoComunicacion.codigo === 'SI') {
      this.listaDestinatariosInternos = [...currentStatus.datosContacto.listaDestinatarios];
    } else if (currentStatus.datosGenerales.tipoComunicacion && currentStatus.datosGenerales.tipoComunicacion.codigo === 'SE') {
      this.listaDestinatariosExternos = [...currentStatus.datosContacto.listaDestinatarios];
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

