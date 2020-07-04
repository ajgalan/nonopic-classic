package es.adrianjg.nonopic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import es.adrianjg.nonopic.AlmacenNiveles;
import es.adrianjg.nonopic.Nivel;

/**
 * Clase sin finalizar para la inicialización de NonoPic en PC
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 960;
		config.width = 540;
		Nivel nivel = AlmacenNiveles.obtenerListaNiveles10x10().get(6);
		//new LwjglApplication(new NonoPicScene(nivel, new Puntuacion(nivel.getId()), config);
	}
}
