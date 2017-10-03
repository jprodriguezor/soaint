import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";

@Component({
    selector: 'produccion-documental',
    templateUrl: './produccion-documental.component.html',
    styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit{

    constructor(private activatedRoute: ActivatedRoute) {}

    step : number = 1;
    revisar: boolean = false;
    aprobar: boolean = false;

    next() : boolean {
        if (this.step === 3) {

            this.step = 1;
            return true;
        }

        this.step++;

        return false;
    }

    prev() : void {
        this.step--;
    }



    ngOnInit(): void {

        this.activatedRoute.params.subscribe((params: Params) => {
            let action = params.hasOwnProperty('action')? params['action']:'';
            this.revisar = (action == 'revisar');
            this.aprobar = (action == 'aprobar');
            console.log("Aprobar = "+this.aprobar+"; Revisar = "+this.revisar+";");
        });

    }
}
