package uex.algoritmos;

import uex.Jugador;
import uex.heuristicas.Heuristica;
import uex.movimiento.Posicion;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado del laberinto ponderado mediante una función de ponderación que devuelve el resultado de sumar la
 * puntuación heurística y el coste acumulado (umbral)
 *
 * @author Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * @author Sergio Barrantes de la Osa
 */
public class EstadoLaberintoPonderado extends EstadoLaberinto {

    private Heuristica heuristica;                      // Función heurística con la que evaluar el estado
    private int ponderacion;                            // Ponderación asociada al estado

    /**
     * @param jugador      Estado del jugador
     * @param posVisitadas Lista de posiciones visitadas
     * @param umbral       Valor restante del umbral
     * @param heuristica   Función empleada para estimar la puntuación heurística del estado
     */
    public EstadoLaberintoPonderado(Jugador jugador, List<Posicion> posVisitadas, int umbral, Heuristica heuristica) {
        super(jugador, posVisitadas, umbral);
        this.heuristica = heuristica;
        actualizarPonderacion();
    }

    /**
     * @param heuristica Función con la que evaluar el estado inicial
     * @return Estado inicial de un laberinto inexplorado
     */
    public static EstadoLaberintoPonderado estadoInicial(Heuristica heuristica) {
        return new EstadoLaberintoPonderado(new Jugador(), new ArrayList<>(), 0, heuristica);
    }

    /**
     * Actualiza el valor de la ponderación del estado
     */
    private void actualizarPonderacion() {
        setPonderacion(puntHeuristica().intValue() + getUmbral());
    }

    /**
     * @return Puntuación heurística asociada al estado
     */
    public Number puntHeuristica() {
        return heuristica.apply(this);
    }

    /**
     * @return Ponderación asociada al estado
     */
    public int getPonderacion() {
        return ponderacion;
    }

    /**
     * Actualiza el coste (umbral) y su ponderación
     *
     * @param umbral Nuevo umbral
     */
    @Override
    protected void setUmbral(int umbral) {
        super.setUmbral(umbral);
        actualizarPonderacion();
    }

    private void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    /**
     * Los estados se ordenan por el orden natural del valor de su ponderación
     *
     * @param o Otro estado
     * @return Orden natural de las ponderaciones
     */
    @Override
    public int compareTo(Object o) {
        return Integer.compare(getPonderacion(), ((EstadoLaberintoPonderado) o).getPonderacion());
    }

    /**
     * @param obj Otro estado
     * @return Dos estados son equivalentes si el jugador se encuentra en la misma posición en el laberinto
     */
    @Override
    public boolean equals(Object obj) {
        return getJugador().ctrlMovimiento().posicion().equals(
                ((EstadoLaberintoPonderado) obj).getJugador().ctrlMovimiento().posicion());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n-------------------------------------------------------------\n");

        stringBuilder.append(getJugador()).append("\n");
        stringBuilder.append("Umbral (Coste) : ").append(getUmbral()).append("\n");
        stringBuilder.append("Puntuación heurística : ").append(puntHeuristica()).append("\n");
        stringBuilder.append("Ponderación : ").append(getPonderacion()).append("\n");

        stringBuilder.append("-------------------------------------------------------------\n");

        return stringBuilder.toString();
    }
}
