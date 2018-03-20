import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Store } from '@ngrx/store';
import { State } from '../../../infrastructure/redux-store/redux-reducers';
import { TareaDTO } from 'app/domain/tareaDTO';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Rx';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import {LoadAction as SedeAdministrativaLoadAction} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {getArrayData as dependenciaGrupoArrayData} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { SerieDTO } from '../../../domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { SerieService } from '../../../infrastructure/api/serie.service';



@Component({
  selector: 'disposicion-final',
  templateUrl: './disposicion-final.component.html',
  encapsulation: ViewEncapsulation.None,
})

export class DisposicionFinalComponent implements OnInit, OnDestroy {

    task: TareaDTO;
    formSearch: FormGroup;
    validations: any = {};
    stacked: boolean;

    tiposDisposicionFinal$: Observable<ConstanteDTO[]>;
    sedes$: Observable<ConstanteDTO[]>;
    dependencias$: Observable<ConstanteDTO[]>;
    series$: Observable<SerieDTO[]>;
    subseries$: Observable<SubserieDTO[]>;

    listaDisposiciones: any[];
    selectedItemsListaDisposiciones: any[];

    constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _serieSubserieApi: SerieService,
              private _changeDetectorRef: ChangeDetectorRef) {

      this.initForm();
      this.listenForChanges();

      Observable.combineLatest(
            this.formSearch.get('sede').valueChanges,
            this.formSearch.get('dependencia').valueChanges,
            this.formSearch.get('serie').valueChanges,
            this.formSearch.get('subserie').valueChanges
      ).filter(([sede, dependencia, serie, subserie]) => sede && dependencia && serie && subserie)
      .zip(([sede, dependencia, serie, subserie]) => {
          return {sede: sede, dependencia: dependencia, serie: serie, subserie: subserie}
      });
    }

    initForm() {
      this.formSearch = this.formBuilder.group({
        'tipoDisposicionFinal': [null, Validators.required],
        'sede': [null, Validators.required],
        'dependencia': [null, Validators.required],
        'serie': [null, Validators.required],
        'subserie': [null, Validators.required],
        'idUnidadDocumental': [null],
        'nombreUnidadDocumental': [null],
        'descriptor1': [null],
        'descriptor2': [null],
      });
    }

    ngOnInit() {
        this.sedes$ = this._store.select(sedeAdministrativaArrayData);
        this.dependencias$ = this._store.select(dependenciaGrupoArrayData);
        this._store.dispatch(new SedeAdministrativaLoadAction());
    }

    ngOnDestroy() {}

    listenForChanges() {
        this.formSearch.get('sede').valueChanges.subscribe((value: ConstanteDTO) => {
            if (value) {
                this.formSearch.get('dependencia').reset();
                this.formSearch.get('serie').reset();
                this.formSearch.get('subserie').reset();
                this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
                // this.series$ = this._serieSubserieApi.ListarSerieSubserie({idOrgOfc: codDependencia});
            }
        });

        this.formSearch.get('dependencia').valueChanges.subscribe((value: ConstanteDTO) => {
            if (value) {
                this.formSearch.get('serie').reset();
                this.formSearch.get('subserie').reset();
                this.series$ = this._serieSubserieApi.getSeriePorDependencia(value.codigo);
            }
        });

        this.formSearch.get('serie').valueChanges.subscribe((value) => {
            if (value) {
                this.formSearch.get('subserie').reset();
                this.subseries$ = this._serieSubserieApi.getSubseriePorDependenciaSerie(this.formSearch.get('dependencia').value.codigo, value);
            }
        });
    }

    transponer() {
        this.stacked = !this.stacked;
    }
}
