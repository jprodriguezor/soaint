import {RadicarSalidaComponent} from "./radicar-salida.component";
import {ChangeDetectorRef, Component} from "@angular/core";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {Sandbox as TaskSandBox} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {Sandbox as AsignacionSandbox} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {Subscription} from "rxjs/Subscription";
import {combineLatest} from "rxjs/observable/combineLatest";
import {getActiveTask} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {ComunicacionToSource} from "../../../shared/data-transformers/comunicacionToSource";
import {ApiBase} from "../../../infrastructure/api/api-base";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";

@Component({
  selector: 'app-radicar-documento-producido',
  templateUrl: './radicar-documento-producido.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarDocumentoProducidoComponent extends  RadicarSalidaComponent{

  comunicacionUnsubscriber:Subscription;

  datosGenerales$:Subject<any> = new Subject;
  datosContacto$:Subject<any> = new Subject;


  constructor(
    protected _store: Store<RootState>
    ,protected _changeDetectorRef: ChangeDetectorRef
    ,protected _sandbox:RadicacionSalidaService
    ,protected _taskSandbox:TaskSandBox
  ,private _asignacionSandbox:AsignacionSandbox
  ,private _api:ApiBase
  ) {

    super(_store,_changeDetectorRef,_sandbox,_taskSandbox);
  }

  ngOnInit(){

    super.ngOnInit();

    ViewFilterHook.addFilter("app-datos-direccion-show-block-dist-dig",() => false);

    this._asignacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado)
      .subscribe(comunicacion => {

        console.log("comunicacion cruda",comunicacion);

       let obj = new ComunicacionToSource(comunicacion,this._store,this._api);

        obj.transform().subscribe( resp => {  console.log("tareaData:",this.task);


        resp.generales.reqDigit = 2;
        resp.generales.radicadosReferidos = [{nombre: this.task.variables.numeroRadicado}];

         this.datosGenerales$.next(resp.generales);

         this.datosContacto$.next(resp.datosContacto);

        });


      })




  }

  ngOnDestroy(){

    super.ngOnDestroy();

    ViewFilterHook.removeFilter("app-datos-direccion-show-block-dist-dig");

  }

}
