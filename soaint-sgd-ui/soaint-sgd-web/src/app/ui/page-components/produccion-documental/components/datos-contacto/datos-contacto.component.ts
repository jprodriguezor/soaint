import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {ConstanteDTO} from "app/domain/constanteDTO";
import {ProduccionDocumentalApiService} from "app/infrastructure/api/produccion-documental.api";
import {Observable} from "rxjs/Observable";
import {Subscription} from "rxjs/Subscription";
import {PdMessageService} from "../../providers/PdMessageService";


@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy{

  form: FormGroup;
  tipoComunicacionSelected : any;
  subscription : Subscription;

  tipoComunicacion : number = 1; //1.Externa 2.Interna
  tipoPersona : number = 2; //1.Natural 2.Juridica

  sedesAdministrativas$ : Observable<ConstanteDTO[]>;
  dependencias$ : Observable<ConstanteDTO[]>;
  tiposPersona$ : Observable<ConstanteDTO[]>;
  tiposDestinatario$ : Observable<ConstanteDTO[]>;
  actuanEnCalidad$ : Observable<ConstanteDTO[]>;



  constructor(private formBuilder: FormBuilder,
              private _produccionDocumentalApi : ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService){
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => { this.tipoComunicacionSelected = tipoComunicacion; });
  }


  initForm() {
      this.form = this.formBuilder.group({
        //Distribucion
        'fisica': [{value: false}, Validators.required],
        'electronica': [{value: false}, Validators.required],
        'responseToRem': [{value: false}, Validators.required],

        //Datos destinatario
        'tipoDestinatarioText': [null],
        'tipoDestinatarioList': [{value:null}],
        'tipoDocumentoText': [null],
        'tipoDocumentoList': [{value:null}],
        'tipoPersona': [{value:null}],
        'nombreApellidos': [null],
        'nit': [null],
        'razonSocial': [null],
        'actuaCalidad': [{value:null}],
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

      this.initForm();
  }

  ngOnDestroy() {
    // unsubscribe to ensure no memory leaks
    this.subscription.unsubscribe();
  }
}

