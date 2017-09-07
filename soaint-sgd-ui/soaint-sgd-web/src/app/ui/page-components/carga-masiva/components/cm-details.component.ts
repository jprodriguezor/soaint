import {Component, OnInit, Input} from '@angular/core';
import {CargaMasivaService} from "../providers/carga-masiva.service";
import {Observable} from "rxjs/Observable";
import {CargaMasivaDTO} from "../domain/CargaMasivaDTO";
import {ActivatedRoute, ParamMap} from "@angular/router";
import { Location }  from '@angular/common';

@Component({
    selector: 'cm-details',
    templateUrl: './cm-details.component.html',
    styleUrls: ['../carga-masiva.component.css'],
    providers: [CargaMasivaService]
})

export class CargaMasivaDetailsComponent implements OnInit {
    @Input() registro: CargaMasivaDTO;

    constructor(
        // private cmService: CargaMasivaService,
        // private route: ActivatedRoute,
        // private location: Location
    ) {}

    goBack(): void {
        // this.location.back();
    }


    ngOnInit(): void {
      // this.route.paramMap
      //     .switchMap((params: ParamMap) => this.cmService.getRecord(+params.get('id')))
      //     .subscribe(registro => this.registro = registro);
    }
}
