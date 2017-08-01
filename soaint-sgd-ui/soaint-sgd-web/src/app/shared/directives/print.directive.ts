import {Directive, ElementRef, Input, AfterViewInit} from '@angular/core';
import {isString} from 'util';
declare const jQuery: any;
declare const require: any;

const layoutStyles = require('./print.directive.css');

@Directive({selector: '[printDirective]'})

export class PrintDirective implements AfterViewInit {

  @Input() printelement: string;

  @Input() inlineStyles: string;

  button = this.e.nativeElement;

  constructor(public e: ElementRef) {
  }

  ngAfterViewInit() {

    const inlineStyles = this.inlineStyles;

    let self = this;
    jQuery(this.button).on('click', function () {

        const html = jQuery('#' + self.printelement).prop('outerHTML');

        const sheets = document.styleSheets;
        console.log(sheets);
        let array = [];
        for (let c = 0; c < sheets.length; c++) {

          array.push(sheets[c].href);

        }

        // let printStyles: any = '';
        //
        // array.forEach(function (value: any, index: any) {
        //   if (isString(value)) {
        //     const res = value.substring(value.indexOf(':') + 1);
        //     printStyles = '<link rel=\'stylesheet\' type=\'text/css\'  href=' + value + ' media=\'print\'>\n' + printStyles;
        //   }
        // });

        const printContents = html;

        let popupWin;

        popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
        popupWin.document.open();
        popupWin.document.write(`
        <html>
            <head>
            <title>Print tab</title>
            
            <style type="text/css">
                ${inlineStyles}
                ${layoutStyles}
            </style>

            </head>
        <body onload="window.print();window.close()">${printContents}</body>
        </html>`
        );
        popupWin.document.close();
      }
    )
  };
}
