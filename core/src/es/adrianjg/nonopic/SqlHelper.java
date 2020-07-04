package es.adrianjg.nonopic;

/**
 * Interfaz para manejar las bases de datos.
 */

public interface SqlHelper {
    /**
     * Almacena una puntuación en la base de datos que se le pasa por parámetro.
     *
     * @param puntuacion La puntuación a guardar
     */
    public void insertarPuntuacion(Puntuacion puntuacion);

    /**
     * Cierra la conexión la base de datos.
     */
    public void close();

    /**
     * A partir de un id de nivel, devuelve su puntuación.
     * @param id La identificación de un nivel
     * @return La puntuación de dicho nivel
     */
    public Puntuacion leerPuntuacion(int id);
}
