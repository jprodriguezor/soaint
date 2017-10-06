import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'dropdownItem'})
export class DropdownItemPipe implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return value.map(item => {
        return {label: item.nombre, value: item};
      });
    }
  }
}

@Pipe({name: 'dropdownItemFullName'})
export class DropdownItemPipeFullName implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    return value.map(item => {
      return {
        label: item.nombre + ' ' + (item.valApellido1 ? item.valApellido1 : "") + ' ' + (item.valApellido2 ? item.valApellido2 : ""),
        value: item
      };
    });
  }
}
