package uex;

import uex.algoritmos.*;
import uex.parsers.CasillaParserAmpl;

import java.io.File;
import java.io.FileNotFoundException;

public class Simulador {

    private static final String DEF_RUTA_FCH_LABERINTOS = "res/laberintos_ampl";

    public static void main(String[] args) {
        boolean hayRutaLabAlt = args.length != 0;                   // Si se ha indicado una carpeta externa
        CargadorLaberinto cargador = null;
        int idx = 1;

        try {
            cargador = new CargadorLaberinto(new File(hayRutaLabAlt ? args[0] : DEF_RUTA_FCH_LABERINTOS),
                    new CasillaParserAmpl());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Ejecutores de cada algorítmo de búsqueda
        EjecutorExpansor genYPrueba = new EjecutorExpansor(new GeneracionYPrueba(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor escSimple = new EjecutorExpansor(new EscaladaSimple(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor escMaxPen = new EjecutorExpansor(new EscaladaMaximaPendiente(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor primeroMejor = new EjecutorExpansor(new PrimeroMejor(EjecutorExpansor.heuristicaPorDefecto()));
        EjecutorExpansor aEstrella = new EjecutorExpansor(new AEstrella(EjecutorExpansor.heuristicaPorDefecto()));

        // Ejecuta cada algoritmo implementado con cada heurística implementada
        //noinspection ConstantConditions
        do {
            System.out.println("\n\n\t--- LABERINTO " + idx++ + " ---\n\n");

            genYPrueba.ejecutar();
            escSimple.ejecutar();
            escMaxPen.ejecutar();
            primeroMejor.ejecutar();
            aEstrella.ejecutar();
        } while (cargador.cargarSiguienteLaberinto());
    }

}
