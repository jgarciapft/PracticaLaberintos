package uex;

import static uex.parsers.CasillaParserAmpl.CHAR_OBSTACULO;

/**
 * Clase encargada de modelar la casilla de un laberinto.
 * Cada casilla almacena un valor que indica qué tipo de casllla es
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class Casilla {

    private static final int VALOR_OBJETIVO = 0;    // Valor de la casilla objetivo

    private final int valor;                        // Valor numérico de la casilla
    private final TipoCasilla tipo;                 // Tipo de la casilla

    /**
     * @param valor Valor inicial de la casilla
     */
    public Casilla(int valor) {
        this.valor = valor;
        tipo = TipoCasilla.ESTANDAR;
    }

    /**
     * @param valor Valor inicial de la casilla
     * @param tipo  Tipo de la casilla
     */
    public Casilla(int valor, TipoCasilla tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    /**
     * @return Si es una celda objetivo
     */
    public boolean esObjetivo() {
        return valor() == VALOR_OBJETIVO;
    }

    /**
     * @return Valor de la casilla
     */
    public int valor() {
        return valor;
    }

    /**
     * @return Tipo de la casilla
     */
    public TipoCasilla getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return esObjetivo() ? " " + valor() + " " :
                getTipo().equals(TipoCasilla.ESTANDAR) ? "[" + valor() + "]" : " " + CHAR_OBSTACULO + " ";
    }

    /**
     * Enumerado para representar distintos tipos de casilla
     * <p>
     * ESTANDAR es el tipo por defecto
     * OBSTACULO representa una casilla a la que no se puede mover
     */
    public enum TipoCasilla {
        ESTANDAR,
        OBSTACULO
    }

}
