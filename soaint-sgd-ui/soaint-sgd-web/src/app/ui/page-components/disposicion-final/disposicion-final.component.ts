import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { TareaDTO } from 'app/domain/tareaDTO';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Rx';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { DisposicionFinalDTO } from 'app/domain/DisposicionFinalDTO';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import { UnidadDocumentalApiService } from '../../../infrastructure/api/unidad-documental.api';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { Subscription} from 'rxjs/Subscription';
import { isNullOrUndefined } from 'util';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';

@Component({
  selector: 'disposicion-final',
  templateUrl: './disposicion-final.component.html',
  encapsulation: ViewEncapsulation.None,
})

export class DisposicionFinalComponent implements OnInit, OnDestroy {

    state: StateUnidadDocumentalService;
    // form
    formBuscar: FormGroup;
    validations: any = {};
    subscribers: Array<Subscription> = [];
    tipoDisposicion = '';
    listadoSeries$: Observable<SerieDTO[]> = Observable.of([]);
    listaDisposiciones: any[] = [
      { label:'Conservación total', value: 'CT' }, 
      { label:'Eliminar', value: 'E' },
      { label:'Seleccionar', value: 'S' }, 
      { label:'Microfilmar', value: 'M' }, 
      { label:'Digitalizar', value: 'D' },
    ];
    listaDisposicionesEjecutar: any[] = [
      { label:'Conservación total', value: 'CT' }, 
      { label:'Eliminar', value: 'E' },
    ];
    
    indexUnidadSeleccionada: number = null;
    abrirNotas = false;

    constructor(
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _unidadDocumentalStateService: StateUnidadDocumentalService,
              private fb: FormBuilder,
              private _detectChanges: ChangeDetectorRef,
              private _store: Store<State>
            ) {
      this.state = _unidadDocumentalStateService;
    }

    ngOnInit() {      
      this.InitForm();
      this.SetListadoSubscriptions();
      this.StateLoadData();
    }

    InitForm() {
      this.formBuscar = this.fb.group({
       tiposDisposicionFinal: [null, [Validators.required]],
       sede: [null, [Validators.required]],
       dependencia: [null, [Validators.required]],
       serie: [null],
       subserie: [null],
       identificador: [''],
       nombre: [''],
       descriptor1: [''],
       descriptor2: [''],
      });
      this.SetFormSubscriptions();
   }

    StateLoadData() {
      this.state.GetListadoSedes();
      this.state.ListadoUnidadDocumental = [];
    }

    ngOnDestroy() {
      this.subscribers.forEach(obs => {
        obs.unsubscribe();
      });
    }

    ResetForm() {
      this.formBuscar.reset();
      this.state.ListadoSubseries = [];
      this.state.ListadoSeries = [];
      this.state.dependencias = [];
      this.state.ListadoUnidadDocumental = [];
      this.state.unidadesSeleccionadas = [];
      this._detectChanges.detectChanges();
    }

    OnBlurEvents(control: string) {
      this.SetValidationMessages(control);
    }
  
  SetValidationMessages(control: string) {
      const formControl = this.formBuscar.get(control);
      if (formControl.touched && formControl.invalid) {
        const error_keys = Object.keys(formControl.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
          this.validations[control] = '';
      }
  }
  
  SetFormSubscriptions() {
    this.subscribers.push(
      this.formBuscar.get('sede').valueChanges.distinctUntilChanged().subscribe(value => {
        this.formBuscar.controls['dependencia'].reset();   
        this.state.dependencias = [];      
        this.state.GetListadoDependencias(value.id);
      }));
      this.subscribers.push(
        this.formBuscar.get('dependencia').valueChanges.distinctUntilChanged().subscribe(value => {
          this.formBuscar.controls['serie'].reset();   
          this.state.ListadoSeries = []; 
          if(value) {
              this.state.GetListadosSeries(value.codigo);
          }
      }));
      
      this.subscribers.push(
          this.formBuscar.get('serie').valueChanges.distinctUntilChanged().subscribe(value => {
            this.formBuscar.controls['subserie'].reset(); 
            const coddependencia = this.formBuscar.controls['dependencia'].value || null; 
            if(coddependencia) {
              this.state.GetSubSeries(value, coddependencia.codigo )
              .subscribe(result => {
                this.state.ListadoSubseries =  result;                          
               });
            }      

       }));
  }

  SetListadoSubscriptions() {
    this.subscribers.push(this.state.ListadoActualizado$.subscribe(()=>{       
      this._detectChanges.detectChanges();
    }));
  }

  Listar() {
    this.state.ListarDisposicionFinal(this.GetPayload());
  }

  AplicarDisposicion() {
    this.state.AplicarDisposicion(this.tipoDisposicion);
  }

  AgregarNotas(index: number) {
    this.indexUnidadSeleccionada = index;
    this.abrirNotas = true;
  }

  CerrarNotas() {
    this.abrirNotas = false;
  }

  GetPayload(): DisposicionFinalDTO {    
        const payload: DisposicionFinalDTO = {
          disposicionFinalList: [],
          unidadDocumentalDTO: {}
        };

        if (this.formBuscar.controls['tiposDisposicionFinal'].value) {
          payload.disposicionFinalList = this.formBuscar.controls['tiposDisposicionFinal'].value;
        } 
        if (this.formBuscar.controls['dependencia'].value) {
          payload.unidadDocumentalDTO.codigoDependencia = this.formBuscar.controls['dependencia'].value.codigo;
        }   
        if (this.formBuscar.controls['serie'].value) {
          payload.unidadDocumentalDTO.codigoSerie = this.formBuscar.controls['serie'].value;
        }
        if (this.formBuscar.controls['subserie'].value) {
          payload.unidadDocumentalDTO.codigoSubSerie = this.formBuscar.controls['subserie'].value;
        }
        if (this.formBuscar.controls['identificador'].value) {
          payload.unidadDocumentalDTO.id = this.formBuscar.controls['identificador'].value;
        }
        if (this.formBuscar.controls['nombre'].value) {
          payload.unidadDocumentalDTO.nombreUnidadDocumental = this.formBuscar.controls['nombre'].value;
        }
        if (this.formBuscar.controls['descriptor1'].value) {
          payload.unidadDocumentalDTO.descriptor1 = this.formBuscar.controls['descriptor1'].value;
        }
        if (this.formBuscar.controls['descriptor2'].value) {
          payload.unidadDocumentalDTO.descriptor1 = this.formBuscar.controls['descriptor2'].value;
        }
    
        return payload;
    }

    Finalizar() {
      if(this.state.ListadoUnidadDocumental.length) {
        const item_pendiente = this.state.ListadoUnidadDocumental.find(_item => _item.estado === null || _item.estado === '');
        if(item_pendiente) {
          this._store.dispatch(new PushNotificationAction({severity: 'warning', summary: 'Recuerde que debe aprobar/rechazar todas las unidades documentales'}));
        } else {
          this.state.ActualizarDisposicionFinal();
        }
      }
    }

}
