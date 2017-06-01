import { Component, OnInit } from '@angular/core';
import { ProductsService } from 'app/infrastructure/api/products.service';

@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
  styleUrls: ['./datos-remitente.component.css']
})
export class DatosRemitenteComponent implements OnInit {

  selectedBrands: string[];

  brands: string[] = ['Audi', 'BMW', 'Fiat', 'Ford', 'Honda', 'Jaguar', 'Mercedes', 'Renault', 'Volvo', 'VW'];

  filteredBrands: any[];


  checkboxValues: string[] = [];

  constructor(private _productApi: ProductsService) {
  }


  ngOnInit(): void {
  }

  filterBrands(event) {
    this.filteredBrands = [];
    for (let i = 0; i < this.brands.length; i++) {
      const brand = this.brands[i];
      if (brand.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.filteredBrands.push(brand);
      }
    }
  }

  handleACDropdownClick() {
    this.filteredBrands = [];

    // mimic remote call
    setTimeout(() => {
      this.filteredBrands = this.brands;
    }, 100)
  }

}
