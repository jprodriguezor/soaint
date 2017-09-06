/**
 * Created by Ernesto on 2017-09-04.
 */

import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

import {CargaMasivaService} from '../providers/carga-masiva.service';
import {CargaMasivaDTO} from '../domain/CargaMasivaDTO';
import {Observable} from 'rxjs/Observable';



@Component({
  selector: 'carga-masiva-details',
  templateUrl: 'carga-masiva-details.component.html',
  styleUrls: ['../carga-masiva.component.css'],
  providers: [CargaMasivaService]
})

export class CargaMasivaDetailsComponent implements OnInit {

  registros$: Observable<CargaMasivaDTO[]>;
  selectedRecord: CargaMasivaDTO;

  constructor(private router: Router, private cmService: CargaMasivaService) {}


  getRegistros(): void {
      this.registros$ = this.cmService.getRecords();
  }

  gotoDetail(): void {

  }

  onSelect(record: CargaMasivaDTO): void {
    this.selectedRecord = record;
  }

  ngOnInit(): void {
      console.log('asasasas');
      this.getRegistros();
  }

}
