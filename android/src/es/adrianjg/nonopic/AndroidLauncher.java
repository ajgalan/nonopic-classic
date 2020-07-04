package es.adrianjg.nonopic;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.List;

/**
 * Actividad que inicia la escena del juego (NonoPicScene)
 */
public class AndroidLauncher extends AndroidApplication {
    private Puntuacion puntuacion;
    private Nivel nivel;
    private SqlHelperAndroid sqlHelperAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        sqlHelperAndroid = new SqlHelperAndroid(this);
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("guardado")) {
            String guardado = bundle.getString("guardado");
            List<Nivel> listaNiveles = AlmacenNiveles.obtenerTodosNiveles();
            nivel = Utilidades.cargarNivelPartida(guardado, listaNiveles);
            puntuacion = new Puntuacion(nivel.getId());
            EstadosCasilla[][] estados = Utilidades.cargarEstadoPartida(guardado, listaNiveles, puntuacion);
            initialize(new NonoPicScene(nivel, puntuacion, estados, sqlHelperAndroid), config);
        } else {
            int tipo = bundle.getInt("tipo");
            int index = bundle.getInt("index");

            switch (tipo) {
                case 5:
                    nivel = AlmacenNiveles.obtenerListaNiveles5x5().get(index);
                    break;
                case 10:
                    nivel = AlmacenNiveles.obtenerListaNiveles10x10().get(index);
                    break;
                case 15:
                    nivel = AlmacenNiveles.obtenerListaNiveles15x15().get(index);
                    break;
                case 20:
                    nivel = AlmacenNiveles.obtenerListaNiveles20x20().get(index);
                    break;
            }
            puntuacion = new Puntuacion(nivel.getId());

            initialize(new NonoPicScene(nivel, puntuacion, sqlHelperAndroid), config);
        }


    }

}
