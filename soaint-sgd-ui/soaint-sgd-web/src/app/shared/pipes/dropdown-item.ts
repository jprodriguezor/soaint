import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'dropdownItem'})
export class DropdownItemPipe implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    return value.map(item => {
      return {label: item.nombre, value: item};
    });
  }
}
