package es.adrianjg.nonopic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Clase que gestiona los assets del juego
 */
public class Assets {

    /**
     * Enum que identifica las diferentes texturas del juego
     */
    public enum Textures {
        botonAccion, botonCruz, casillaCruz, casillaMarcada, casillaVacia, dPadUp, dPadDown, dPadLeft, dPadRight, seleccion
    }

    /**
     * Enum que identifica las músicas del juego
     */
    public enum Music {
        music1, music2, music3, music4, music5, music6
    }

    /**
     * Enum que identifica los sonidos del juego
     */
    public enum Sounds {
        click, cruz, marcado, vacio, win
    }

    /**
     * Enum que identifica las fuentes del juego
     */
    public enum Fonts {
        unbom, visitor2
    }

    /**
     * Enum que identifica las texturas de fondo usados en el juego
     */
    public enum Backgrounds {
        fondo1, fondo2, fondo3, fondo4
    }

    /**
     * A partir de un enum Texture, devuelve el objeto Texture correspondiente.
     *
     * @param texture Enum de una textura
     * @return un objeto Texture
     */
    public static Texture getTexture(Textures texture) {
        return new Texture(Gdx.files.internal(getTexturePath(texture)));
    }

    /**
     * A partir de un enum Texture, devuelve la ruta de una textura.
     * @param texture Enum de una textura
     * @return String con la ruta de la textura
     */
    public static String getTexturePath(Textures texture) {
        switch (texture) {
            case botonAccion:
                return "botonaccion.png";
            case botonCruz:
                return "botoncruz.png";
            case casillaCruz:
                return "casillacruz.png";
            case casillaMarcada:
                return "casillamarcada.png";
            case casillaVacia:
                return "casillavacia.png";
            case dPadUp:
                return "up.png";
            case dPadDown:
                return "down.png";
            case dPadLeft:
                return "left.png";
            case dPadRight:
                return "right.png";
            case seleccion:
                return "seleccion.png";
        }
        return null;
    }

    /**
     * A partir de un enum Music, devuelve la ruta de una música.
     * @param music Enum de una música
     * @return String con la ruta de la música
     */
    public static String getMusicPath(Music music) {
        switch (music) {
            case music1:
                return "music/music1.ogg";
            case music2:
                return "music/music2.ogg";
            case music3:
                return "music/music3.ogg";
            case music4:
                return "music/music4.ogg";
            case music5:
                return "music/music5.ogg";
            case music6:
                return "music/music6.ogg";
        }
        return null;
    }

    /**
     * Obtiene la ruta de una música aleatoria.
     * @return String con la ruta del archivo de música
     */
    public static String getRandomMusicPath() {
        int i = MathUtils.random(Music.values().length - 1);
        return getMusicPath(Music.values()[i]);

    }

    /**
     * A partir de un enum Sound, devuelve la ruta de un sonido.
     * @param sound Enum de un sonido
     * @return String con la ruta del archivo de sonido
     */
    public static String getSoundPath(Sounds sound) {
        switch (sound) {
            case click:
                return "audio/click.mp3";
            case cruz:
                return "audio/cruz.mp3";
            case marcado:
                return "audio/marcado.mp3";
            case vacio:
                return "audio/vacio.mp3";
            case win:
                return "audio/win.ogg";
        }
        return null;
    }

    /**
     * A partir de un enum Font, devuelve la ruta de una fuente.
     * @param font Enum de una fuente
     * @return String con la ruta del archivo de fuente.
     */
    public static String getFontPath(Fonts font) {
        switch (font) {
            case unbom:
                return "fonts/unbom.ttf";
            case visitor2:
                return "fonts/visitor2.ttf";

        }
        return null;
    }

    /**
     * A partir de un enum Background, devuelve la ruta de una imagen de fondo.
     * @param background Enum de una imagen de fondo
     * @return String con la ruta de la imagen de fondo
     */
    public static String getBackgroundPath(Backgrounds background) {
        switch (background) {
            case fondo1:
                return "backgrounds/fondo1.png";
            case fondo2:
                return "backgrounds/fondo2.png";
            case fondo3:
                return "backgrounds/fondo3.png";
            case fondo4:
                return "backgrounds/fondo4.png";
        }
        return null;
    }

    /**
     * Obtiene una textura de una imagen de fondo aleatoria.
     * @return Objeto Texture de una imagen de fondo
     */
    public static Texture getRandomBackground() {
        int i = MathUtils.random(Backgrounds.values().length - 1);
        return getBackground(Backgrounds.values()[i]);
    }

    /**
     * Obtiene una textura a partir de un enum Background.
     * @param background Enum de una imagen de fondo
     * @return Objeto Texture de una imagen de fondo
     */
    public static Texture getBackground(Backgrounds background) {
        return new Texture(Gdx.files.internal(getBackgroundPath(background)));
    }

}
