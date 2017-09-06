import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CargaMasivaService} from "./providers/carga-masiva.service";
import {Observable} from "rxjs/Observable";
import {CargaMasivaDTO} from "./domain/CargaMasivaDTO";
import {ROUTES_PATH} from "../../../app.route-names";

@Component({
    selector: 'carga-masiva',
    templateUrl: './carga-masiva.component.html',
    styleUrls: ['./carga-masiva.component.css'],
    providers: [CargaMasivaService]
})

export class CargaMasivaComponent implements OnInit{

    registros$: Observable<CargaMasivaDTO[]>;


    constructor(private router: Router, private cmService: CargaMasivaService) {}


    getRegistros(): void {
        this.registros$ = this.cmService.getRecords();
    }

    goToDetails(id: string): void {
      console.log('Mostrando detalles...'+ROUTES_PATH.cargaMasivaDetails);
      this.router.navigate(['/carga-masiva/record', id]);
    }

    ngOnInit(): void {
        this.getRegistros();
    }
}
