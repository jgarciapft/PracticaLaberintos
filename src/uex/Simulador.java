package uex;

import uex.algoritmos.ExpansorArbol;
import uex.algoritmos.GeneracionYPrueba;
import uex.parsers.CasillaParser;

import java.io.File;
import java.io.FileNotFoundException;

public class Simulador {

    public static void main(String[] args) {
        // TESTING ->

        CargadorLaberinto cargador = null;
        ExpansorArbol generacionYPrueba = new GeneracionYPrueba(null);

        try {
            cargador = new CargadorLaberinto(new File("res/laberintos"), new CasillaParser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //noinspection ConstantConditions
        while (cargador.cargarSiguienteLaberinto()) {
            System.out.println(Laberinto.instancia());
            generacionYPrueba.resolver();
        }

        // <- TESTING
    }
}
