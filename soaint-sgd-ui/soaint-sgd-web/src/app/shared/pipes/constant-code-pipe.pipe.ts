import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'constantCode'
})
export class ConstantCodePipe implements PipeTransform {

  transform(value: any, contantes: any []): any {
    let result = '';
    let isSede = false;
    if (contantes) {
      let finded = contantes.find((item) => {
        isSede = item.codSede === value;
        return item.codigo === value || item.codSede === value;
      });
      if (finded) {
        if (!isSede)
          result = finded.nombre;
        else
          result = finded.nomSede;
      }
    }
    return result;
  }

}
