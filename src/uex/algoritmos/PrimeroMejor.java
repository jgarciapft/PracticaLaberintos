package uex.algoritmos;

import uex.Jugador;
import uex.Laberinto;
import uex.durian.TreeNode;
import uex.heuristicas.Heuristica;
import uex.movimiento.ControladorMovimiento;
import uex.movimiento.Posicion;

import java.util.*;
import java.util.stream.Collectors;

public class PrimeroMejor extends ExpansorArbol {

    private ArrayList<TreeNode<EstadoLaberinto>> nodosAbiertos;     // Colección de nodos en exploración
    private Set<TreeNode<EstadoLaberinto>> nodosCerrados;           // Colección de nodos explorados

    /**
     * @param heuristica Función heurística a aplicar a los nodos del árbol
     */
    public PrimeroMejor(Heuristica heuristica) {
        super(heuristica);
        resetExpansor();
    }

    @Override
    protected void resetExpansor() {
        setArbolDecision(new TreeNode<>(null, EstadoLaberinto.estadoInicial()));
        nodosAbiertos = new ArrayList<>();
        nodosCerrados = new HashSet<>();
        nodosAbiertos.add(getArbolDecision());                      // Añade el primer nodo, el estado inicial

        setContNodosGen(0);                                         // Reinicia el número de nodos generados
        getReloj().reset();                                         // Reinicia el cronómetro
    }

    @Override
    public void resolver() {
        // REINICIO DE VARIABLES ENTRE RESOLUCIONES DE LABERINTOS
        resetExpansor();
        Laberinto laberinto = Laberinto.instancia();
        TreeNode<EstadoLaberinto> mejorNodo = nodosAbiertos.get(0); // Nodo más prometedor
        Posicion operando;
        Jugador clon;
        ArrayList<Posicion> visitadas;

        getReloj().start();

        // Comprueba si es la casilla objetivo o si queda algún nodo para explorar
        while (nodosAbiertos.size() != 0
                && !laberinto.casilla(mejorNodo.getContent().getJugador().ctrlMovimiento().posicion()).esObjetivo()) {
            agregarNodoCerrado(mejorNodo);                          // Cierra el nodo más prometedor

            // Comprueba que el mejor nodo siga siendo solución viable, sino lo descartamos y pasamos al siguiente mejor
            if (mejorNodo.getContent().getUmbral() <= laberinto.getUmbral()) {
                // Exploramos el mejor nodo
                do {
                    clon = (Jugador) mejorNodo.getContent().getJugador().clone();
                    visitadas = new ArrayList<>();
                    operando = seleccionarOperando(mejorNodo);

                    if (operando != null) {
                        clon.ctrlMovimiento().setPosicionAbsoluta(operando);
                        visitadas.add(mejorNodo.getContent().getJugador().ctrlMovimiento().posicion());

                        EstadoLaberinto estadoExpandido =
                                new EstadoLaberinto(clon, visitadas, mejorNodo.getContent().getUmbral() +
                                        costeAsociado(operando));
                        setContNodosGen(getContNodosGen() + 1);

                        // Comprueba si el nuevo estado ya ha sido expandido
                        if (!enNodos(estadoExpandido)) {
                            // Si no ha sido expandido lo añadimos a la lista de nodos abiertos
                            TreeNode<EstadoLaberinto> nodoExpandido = new TreeNode<>(mejorNodo, estadoExpandido);
                            agregarNodoAbierto(nodoExpandido);
                        }
                    }
                } while (operando != null);
            }

            // Obtenemos el siguiente nodo más prometedor, si queda alguno
            if (nodosAbiertos.size() != 0) mejorNodo = nodosAbiertos.get(0);
        }

        getReloj().stop();

        // Se encontró una solución si quedó algún nodo en la listas de abiertos (el primero es la solución), sino no tiene solución
        if (nodosAbiertos.size() != 0) {
            System.out.println("SOLUCIÓN ENCONTRADA");
            mostrarSolucion(mejorNodo);
        } else {
            System.out.println("NO TIENE SOLUCIÓN");
        }
    }

    private void agregarNodoAbierto(TreeNode<EstadoLaberinto> nodoExpandido) {
        nodosAbiertos.add(nodoExpandido);
        ordenarNodosAbiertos();
    }

    private void ordenarNodosAbiertos() {
        nodosAbiertos.sort(Comparator.comparingInt(nodo -> aplicarHeuristica(nodo).intValue()));
    }

    private TreeNode<EstadoLaberinto> recNodoEquiv(EstadoLaberinto estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .filter(nodo -> nodo.getContent().equals(estadoLaberintoPonderado))
                .collect(Collectors.toList()).get(0);
    }

    private void agregarNodoCerrado(TreeNode<EstadoLaberinto> nodo) {
        nodosAbiertos.remove(nodo);
        nodosCerrados.add(nodo);
    }

    private Collection<TreeNode<EstadoLaberinto>> nodosExpandidos() {
        Set<TreeNode<EstadoLaberinto>> nodos = new HashSet<>(new HashSet<>(nodosAbiertos));
        nodos.addAll(nodosCerrados);

        return nodos;
    }

    private boolean enNodos(EstadoLaberinto estadoLaberintoPonderado) {
        return nodosExpandidos()
                .stream()
                .map(TreeNode::getContent)
                .filter(estadoLaberinto -> estadoLaberinto.equals(estadoLaberintoPonderado))
                .collect(Collectors.toSet()).size() != 0;
    }

    @Override
    protected Posicion seleccionarOperando(TreeNode<EstadoLaberinto> nodo) {
        // Variables del estado del laberinto actual
        EstadoLaberinto estadoLaberinto = nodo.getContent();
        /*
        La lista de posiciones visitadas en primer lugar contiene la posicion del nodo que creó el nodo actual, y tras
        sucesivas llamadas a este método, contendrá cada posicion expandida hasta que no queden posiciones posibles.
        Evita que se vuelva hacia atrás por el nodo padre
         */
        List<Posicion> posVisitadas = estadoLaberinto.getPosVisitadas();
        ControladorMovimiento cMov = estadoLaberinto.getJugador().ctrlMovimiento();

        /*
         Obtiene los movimientos posibles a partir de la posición actual y filtra aquellas que no resulten
         en una posición ya expandida
         */
        List<Posicion> posPosibles = cMov.movimientosPosibles()
                .stream()
                .map(cMov::aplicarMovimiento)
                .filter(posicion -> !posVisitadas.contains(posicion))
                .collect(Collectors.toList());

        // Si queda alguna posición posible la añade a las expandidas
        if (posPosibles.size() != 0) posVisitadas.add(posPosibles.get(0));
        else return null;

        // Siguente posición posible elegida
        return posPosibles.get(0);
    }

    @Override
    protected void mostrarSolucion(TreeNode<EstadoLaberinto> arbolDecision) {
        // Imprime tiempo empleado
        System.out.println("Tiempo empleado : " + getReloj());
        // Imprime el número de nodos generados en memoria
        System.out.println("Número de nodos generados : " + getContNodosGen());

        ArrayList<Posicion> camino = new ArrayList<>();
        recuperarCamino(arbolDecision, camino);
        System.out.println(new Laberinto.Solucionado(camino, arbolDecision.getContent().getUmbral()));
        System.out.println(arbolDecision.getPath(EstadoLaberinto::toString, ""));
    }

    private void recuperarCamino(TreeNode<EstadoLaberinto> nodo, ArrayList<Posicion> camino) {
        camino.add(nodo.getContent().getJugador().ctrlMovimiento().posicion());
        if (nodo.getParent() != null)
            recuperarCamino(nodo.getParent(), camino);
    }
}
