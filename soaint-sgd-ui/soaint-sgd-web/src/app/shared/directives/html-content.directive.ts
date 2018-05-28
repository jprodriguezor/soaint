import {Directive, ElementRef, Input, OnInit} from '@angular/core';

@Directive({
  selector: '[appHtmlContent]'
})
export class HtmlContentDirective implements OnInit {

  @Input() content:string;

  constructor( private ref:ElementRef) {
  }

  ngOnInit(): void {

    console.log("contenido de la directiva",this.content);

    this.ref.nativeElement.innerHTML =  this.content;
  }

}
