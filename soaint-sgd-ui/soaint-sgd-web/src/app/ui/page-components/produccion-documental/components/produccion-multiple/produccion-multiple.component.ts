import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-produccion-multiple',
  templateUrl: './produccion-multiple.component.html'
})

export class PDProduccionMultipleComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({

    });
  }

  ngOnInit(): void {
    this.initForm();
  }
}

