package uex.movimiento;

import uex.Casilla;
import uex.Laberinto;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorMovimiento {

    // Coordendas de la posición inicial común a los controladores de movimiento
    private static final int POS_X_INI = 0;
    private static final int POS_Y_INI = 0;

    private Posicion posicion;                  // Posición actual del controlador

    /**
     * Instancia un controlador de movimiento en la posición {x:0, y:0}
     */
    public ControladorMovimiento() {
        posicion = new Posicion(POS_X_INI, POS_Y_INI);
    }

    /**
     * @param posicionInicial Posición inicial
     */
    public ControladorMovimiento(Posicion posicionInicial) {
        this.posicion = posicionInicial;
    }

    /**
     * Mueve la posición del controlador mediante una transformación de movimiento {@link Movimiento}
     *
     * @param movimiento Contiene la dirección y unidades de desplazamiento
     * @throws IllegalArgumentException Si el movimiento resulta fuera de los límites del laberinto
     */
    public void setPosicionRelativa(Movimiento movimiento) {
        if (!movimientoLegal(aplicarMovimiento(movimiento)))
            throw new IllegalArgumentException("No se puede mover más allá de los límites del laberinto.\n\t" +
                    "Movimiento : " + movimiento + "\n\tPosición actual : " + posicion());

        // Mueve la posición en la dirección indicada las unidades indicadas
        setPosicion(aplicarMovimiento(movimiento));
    }


    /**
     * Mueve la posición del controlador a una posición absoluta indicada por parámetro
     *
     * @param posicionAbsoluta Posición de destino
     * @throws IllegalArgumentException Si la posición suministada está fuera de los límites del laberinto
     */
    public void setPosicionAbsoluta(Posicion posicionAbsoluta) {
        if (!movimientoLegal(posicionAbsoluta))
            throw new IllegalArgumentException("Movimiento absoluto fuera de los límites del laberinto.\n\t" +
                    "Movimiento : " + posicionAbsoluta);

        setPosicion(posicionAbsoluta);
    }

    /**
     * Transforma la posición actual en otra posición por el movimiento indicado. No actualiza la posición actual.
     * Suma a las coordenadas de una posición las unidades indicadas por el movimiento en una dirección determinada
     *
     * @param movimiento Movimiento a aplicar
     * @return Posición destino
     */
    public Posicion aplicarMovimiento(Movimiento movimiento) {
        // Copia de la posición actual en la que aplicar el movimiento
        Posicion posFinal = new Posicion(posicion());

        // Aplica el movimiento
        switch (movimiento.getDirMovimiento()) {
            case NORTE:
                posFinal.setY(posFinal.getY() - movimiento.getUnidades());
                break;
            case NORESTE:
                posFinal.setPos(posFinal.getX() + movimiento.getUnidades(),
                        posFinal.getY() - movimiento.getUnidades());
                break;
            case ESTE:
                posFinal.setX(posFinal.getX() + movimiento.getUnidades());
                break;
            case SURESTE:
                posFinal.setPos(posFinal.getX() + movimiento.getUnidades(),
                        posFinal.getY() + movimiento.getUnidades());
                break;
            case SUR:
                posFinal.setY(posFinal.getY() + movimiento.getUnidades());
                break;
            case SUROESTE:
                posFinal.setPos(posFinal.getX() - movimiento.getUnidades(),
                        posFinal.getY() + movimiento.getUnidades());
                break;
            case OESTE:
                posFinal.setX(posFinal.getX() - movimiento.getUnidades());
                break;
            case NOROESTE:
                posFinal.setPos(posFinal.getX() - movimiento.getUnidades(),
                        posFinal.getY() - movimiento.getUnidades());
        }

        return posFinal;
    }

    /**
     * @return Lista de movimientos unitarios que resultan en movimientos posibles
     */
    public List<Movimiento> movimientosPosibles() {
        // Lista de direcciones de movimiento válidas
        List<Direccion> direccionesPosibles = new ArrayList<>(EnumSet.allOf(Direccion.class))
                .stream()
                .filter(direccion -> movimientoLegal(aplicarMovimiento(new MovimientoUnitario(direccion))))
                .collect(Collectors.toList());
        // Devuelve las direcciones de movimiento válidas como movimientos unitarios
        return direccionesPosibles
                .stream()
                .map(MovimientoUnitario::new)
                .collect(Collectors.toList());
    }

    /**
     * Resetea el controlador a la posición inicial
     */
    public void resetPosicion() {
        setPosicionAbsoluta(new Posicion(POS_X_INI, POS_Y_INI));
    }

    /**
     * @param posicion Posición absoluta destino del movimiento
     * @return Si el movimiento es legal, es decir, es una posición dentro de los límites del laberinto y no representa
     * una casilla de obstáculo
     */
    private boolean movimientoLegal(Posicion posicion) {
        return posicion.getX() >= 0 &&
                posicion.getX() < Laberinto.instancia().getDimension() &&
                posicion.getY() >= 0 &&
                posicion.getY() < Laberinto.instancia().getDimension() &&
                !Laberinto.instancia().casilla(posicion).getTipo().equals(Casilla.TipoCasilla.OBSTACULO);
    }

    /**
     * @return Posición asociada al controlador
     */
    public Posicion posicion() {
        return posicion;
    }

    /**
     * @param posicion Nueva posición del controlador
     */
    private void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
}
