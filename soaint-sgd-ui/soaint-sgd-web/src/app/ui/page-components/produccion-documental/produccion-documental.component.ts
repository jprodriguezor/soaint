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



    nextStep() : boolean {
        if (isNaN(this.step)) { return false; }
        this.step++;

        let status = this.navbarStatus();



        return status;
    }

    prevStep() : boolean {
        if (isNaN(this.step)) { return false; }
        this.step--;

        let status = this.navbarStatus();



        return status;
    }

    navbarStatus() : boolean {
        if (this.step > 0 && this.step < 4) {
          this.revisar = this.aprobar = false;
          return true;
        }
        if (this.step > 3 && this.step < 7) {
          this.revisar = true;
          this.aprobar = false;
          return true;
        }
        if (this.step > 6 && this.step < 10) {
          this.revisar = false;
          this.aprobar = true;
          return true;
        }

        console.log("Step: "+this.step);
        console.log("Revisar: "+this.revisar);
        console.log("Aprobar: "+this.aprobar);

        return false;
    }

    ngOnInit(): void {

        this.activatedRoute.queryParams.subscribe((params: Params) => {
            this.step = params.hasOwnProperty('step')? params['step'] : 1;
            console.log(this.step);
        });

    }
}
