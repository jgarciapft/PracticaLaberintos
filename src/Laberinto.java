/**
 * Clase que modela el comportamiento del Laberinto (encargado de gestionar las casillas
 * asociadas a este mismo). Dimensión establecida : 10
 */
public class Laberinto {

    private static final int DEF_DIEMNSION = 10;        // Dimensión por defecto del laberinto

    private static Laberinto instancia;                 // Instancia Singleton del laberinto

    private Casilla[][] laberinto;                      // Matriz de casillas que representa un tablero
    private final int dimension;                        // Dimensión del laberinto
    private int umbral;                                 // Umbral asociado al laberinto

    /**
     * @param dimension Dimensión del laberinto a crear
     */
    private Laberinto(int dimension) {
        laberinto = new Casilla[dimension][dimension];
        this.dimension = dimension;
    }

    /**
     * Inserta una casilla en una posición determinada del Laberinto
     *
     * @param casilla  Casilla a insertar en el Laberinto
     * @param posicion Posición del Laberinto en la que insertar la nueva casilla
     */
    public void insertarCasilla(Casilla casilla, Posicion posicion) {
        laberinto[posicion.getX()][posicion.getY()] = casilla;
    }

    /**
     * Carga un laberinto a partir de una matriz de casillas que lo conforman
     *
     * @param laberinto Matriz de casillas que conforman el laberinto
     */
    public void cargarLaberinto(Casilla[][] laberinto) {
        this.laberinto = laberinto;
    }

    /**
     * @return Instancia Singleton del laberinto
     */
    public static Laberinto recuperarInstancia() {
        if (instancia == null)
            instancia = new Laberinto(DEF_DIEMNSION);

        return instancia;
    }

    /**
     * @return Dimensión del laberinto
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @return Umbral asociado al laberinto
     */
    public int getUmbral() {
        return umbral;
    }

    /**
     * @param umbral Nuevo valor del umbral
     */
    public void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Umbral : ").append(getUmbral()).append("\n\n");
        for (int i = 0; i < getDimension(); i++) {
            for (int j = 0; j < getDimension(); j++)
                stringBuilder.append(laberinto[i][j]).append(" ");
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
