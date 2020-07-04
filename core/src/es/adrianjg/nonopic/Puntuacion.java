package es.adrianjg.nonopic;

/**
 * Clase que gestiona el tiempo transcurrido en una partida.
 */

public class Puntuacion {
    private int id;
    private int segundos;
    private int minutos;
    private boolean completado;

    /**
     * Constructor de Puntuacion.
     *
     * @param id Identificador del nivel
     */
    public Puntuacion(int id) {
        this.id = id;
        this.segundos = 0;
        this.minutos = 0;
        this.completado = false;
    }

    /**
     * Constructor completo de Puntuacion.
     * @param id Identificador del nivel
     * @param minutos Minutos transcurridos en la partida
     * @param segundos Segundos transcurridos en la partida
     * @param completado Indica si un nivel ha sido completado
     */
    public Puntuacion(int id, int minutos, int segundos, boolean completado) {
        this.id = id;
        this.segundos = segundos;
        this.minutos = minutos;
        this.completado = completado;
    }

    /**
     * Getter de id.
     * @return Identificador del nivel
     */
    public int getId() {
        return id;
    }

    /**
     * Setter de id.
     * @param id Identificador del nivel
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter de segundos.
     * @return Segundos transcurridos en la partida
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     * Setter de segundos.
     * @param segundos Segundos transcurridos en la partida
     */
    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    /**
     * Getter de minutos.
     * @return Minutos transcurridos en la partida
     */
    public int getMinutos() {
        return minutos;
    }

    /**
     * Setter de minutos.
     * @param minutos Minutos transcurridos en la partida
     */
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    /**
     * Getter de completado.
     * @return True si el nivel se ha completado. False si no.
     */
    public boolean isCompletado() {
        return completado;
    }

    /**
     * Getter de completado
     * @param completado True si el nivel se ha completado. False si no.
     */
    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
