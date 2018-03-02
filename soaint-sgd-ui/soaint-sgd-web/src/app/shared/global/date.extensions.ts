export {}

// DATE EXTENSIONS
// ================

declare global {
    interface Date {
        addDays(days: number, useThis?: boolean): Date;
        isToday(): boolean;
        clone(): Date;
        isAnotherMonth(date: Date): boolean;
        isWeekend(): boolean;
        isSameDate(date: Date): boolean;
        toFormattedString(format: string): string;
    }
}

Date.prototype.addDays = (days: number): Date => {
    if (!days) { return this };
    console.log(this);
    const date = this;
    date.setDate(date.getDate() + days);

    return date;
};

Date.prototype.isToday = (): boolean => {
    const today = new Date();
    return this.isSameDate(today);
};

Date.prototype.clone = (): Date => {
    return new Date(+this);
};

Date.prototype.isAnotherMonth = (date: Date): boolean => {
    return date && this.getMonth() !== date.getMonth();
};

Date.prototype.isWeekend = (): boolean => {
    return this.getDay() === 0 || this.getDay() === 6;
};

Date.prototype.isSameDate = (date: Date): boolean => {
    return date && this.getFullYear() === date.getFullYear() && this.getMonth() === date.getMonth() && this.getDate() === date.getDate();
};

Date.prototype.toFormattedString = function (format) {
    const date = this,
        day = date.getDate(),
        month = date.getMonth() + 1,
        year = date.getFullYear(),
        minutes = date.getMinutes(),
        seconds = date.getSeconds();
    let hours = date.getHours();

    if (!format) { format = 'dd/MM/yyyy'; }

    format = format.replace('MM', month.toString().replace(/^(\d)$/, '0$1'));

    if (format.indexOf('yyyy') > -1) {
        format = format.replace('yyyy', year.toString());
    } else if (format.indexOf('yy') > -1) {
        format = format.replace('yy', year.toString().substr(2, 2));
    }

    format = format.replace('dd', day.toString().replace(/^(\d)$/, '0$1'));

    if (format.indexOf('t') > -1) {
        if (hours > 11) {
            format = format.replace('t', 'pm');
        } else {
            format = format.replace('t', 'am');
        }
    }

    if (format.indexOf('HH') > -1) {
        format = format.replace('HH', hours.toString().replace(/^(\d)$/, '0$1'));
    }

    if (format.indexOf('hh') > -1) {
        if (hours > 12) {
            hours -= 12;
        }

        if (hours === 0) {
            hours = 12;
        }
        format = format.replace('hh', hours.toString().replace(/^(\d)$/, '0$1'));
    }

    if (format.indexOf('mm') > -1) {
        format = format.replace('mm', minutes.toString().replace(/^(\d)$/, '0$1'));
    }

    if (format.indexOf('ss') > -1) {
        format = format.replace('ss', seconds.toString().replace(/^(\d)$/, '0$1'));
    }

    return format;
};

// EOF
