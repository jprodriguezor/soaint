import { Usuario } from 'app/domain/usuario';

export class LoginModel {

    user: Usuario;
    loggedin: boolean;

    constructor() {
        this.loggedin = false;
        this.user = new Usuario();
    }

}
