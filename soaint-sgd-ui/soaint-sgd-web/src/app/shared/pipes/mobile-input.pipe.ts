import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'countryPhone'})
export class MobilePhonePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) {
      return;
    }

    let cleaned = value.replace(/\D/g, '');
    if (cleaned.length > 10) {
      cleaned = cleaned.substr(0, 10);
    }

    let formated = '';
    const firstSegment = cleaned.substr(0, 3);
    formated += firstSegment;

    const secondSegment = cleaned.substr(3, 3);
    formated += (secondSegment.length > 0) ? '-' + secondSegment : '';

    const thirdSegment = cleaned.substr(6, 4);
    formated += (thirdSegment.length > 0) ? '-' + thirdSegment : '';

    return formated;

  }

}
