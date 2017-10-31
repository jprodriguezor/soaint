import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';


@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  tipoPersonaSelected: ConstanteDTO;
  subscription: Subscription;

  responseToRem = true;
  distFisica = false;
  distElectronica = false;

  sedesAdministrativas$: Observable<ConstanteDTO[]>;
  dependencias$: Observable<ConstanteDTO[]>;
  tiposPersona$: Observable<ConstanteDTO[]>;
  tiposDestinatario$: Observable<ConstanteDTO[]>;
  actuanEnCalidad$: Observable<ConstanteDTO[]>;
  tiposDocumento$: Observable<ConstanteDTO[]>;



  constructor(private formBuilder: FormBuilder,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => { this.tipoComunicacionSelected = tipoComunicacion; });
  }


  tipoPersonaChange(event) {
    this.tipoPersonaSelected = event.value;
  }


  initForm() {
      this.form = this.formBuilder.group({
        // Datos destinatario
        'tipoDestinatarioText': [null],
        'tipoDestinatarioList': [{value: null}],
        'tipoDocumentoText': [null],
        'tipoDocumentoList': [{value: null}],
        'tipoPersona': [{value: null}],
        'nombreApellidos': [null],
        'nit': [null],
        'razonSocial': [null],
        'actuaCalidad': [{value: null}],
        'sedeAdministrativa': [{value: false}],
        'dependencia': [{value: false}],
        'funcionario': [{value: false}]
      });



  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
    this.dependencias$ = this._produccionDocumentalApi.getDependencias({});
    this.tiposPersona$ = this._produccionDocumentalApi.getTiposPersona({});
    this.tiposDestinatario$ = this._produccionDocumentalApi.getTiposDestinatario({});
    this.actuanEnCalidad$ = this._produccionDocumentalApi.getActuaEnCalidad({});
    this.tiposDocumento$ = this._produccionDocumentalApi.getTiposDocumento({});

    this.initForm();

    this.tiposPersona$.subscribe( (results) => {
      if (results.length > 0) {
        this.tipoPersonaSelected = results[0];
        this.form.get('tipoPersona').setValue(results[0]);
      }
    });


  }

  ngOnDestroy() {
    // unsubscribe to ensure no memory leaks
    this.subscription.unsubscribe();
  }
}

