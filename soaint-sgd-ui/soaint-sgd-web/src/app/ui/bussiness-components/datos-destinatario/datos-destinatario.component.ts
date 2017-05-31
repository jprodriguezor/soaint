import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html',
  styleUrls: ['./datos-destinatario.component.css']
})
export class DatosDestinatarioComponent implements OnInit {


  brands: string[] = ['Audi','BMW','Fiat','Ford','Honda','Jaguar','Mercedes','Renault','Volvo','VW'];

  filteredBrands: any[];

  selectedBrands: string[];

  constructor() {}

  ngOnInit() {
  }

  handleACDropdownClick() {
    this.filteredBrands = [];

    //mimic remote call
    setTimeout(() => {
      this.filteredBrands = this.brands;
    }, 100)
  }

}
