import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation, IterableDiffers } from '@angular/core';
import { TareaDTO } from 'app/domain/tareaDTO';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Rx';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import { UnidadDocumentalApiService } from '../../../infrastructure/api/unidad-documental.api';
import { StateUnidadDocumentalService } from '../unidades-documentales/state.unidad.documental';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {Subscription} from 'rxjs/Subscription';
import { isNullOrUndefined } from 'util';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';

@Component({
  selector: 'disposicion-final',
  templateUrl: './disposicion-final.component.html',
  encapsulation: ViewEncapsulation.None,
})

export class DisposicionFinalComponent implements OnInit, OnDestroy {

    state: StateUnidadDocumentalService;
    stacked: boolean;

    unidadesDocumentales$: Observable<UnidadDocumentalDTO[]> = Observable.of([]);

    // form
    formBuscar: FormGroup;
    validations: any = {};
    subscribers: Array<Subscription> = [];

    listaDisposiciones: any[];
    selectedItemsListaDisposiciones: any[];

    // control de cambios en array
    differ: any;

    constructor(
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _unidadDocumentalStateService: StateUnidadDocumentalService,
              private fb: FormBuilder,
              private _differs: IterableDiffers,
              private _detectChanges: ChangeDetectorRef,
            ) {
      this.state = _unidadDocumentalStateService;
    }

    ngOnInit() {      
      this.InitForm();
      this.StateLoadData();
    }

    InitForm() {
      this.formBuscar = this.fb.group({
       tipoDisposicionFinal: [null],
       sede: [null],
       dependencia: [null],
       serie: [null],
       subserie: [null],
       identificador: [''],
       nombre: [''],
       descriptor1: [''],
       descriptor2: [''],
      });
   }

    StateLoadData() {
      this.state.GetListadoSedes();
      this.state.Listar(this.GetPayload());
    }

    ngOnDestroy() {

    }

    transponer() {
        this.stacked = !this.stacked;
    }
    ResetForm() {
      this.formBuscar.reset();
      this.state.ListadoSubseries = [];
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
          this.state.GetListadosSeries(value.codigo);
        }));
      
      this.subscribers.push(
          this.formBuscar.get('serie').valueChanges.distinctUntilChanged().subscribe(value => {
            this.formBuscar.controls['subserie'].reset();         
            this.state.GetSubSeries(value, this.formBuscar.controls['dependencia'].value.codigo)
            .subscribe(result => {
              this.state.ListadoSubseries =  result;  
              if(result.length) {
                this.formBuscar.controls['subserie'].setValidators(Validators.required);
                this.formBuscar.updateValueAndValidity();
              }             
             });;
       }));
        this.formBuscar.updateValueAndValidity();
  }

  GetPayload(): UnidadDocumentalDTO {
    
            const payload: UnidadDocumentalDTO = {};
    
            if (this.formBuscar.controls['dependencia'].value) {
                payload.codigoDependencia = this.formBuscar.controls['dependencia'].value;
            }   
            if (this.formBuscar.controls['serie'].value) {
                payload.codigoSerie = this.formBuscar.controls['serie'].value;
            }
            if (this.formBuscar.controls['subserie'].value) {
                payload.codigoSubSerie = this.formBuscar.controls['subserie'].value;
            }
            if (this.formBuscar.controls['identificador'].value) {
                payload.codigoUnidadDocumental = this.formBuscar.controls['identificador'].value;
            }
            if (this.formBuscar.controls['nombre'].value) {
                payload.nombreUnidadDocumental = this.formBuscar.controls['nombre'].value;
            }
            if (this.formBuscar.controls['descriptor1'].value) {
                payload.descriptor1 = this.formBuscar.controls['descriptor1'].value;
            }
            if (this.formBuscar.controls['descriptor2'].value) {
                payload.descriptor1 = this.formBuscar.controls['descriptor2'].value;
            }
    
            return payload;
        }
  
    ngDoCheck() {
      const change = this.differ.diff(this.state.ListadoUnidadDocumental);
      if (change) {
        this._detectChanges.detectChanges();
      }
    }

}
