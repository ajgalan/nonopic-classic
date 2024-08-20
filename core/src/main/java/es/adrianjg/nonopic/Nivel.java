package es.adrianjg.nonopic;

/**
 * Clase que representa un nivel de un puzle de NonoPic.
 */

public class Nivel {
    private int id;
    private String nombre;
    private int alto;
    private int ancho;
    private boolean[][] mapa;
    private int[][] guiaAlto;
    private int[][] guiaAncho;

    /**
     * Constructor del Nivel.
     *
     * @param id     Número de identificación del nivel
     * @param nombre Nombre dado al nivel
     * @param alto   Número de casillas que tiene una columna
     * @param ancho  Número de casillas que tiene una fila
     * @param mapa   Objeto formado por un array de booleanos que representa el dibujo del nivel.
     *               El valor true equivale a una casilla marcada y el valor false a una vacía.
     */
    public Nivel(int id, String nombre, int alto, int ancho, boolean[][] mapa) {
        this.id = id;
        this.nombre = nombre;
        this.alto = alto;
        this.ancho = ancho;
        this.mapa = mapa;
        guiaAlto = new int[alto][10];
        guiaAncho = new int[ancho][10];
        calculaGuia();
    }

    /**
     * Constructor no implementado.
     */
    public Nivel() {
    }


    /**
     * Calcula las guías numéricas y las almacena en guiaAlto y guiaAncho.
     */
    private void calculaGuia() {
        for (int y = 0; y < alto; y++) {
            int contador = 0;
            int n = 0;
            for (int x = 0; x < ancho; x++) {
                if (this.mapa[y][x]) {
                    contador++;
                    if (x == ancho - 1) {
                        guiaAlto[alto - 1 - y][n] = contador;
                    }
                } else {
                    if (contador != 0) {
                        guiaAlto[alto - 1 - y][n] = contador;
                        n++;
                        contador = 0;
                    }
                }
            }
        }
        for (int x = 0; x < ancho; x++) {
            int contador = 0;
            int n = 0;
            for (int y = 0; y < alto; y++) {
                if (this.mapa[y][x]) {
                    contador++;
                    if (y == alto - 1) {
                        guiaAncho[x][n] = contador;
                    }
                } else {
                    if (contador != 0) {
                        guiaAncho[x][n] = contador;
                        n++;
                        contador = 0;
                    }
                }
            }
        }
    }

    /**
     * Getter de la variable guiaAncho.
     * @return Un Array de booleanos de dos dimensiones con las pistas de las columnas
     */
    public int[][] getGuiaAncho() {
        return guiaAncho;
    }

    /**
     * Getter de la variable guiaAlto.
     * @return Un Array de booleanos de dos dimensiones con las pistas de las filas
     */
    public int[][] getGuiaAlto() {
        return guiaAlto;
    }

    /**
     * Getter de la variable mapa.
     * @return Un array de booleanos que representa el dibujo del nivel.
     */
    public boolean[][] getMapa() {
        return mapa;
    }

    /**
     * Getter de la variable alto.
     * @return Un integer con el número de casillas que tiene una columna
     */
    public int getAlto() {
        return alto;
    }

    /**
     * Getter de la variable ancho.
     * @return Un integer con el número de casillas que tiene una fila
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * Getter de la variable id.
     * @return Un integer que identifica el nivel
     */
    public int getId() {
        return id;
    }

    /**
     * Getter de la variable nombre.
     * @return Una cadena de texto con el nombre dado al nivel
     */
    public String getNombre() {
        return nombre;
    }

}
