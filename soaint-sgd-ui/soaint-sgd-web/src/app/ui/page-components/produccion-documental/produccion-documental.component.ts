import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {PdMessageService} from "./providers/PdMessageService";
import {Subscription} from "rxjs/Subscription";
import {ConstanteDTO} from "../../../domain/constanteDTO";

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit{

  tipoComunicacionSelected : ConstanteDTO;
  subscription : Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => { this.tipoComunicacionSelected = tipoComunicacion; });
  }

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