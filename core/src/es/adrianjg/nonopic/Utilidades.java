package es.adrianjg.nonopic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.math.BigInteger;
import java.util.List;

/**
 * Clase con utilidades varias para NonoPic.
 */

public class Utilidades {
    /**
     * Almacena una imagen con el dibujo de un nivel en la ruta dada.
     *
     * @param nivel Nivel de juego del que se quiere almacenar su imagen
     * @param ruta  String con la ruta. Esta ruta es relativa a la interna que establece libGDX
     */
    public static void generarMiniatura(Nivel nivel, String ruta) {
        Pixmap pixmap = new Pixmap(180, 180, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(0, 0, 180, 180);
        pixmap.setColor(0, 0, 0, 1);
        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAlto(); x++) {
                if (nivel.getMapa()[y][x]) {
                    float size = 180 / nivel.getAncho();
                    pixmap.fillRectangle((int) (x * size), (int) (y * size), (int) size, (int) size);
                }
            }
        }

        PixmapIO.writePNG(Gdx.files.external(ruta), pixmap);
        pixmap.dispose();
    }

    /**
     * Serializa el mapa de un nivel en formato hexadecimal, cada columna separada por comas.
     * @param mapaNivel Mapa de un Nivel representado en un array booleano de dos dimensiones
     * @param alto Número de columnas de un nivel
     * @param ancho Número de filas de un nivel
     * @return String con la serialización del mapa del nivel
     */
    public static String obtenerCadenaNivel(boolean[][] mapaNivel, int alto, int ancho) {
        String cadenaNivel = "";

        for (int y = 0; y < alto; y++) {
            String bitsFila = "";
            for (int x = 0; x < ancho; x++) {
                if (mapaNivel[y][x]) {
                    bitsFila += "1";
                } else {
                    bitsFila += "0";
                }
            }

            int decimal = Integer.parseInt(bitsFila, 2);
            cadenaNivel += Integer.toString(decimal, 16);
            if (y < alto - 1) {
                cadenaNivel += ";";
            }
        }

        return cadenaNivel;

    }

    /**
     * Transforma un mapa de nivel serializado en un array de hexadecimales, cada elemento
     * representando una columna.
     * @param cadenaNivel String con la serialización del mapa del nivel
     * @param alto Número de columnas de un nivel
     * @param ancho Número de filas de un nivel
     * @return Mapa del nivel serializado en array de hexadecimales
     */
    public static String[] obtenerArrayBinDesdeCadena(String cadenaNivel, int alto, int ancho) {
        String[] filasBin = new String[alto];
        String filasHex[] = cadenaNivel.split(";");
        for (int i = 0; i < filasHex.length; i++) {
            filasBin[i] = String.format("%" + ancho + "s", new BigInteger(filasHex[i], 16).toString(2)).replace(" ", "0");
        }

        return filasBin;
    }

    /**
     * Deserializa el mapa del nivel a partir de un array de hexadecimales.
     * @param arrayBin Mapa del nivel serializado en array de hexadecimales
     * @param alto Número de columnas de un nivel
     * @param ancho Número de filas de un nivel
     * @return Mapa de un Nivel representado en un array booleano de dos dimensiones
     */
    public static boolean[][] obtenerMapaDesdeArrayBin(String[] arrayBin, int alto, int ancho) {
        boolean[][] mapa = new boolean[alto][ancho];
        for (int y = 0; y < arrayBin.length; y++) {
            for (int x = 0; x < ancho; x++) {
                if (arrayBin[y].charAt(x) == '1') {
                    mapa[y][x] = true;
                } else {
                    mapa[y][x] = false;
                }
            }
        }
        return mapa;
    }

    /**
     * Deserializa un mapa del nivel.
     * @param cadenaNivel String con la serialización del mapa del nivel
     * @param alto Número de columnas de un nivel
     * @param ancho Número de filas de un nivel
     * @return Mapa de un Nivel representado en un array booleano de dos dimensiones
     */
    public static boolean[][] obtenerMapaDesdeCadena(String cadenaNivel, int alto, int ancho) {
        String[] filasBin = obtenerArrayBinDesdeCadena(cadenaNivel, alto, ancho);
        boolean[][] mapa = obtenerMapaDesdeArrayBin(filasBin, alto, ancho);
        return mapa;
    }

    /**
     * Serializa una partida en curso.
     * @param nivel Nivel de la partida
     * @param casillas Estado de las casillas de la cuadrícula
     * @param puntuacion Puntuación de la partida
     * @return String con la serialización de la partida en curso
     */
    public static String guardarPartida(Nivel nivel, Casilla[][] casillas, Puntuacion puntuacion) {
        String guardado = nivel.getId() + "@";
        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAncho(); x++) {
                guardado += casillas[y][x].getEstado().ordinal();
            }
            if (y != nivel.getAncho() - 1) {
                guardado += ";";
            }
        }
        guardado += "@" + puntuacion.getMinutos() + "@" + puntuacion.getSegundos();
        return guardado;
    }

    /**
     * Carga el estado de una partida a partir de una serialización de la partida en curso.
     * @param guardado String con la serialización de la partida en curso
     * @param listaNiveles Lista completa de los niveles de NonoPic
     * @param puntuacion Objeto Puntuación al que se le devolverá el valor actualizado
     * @return El estado de las casillas de la cuadrícula
     */
    public static EstadosCasilla[][] cargarEstadoPartida(String guardado, List<Nivel> listaNiveles, Puntuacion puntuacion) {
        Nivel nivel = cargarNivelPartida(guardado, listaNiveles);
        String[] splitGuardado = guardado.split("@");
        String[] splitFilas = splitGuardado[1].split(";");
        EstadosCasilla[][] estados = new EstadosCasilla[nivel.getAlto()][nivel.getAncho()];

        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAncho(); x++) {
                estados[y][x] = EstadosCasilla.values()[Character.getNumericValue(splitFilas[y].charAt(x))];
            }
        }
        puntuacion.setMinutos(Integer.parseInt(splitGuardado[2]));
        puntuacion.setSegundos(Integer.parseInt(splitGuardado[3]));

        return estados;
    }

    /**
     * Devuelve el nivel que corresponde a la partida en curso.
     * @param guardado String con la serialización de la partida en curso
     * @param listaNiveles Lista completa de los niveles de NonoPic
     * @return Un nivel del juego
     */
    public static Nivel cargarNivelPartida(String guardado, List<Nivel> listaNiveles) {
        String[] splitGuardado = guardado.split("@");
        int idNivel = Integer.parseInt(splitGuardado[0]);
        Nivel nivel = new Nivel();
        for (Nivel aNivel : listaNiveles) {
            if (aNivel.getId() == idNivel) {
                nivel = aNivel;
                break;
            }
        }
        return nivel;
    }

    /**
     * Genera la fuente para las pistas numéricas.
     * @param size Tamaño en píxeles de la fuente
     * @return Objteto BitmapFont utilizable por NonoPicScene
     */
    public static BitmapFont generarFuenteGuia(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Assets.getFontPath(Assets.Fonts.visitor2)));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        font.setFixedWidthGlyphs("1234567890");
        return font;
    }

    /**
     * Genera la fuente para el resto de elementos del juego.
     * @param size Tamaño en píxeles de la fuente
     * @return Objteto BitmapFont utilizable por NonoPicScene
     */
    public static BitmapFont generarFuente(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Assets.getFontPath(Assets.Fonts.unbom)));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
