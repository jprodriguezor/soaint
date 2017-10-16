package co.com.foundation.sgd.apigateway.apis;

import java.util.ArrayList;
import java.util.List;

public class Cred {

    public static void main(String[] args) {
        List<String> usuarios = new ArrayList<String>();
        usuarios.add("daniel.barrios:descarga");
        usuarios.add("leinier.alvarez:descarga");
        usuarios.add("dasiel.otero:descarga");
        usuarios.add("ernesto.sanchez:descarga");
        usuarios.add("jorge.infante:descarga");
        usuarios.add("arce:arce");
        for (String usuario : usuarios) {
            System.out.println(usuario + " ----- " + java.util.Base64.getEncoder().encodeToString(usuario.getBytes()));
        }

    }
}
