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
import {getTipoComunicacionArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-comunicacion-selectors";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-radicar-documento-producido',
  templateUrl: './radicar-documento-producido.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarDocumentoProducidoComponent extends  RadicarSalidaComponent {

  comunicacionUnsubscriber: Subscription;

  datosGenerales$: Subject<any> = new Subject;
  datosContacto$: Subject<any> = new Subject;

  ideEcm;


  constructor(
    protected _store: Store<RootState>
    , protected _changeDetectorRef: ChangeDetectorRef
    , protected _sandbox: RadicacionSalidaService
    , protected _taskSandbox: TaskSandBox
    , private _asignacionSandbox: AsignacionSandbox
    , private _api: ApiBase
  ) {

    super(_store, _changeDetectorRef, _sandbox, _taskSandbox);
  }

  ngOnInit() {

    super.ngOnInit();

    ViewFilterHook.addFilter("app-datos-direccion-show-block-dist-dig", () => false);
  }

  restore() {
    console.log('RESTORE...');
    if (this.task) {
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        const results = response.payload;
        if (!results) {

          this._taskSandbox.getTareaPersisted(this.task.variables.idInstancia, '0000').map(r => r.payload)
            .subscribe(resp => {

              resp.datosGenerales.reqDigit = 2;
              if(!isNullOrUndefined(this.task.variables.numeroRadicado))
              resp.datosGenerales.radicadosReferidos = [{nombre: this.task.variables.numeroRadicado}];
              resp.datosGenerales.reqDistFisica = resp.datosContacto.distribucion === "física";

              this.datosGenerales$.next(resp.datosGenerales);
              this.ideEcm = resp.datosGenerales.listaVersionesDocumento[0].id;

              this.datosContacto$.next(resp.datosContacto);
            });

          return;
        }

       this.restoreByPayload(results);

        // if (results.contactInProgress) {
        //   const retry = setInterval(() => {
        //     if (typeof this.datosRemitente.datosContactos !== 'undefined') {
        //       this.datosRemitente.datosContactos.form.patchValue(results.contactInProgress);
        //       clearInterval(retry);
        //     }
        //   }, 400)
        // }

      });
    }
  }


  ngOnDestroy() {

    super.ngOnDestroy();

    ViewFilterHook.removeFilter("app-datos-direccion-show-block-dist-dig");

  }

  protected buildTaskCompleteParameters(generales:any,noRadicado:any):any{

    return {
      codDependencia:this.dependencySelected.codigo,
      numeroRadicado:noRadicado,
      requiereDistribucion:generales.reqDistFisica ? "1" : "2"
    }
  }

  protected buildPayload():any{

    let payload = super.buildPayload();

    payload.generales.ideEcm = this.ideEcm;

    return payload;
  }

  radicacionButtonIsShown():boolean {

    const conditions: boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0
    ];

    return conditions.every(condition => condition);
  }

}
