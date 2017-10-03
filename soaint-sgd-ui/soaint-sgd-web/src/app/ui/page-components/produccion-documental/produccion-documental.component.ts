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

    ngOnInit(): void {

      this.activatedRoute.queryParams.subscribe((params: Params) => {
        this.step = params.hasOwnProperty('step')? params['step'] : 1;
        console.log(this.step);
      });

    }
}
