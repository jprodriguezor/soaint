import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit{

  constructor(private activatedRoute: ActivatedRoute) {}

  revisar: boolean = false;
  aprobar: boolean = false;
  tabIndex = 0;



  updateTabIndex(event) {
    this.tabIndex = event.index;
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
