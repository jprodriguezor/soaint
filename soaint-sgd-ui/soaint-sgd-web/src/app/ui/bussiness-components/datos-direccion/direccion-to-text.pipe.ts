import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'direccionToText'
})
export class DireccionToTextPipe implements PipeTransform {

  transform(value: any): any {

    let direccion = JSON.parse(value);

    let direccionText = "";

    Object.keys(direccion).forEach( key => {

      if(direccion[key].nombre){
        direccionText+= direccion[key].nombre+" ";
        return;
      }

      direccionText += direccion[key];

    });

    return direccionText;
  }

}
