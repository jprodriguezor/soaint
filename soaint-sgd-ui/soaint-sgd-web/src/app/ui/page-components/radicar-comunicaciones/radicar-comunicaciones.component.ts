import {Component, OnInit} from '@angular/core';
import {ProductsService} from 'app/infrastructure/__api.include';

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html'
})
export class RadicarComunicacionesComponent implements OnInit {

  constructor(private _productApi: ProductsService) {
  }

  ngOnInit() {
  }

}

