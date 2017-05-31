import {Component, OnInit} from '@angular/core';
import {ProductsService} from 'app/infrastructure/api/products.service';

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
