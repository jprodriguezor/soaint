// import { Component, OnInit } from '@angular/core';
// import { ProductosModel } from 'app/ui/page-components/productos/productos.model';
// import { ProductsService } from 'app/infrastructure/api/products.service';
//
// @Component({
//   selector: 'app-productos',
//   templateUrl: './productos.component.html'
// })
// export class ProductosComponent implements OnInit {
//
//   model : ProductosModel;
//
//
//   constructor(private _productApi: ProductsService) {
//     this.model = new ProductosModel();
//   }
//
//   ngOnInit() {
//    this._productApi.list().subscribe(
//       data => this.model.productos = data,
//       error => console.log('Error searching in products api ' + error)
//     );
//   }
//
// }
