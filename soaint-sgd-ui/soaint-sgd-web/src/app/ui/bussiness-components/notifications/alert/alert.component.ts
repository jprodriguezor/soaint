import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {

  visible: boolean = false;
  message:string ;

  @Input() width?:number;

  @Input() height?:number;

  constructor() { }

  ngOnInit() {
  }

  ShowMessage(message:string){

    this.visible = true;
    this.message = message;
  }

  Close(){
     this.visible = false;
   }

}
