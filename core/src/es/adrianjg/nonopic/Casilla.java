package es.adrianjg.nonopic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


/**
 * Clase que representa una casilla. Extiende de Rectangle para poder almacenar la posición X e Y.
 */

public class Casilla extends Rectangle {

    private static Texture casillaVacia;
    private static Texture casillaMarcada;
    private static Texture casillaCruz;
    private EstadosCasilla estado;

    /**
     * Contructor de Casilla. Se cargan las texturas de los estados de las casillas.
     */
    public Casilla() {
        super();
        estado = EstadosCasilla.Vacio;
        casillaVacia = Assets.getTexture(Assets.Textures.casillaVacia);
        casillaMarcada = Assets.getTexture(Assets.Textures.casillaMarcada);
        casillaCruz = Assets.getTexture(Assets.Textures.casillaCruz);
    }

    /**
     * Dependiendo del estado de la casilla, devuelve la textura correspondiente.
     *
     * @return Un objeto Texture correspondiente al estado
     */
    public Texture getTexture() {
        switch (estado) {
            case Vacio:
                return casillaVacia;
            case Marcado:
                return casillaMarcada;
            case Cruz:
                return casillaCruz;
        }

        return null;
    }

    /**
     * Elimina las texturas de la memoria.
     */
    public static void dispose() {
        casillaVacia.dispose();
        casillaMarcada.dispose();
        casillaCruz.dispose();
    }

    /**
     * Setter de la variable estado.
     * @param unEstado Enum de EstadosCasillas
     */
    public void setEstado(EstadosCasilla unEstado) {
        estado = unEstado;
    }

    /**
     * Getter de la variable estado.
     * @return Enum de EstadoCasillas
     */
    public EstadosCasilla getEstado() {
        return estado;
    }
}
