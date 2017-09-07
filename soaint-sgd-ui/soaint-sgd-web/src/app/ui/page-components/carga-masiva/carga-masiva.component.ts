import {Component, OnInit} from '@angular/core';
import {CargaMasivaService} from "./providers/carga-masiva.service";
import {Observable} from "rxjs/Observable";
import {CargaMasivaDTO} from "./domain/CargaMasivaDTO";
import {Store} from "@ngrx/store";
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {go} from '@ngrx/router-store';

@Component({
    selector: 'carga-masiva',
    templateUrl: './carga-masiva.component.html',
    styleUrls: ['./carga-masiva.component.css'],
    providers: [CargaMasivaService]
})

export class CargaMasivaComponent implements OnInit{

    registros$: Observable<CargaMasivaDTO[]>;


    constructor(private _store: Store<State>, private cmService: CargaMasivaService) {}


    getRegistros(): void {
        this.registros$ = this.cmService.getRecords();
    }

    goToDetails(id: any): void {
        this._store.dispatch(go('/carga-masiva/record/'+id));
    }

    ngOnInit(): void {
        this.getRegistros();
    }
}
