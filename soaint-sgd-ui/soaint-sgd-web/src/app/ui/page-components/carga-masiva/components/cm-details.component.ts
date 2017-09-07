import {Component, OnInit} from '@angular/core';
import {CargaMasivaService} from "../providers/carga-masiva.service";
import {Observable} from "rxjs/Observable";
import {CargaMasivaDTO} from "../domain/CargaMasivaDTO";
import {ActivatedRoute, ParamMap} from "@angular/router";
import { Location }  from '@angular/common';
import {Store} from "@ngrx/store";
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {go, back} from '@ngrx/router-store';

@Component({
    selector: 'cm-details',
    templateUrl: './cm-details.component.html',
    styleUrls: ['../carga-masiva.component.css'],
    providers: [CargaMasivaService]
})

export class CargaMasivaDetailsComponent implements OnInit {
    registro$: Observable<CargaMasivaDTO>;

    constructor(private cmService: CargaMasivaService,private route: ActivatedRoute,private _store: Store<State>) {}

    goBack(): void {
      this._store.dispatch(back());
    }

    ngOnInit(): void {
      this.registro$ = this.route.paramMap
          .switchMap((params: ParamMap) => this.cmService.getRecord(params.get('id')));
    }
}
