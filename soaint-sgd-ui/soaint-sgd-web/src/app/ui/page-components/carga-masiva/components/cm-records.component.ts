import {Component, OnInit} from '@angular/core';
import {CargaMasivaService} from "../providers/carga-masiva.service";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {go} from '@ngrx/router-store';
import {CargaMasivaList} from "../domain/CargaMasivaList";

@Component({
  selector: 'cm-records',
  templateUrl: './cm-records.component.html',
  styleUrls: ['../carga-masiva.component.css'],
  providers: [CargaMasivaService]
})

export class CargaMasivaRecordsComponent implements OnInit{

  registros$: Observable<CargaMasivaList[]>;

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

