import {Component, OnInit} from '@angular/core';
import {ProductsService} from 'app/infrastructure/api/products.service';
import {DemoModel} from './radicar.model';
import {Product} from '../../../domain/product';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-home',
  templateUrl: './radicar.component.html'
})
export class RadicarComponent implements OnInit {
  demoModel: DemoModel;

  selectedProduct: Observable<Product>;

  constructor(private _productApi: ProductsService) {
  }

  deleteProduct(product) {
    console.log(product);
  }

  selectProduct(product) {
    console.log(product);
  }

  onClick() {
    console.log(this.selectedProduct);
  }

  ngOnInit() {
    this.demoModel = new DemoModel();
    this._productApi.list().subscribe(
      data => this.demoModel.products = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

}
