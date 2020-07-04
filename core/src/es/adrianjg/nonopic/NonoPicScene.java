package es.adrianjg.nonopic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static es.adrianjg.nonopic.Utilidades.generarFuente;
import static es.adrianjg.nonopic.Utilidades.generarFuenteGuia;


/**
 * Created by adrip on 06/05/2017.
 */

public class NonoPicScene implements ApplicationListener {
    private SpriteBatch batch;
    private Texture seleccion;
    private Casilla[][] casillas;
    private Rectangle posicion;
    private ShapeRenderer sRenderer;
    private OrthographicCamera camera;
    private Stage interfazJuego;
    private Stage interfazPausa;
    private Viewport viewport;
    private Nivel nivel;
    private BitmapFont fuenteGuia;
    private BitmapFont fuenteTiempo;
    private BitmapFont fuenteVictoria;
    private Music music;
    private Sound click;
    private Sound soundMarcado;
    private Sound soundVacio;
    private Sound soundCruz;
    private Sound win;
    private float sizeCasilla;
    private Puntuacion puntuacion;
    private Texture fondo;
    private boolean mantenerLeft;
    private boolean mantenerRight;
    private boolean mantenerUp;
    private boolean mantenerDown;
    private float delta;
    private EstadosJuego estado;
    private SqlHelper sqlHelper;
    private Preferences preferences;
    private EstadosCasilla[][] estadosCasillaReanudar;

    /**
     * Constructor para la inicialización de un nivel desde cero.
     *
     * @param nivel      Objeto con el nivel que se va a jugar
     * @param puntuacion Objeto que contiene el tiempo transcurrido
     * @param sqlHelper  Manejador de la base de datos que extiende de la interfaz SqlHelper
     */
    public NonoPicScene(Nivel nivel, Puntuacion puntuacion, SqlHelper sqlHelper) {
        this.nivel = nivel;
        this.puntuacion = puntuacion;
        this.sqlHelper = sqlHelper;
        estadosCasillaReanudar = null;
    }

    /**
     * Constructor para la inicialización de un nivel reanudado.
     * @param nivel Objeto con el nivel que se va a jugar
     * @param puntuacion Objeto que contiene el tiempo transcurrido
     * @param estadosCasillaReanudar Un array de dos dimensiones de EstadosCasilla con el estado
     *                               de un nivel a reanudar.
     * @param sqlHelper Manejador de la base de datos que extiende de la interfaz SqlHelper
     */
    public NonoPicScene(Nivel nivel, Puntuacion puntuacion, EstadosCasilla[][] estadosCasillaReanudar, SqlHelper sqlHelper) {
        this.nivel = nivel;
        this.puntuacion = puntuacion;
        this.sqlHelper = sqlHelper;
        this.estadosCasillaReanudar = estadosCasillaReanudar;
    }

    /**
     * Constructor no implementado.
     */
    public NonoPicScene() {
    }

    /**
     * Inicializa los componentes del juego cuando se crea la escena.
     */
    @Override
    public void create() {
        estado = EstadosJuego.Jugando;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 540, 960);
        viewport = new FillViewport(540, 960, camera);
        sizeCasilla = 320 / nivel.getAlto();
        interfazJuego = new Stage(viewport);
        interfazPausa = new Stage(viewport);
        seleccion = Assets.getTexture(Assets.Textures.seleccion);
        fuenteGuia = generarFuenteGuia((int) sizeCasilla);
        fuenteTiempo = generarFuente(28);
        fuenteVictoria = generarFuente(32);
        fuenteVictoria.setColor(1, 1, 1, 0);
        crearUIJuego(interfazJuego);
        crearUIPausa(interfazPausa);
        batch = new SpriteBatch();
        posicion = new Rectangle();
        sRenderer = new ShapeRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.getRandomMusicPath()));
        music.setLooping(true);
        music.setVolume(0.7f);

        click = Gdx.audio.newSound(Gdx.files.internal(Assets.getSoundPath(Assets.Sounds.click)));
        soundMarcado = Gdx.audio.newSound(Gdx.files.internal(Assets.getSoundPath(Assets.Sounds.marcado)));
        soundVacio = Gdx.audio.newSound(Gdx.files.internal(Assets.getSoundPath(Assets.Sounds.vacio)));
        soundCruz = Gdx.audio.newSound(Gdx.files.internal(Assets.getSoundPath(Assets.Sounds.cruz)));
        win = Gdx.audio.newSound(Gdx.files.internal(Assets.getSoundPath(Assets.Sounds.win)));
        fondo = Assets.getRandomBackground();
        Gdx.input.setCatchBackKey(true);
        casillas = inicializaCasillas();
        inicializarControles(interfazJuego, interfazPausa);
        preferences = Gdx.app.getPreferences("PartidaSalvada");
        music.play();
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               aumentarTiempo();
                           }
                       }, 1, 1
        );
    }

    /**
     * Método encargado de aumentar el tiempo de la puntuación si el estado del juego es Jugando.
     * Es llamado por un Timer cada segundo.
     */
    private void aumentarTiempo() {
        if (estado == estado.Jugando) {
            if (puntuacion.getSegundos() > 58 && puntuacion.getMinutos() < 99) {
                puntuacion.setSegundos(0);
                puntuacion.setMinutos(puntuacion.getMinutos() + 1);
            } else {
                puntuacion.setSegundos(puntuacion.getSegundos() + 1);
            }
        }

    }

    /**
     * Crea la interfaz del juego, la cual se compone de los botones de control y el botón de pausa.
     * @param interfazJuego
     */
    private void crearUIJuego(Stage interfazJuego) {
        TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.dPadUp)));
        TextureRegionDrawable down = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.dPadDown)));
        TextureRegionDrawable left = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.dPadLeft)));
        TextureRegionDrawable right = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.dPadRight)));
        TextureRegionDrawable accion = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.botonAccion)));
        TextureRegionDrawable cruz = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.Textures.botonCruz)));

        final ImageButton bUp = new ImageButton(up);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bUp.setWidth(96);
            bUp.setHeight(96);
            bUp.setPosition(100, 190);
        } else {
            bUp.setWidth(64);
            bUp.setHeight(64);
            bUp.setPosition(100, 240);
        }


        bUp.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                delta = -1.0f;
                Gdx.graphics.setContinuousRendering(true);
                botonUp();
                mantenerUp = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.graphics.setContinuousRendering(false);
                delta = 0;
                mantenerUp = false;
            }
        });

        final ImageButton bDown = new ImageButton(down);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bDown.setWidth(96);
            bDown.setHeight(96);
            bDown.setPosition(100, 62);
        } else {
            bDown.setWidth(64);
            bDown.setHeight(64);
            bDown.setPosition(100, 144);
        }

        bDown.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                delta = -1.0f;
                Gdx.graphics.setContinuousRendering(true);
                botonDown();
                mantenerDown = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                delta = 0;
                Gdx.graphics.setContinuousRendering(false);
                mantenerDown = false;
            }
        });

        final ImageButton bLeft = new ImageButton(left);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bLeft.setWidth(96);
            bLeft.setHeight(96);
            bLeft.setPosition(38, 126);
        } else {
            bLeft.setWidth(64);
            bLeft.setHeight(64);
            bLeft.setPosition(54, 192);
        }
        bLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                delta = -1.0f;
                Gdx.graphics.setContinuousRendering(true);
                botonLeft();
                mantenerLeft = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.graphics.setContinuousRendering(false);
                delta = 0;
                mantenerLeft = false;
            }
        });

        final ImageButton bRight = new ImageButton(right);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bRight.setWidth(96);
            bRight.setHeight(96);
            bRight.setPosition(162, 126);
        } else {
            bRight.setWidth(64);
            bRight.setHeight(64);
            bRight.setPosition(146, 192);
        }

        bRight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                delta = -1.0f;
                Gdx.graphics.setContinuousRendering(true);
                botonRight();
                mantenerRight = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.graphics.setContinuousRendering(false);
                delta = 0;
                mantenerRight = false;
            }
        });

        final ImageButton bAccion = new ImageButton(accion);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bAccion.setWidth(128);
            bAccion.setHeight(128);
            bAccion.setPosition(400, 150);
        } else {
            bAccion.setWidth(96);
            bAccion.setHeight(96);
            bAccion.setPosition(400, 182);
        }

        bAccion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                botonAccion();
            }
        });

        final ImageButton bCruz = new ImageButton(cruz);
        if ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight() != 0.75) {
            bCruz.setWidth(128);
            bCruz.setHeight(128);
            bCruz.setPosition(290, 64);
        } else {
            bCruz.setWidth(96);
            bCruz.setHeight(96);
            bCruz.setPosition(290, 128);
        }
        bCruz.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                botonCruz();
            }
        });
        Skin skinBPausa = new Skin(Gdx.files.internal("uiskin.json"));
        final TextButton bPausa = new TextButton("PAUSE", skinBPausa);
        bPausa.setWidth(160);
        bPausa.setHeight(30);
        bPausa.setPosition(358, 815);
        bPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                botonPausa();
            }
        });

        interfazJuego.addActor(bUp);
        interfazJuego.addActor(bDown);
        interfazJuego.addActor(bLeft);
        interfazJuego.addActor(bRight);
        interfazJuego.addActor(bAccion);
        interfazJuego.addActor(bCruz);
        interfazJuego.addActor(bPausa);

    }

    /**
     * Crea la interfaz que se muestra cuando el juego se encuentra en pausa.
     * @param interfazPausa
     */
    private void crearUIPausa(Stage interfazPausa) {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        final TextButton bContinuar = new TextButton("CONTINUE", skin);
        bContinuar.setWidth(300);
        bContinuar.setHeight(100);
        bContinuar.setPosition(540 / 2 - bContinuar.getWidth() / 2, 960 / 2 - bContinuar.getHeight() / 2 + 120);
        bContinuar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (estado == EstadosJuego.Pausado) {
                    estado = EstadosJuego.Jugando;
                }

            }
        });
        interfazPausa.addActor(bContinuar);

        final TextButton bReset = new TextButton("RESET", skin);
        bReset.setWidth(300);
        bReset.setHeight(100);
        bReset.setPosition(540 / 2 - bContinuar.getWidth() / 2, 960 / 2 - bContinuar.getHeight() / 2);
        bReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (estado == EstadosJuego.Pausado) {
                    posicion.x = 0;
                    posicion.y = 0;
                    for (int alto = 0; alto < nivel.getAlto(); alto++) {
                        for (int ancho = 0; ancho < nivel.getAncho(); ancho++) {
                            casillas[alto][ancho].setEstado(EstadosCasilla.Vacio);
                        }
                    }
                    puntuacion.setSegundos(0);
                    puntuacion.setMinutos(0);
                    estado = EstadosJuego.Jugando;
                }
            }
        });
        interfazPausa.addActor(bReset);

        final TextButton bSalir = new TextButton("EXIT", skin);
        bSalir.setWidth(300);
        bSalir.setHeight(100);
        bSalir.setPosition(540 / 2 - bContinuar.getWidth() / 2, 960 / 2 - bContinuar.getHeight() / 2 - 120);
        bSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (estado == EstadosJuego.Pausado) {
                    Gdx.app.exit();
                }
            }
        });
        interfazPausa.addActor(bSalir);
    }

    /**
     * Comprueba si se han cumplido los requerimientos para completar un nivel.
     * @return Un valor booleano que indica si se cumple la condición de Victoria
     */
    private boolean comprobarVictoria() {

        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAncho(); x++) {
                if (nivel.getMapa()[y][x] && casillas[y][x].getEstado() != EstadosCasilla.Marcado) {
                    return false;
                } else if (!nivel.getMapa()[y][x] && casillas[y][x].getEstado() == EstadosCasilla.Marcado) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Devuelve las casillas inicializadas para usarse en la cuadrícula del juego, con un tamaño y
     * posición adecuados a las dimensiones del nivel. En el caso de haberse pasado unas casillas
     * para reanudar, se copian sus valores.
     * @return el array de dos dimensiones de Casilla que representa la cuadrícula
     */
    private Casilla[][] inicializaCasillas() {
        casillas = new Casilla[nivel.getAlto()][nivel.getAncho()];
        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAncho(); x++) {
                Casilla casilla = new Casilla();
                casilla.x = 180 + 20 + x * (int) sizeCasilla;
                casilla.y = 280 + 32 + y * (int) sizeCasilla;
                if (estadosCasillaReanudar != null) {
                    casilla.setEstado(estadosCasillaReanudar[nivel.getAlto() - 1 - y][x]);
                }
                casillas[nivel.getAlto() - 1 - y][x] = casilla;
            }
        }
        return casillas;
    }

    /**
     * Método al que se llama cuando se redimensiona la ventana.
     * @param width Anchura de la ventana
     * @param height Altura de la ventana
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Método encargado del renderizado gráfico del juego.
     */
    @Override
    public void render() {
        gl.glClearColor(0.2f, 0.3f, 0.7f, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(fondo, 0, 0);
        batch.end();

        if (estado == EstadosJuego.Pausado) {
            interfazPausa.draw();

        } else {
            if (mantenerDown) {
                delta += graphics.getDeltaTime();
                if (delta > 0.1f) {
                    botonDown();
                    delta = 0;
                }
            } else if (mantenerUp) {
                delta += graphics.getDeltaTime();
                if (delta > 0.1f) {
                    botonUp();
                    delta = 0;
                }

            } else if (mantenerLeft) {
                delta += graphics.getDeltaTime();
                if (delta > 0.1f) {
                    botonLeft();
                    delta = 0;
                }
            } else if (mantenerRight) {
                delta += graphics.getDeltaTime();
                if (delta > 0.1f) {
                    botonRight();
                    delta = 0;
                }
            }


            sRenderer.begin(ShapeRenderer.ShapeType.Filled);
            gl.glEnable(GL20.GL_BLEND);
            gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sRenderer.setColor(1, 1, 1, 0.2f);
            if (nivel.getAlto() == 15) {
                sRenderer.rect(179 + 20, 280 + 31, 317, 318);
            } else {
                sRenderer.rect(179 + 20, 280 + 31, 321, 321);
            }

            sRenderer.end();

            batch.begin();
            for (Casilla[] fila : casillas) {
                for (Casilla casilla : fila) {
                    batch.draw(casilla.getTexture(), casilla.x, casilla.y, sizeCasilla, sizeCasilla);
                }
            }
            if (estado == EstadosJuego.Jugando) {
                batch.draw(seleccion, 180 + 20 + posicion.x * (int) sizeCasilla, 280 + 32 + (nivel.getAlto() - 1 - posicion.y) * (int) sizeCasilla, sizeCasilla, sizeCasilla);
            }

            fuenteTiempo.draw(batch, String.format("TIME: %02d:%02d", puntuacion.getMinutos(), puntuacion.getSegundos()), 8 + 20, 840);

            batch.end();

            interfazJuego.draw();

            sRenderer.begin(ShapeRenderer.ShapeType.Line);
            sRenderer.setProjectionMatrix(camera.combined);
            sRenderer.setColor(0, 0, 0, 1);
            if (nivel.getAlto() == 15) {
                sRenderer.rect(180 + 20, 280 + 32, 316, 316);
            } else {
                sRenderer.rect(180 + 20, 280 + 32, 321, 321);
            }


            sRenderer.end();
            sRenderer.begin(ShapeRenderer.ShapeType.Filled);
            gl.glEnable(GL20.GL_BLEND);
            gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sRenderer.setColor(0, 0, 0, 0.1f);
            sRenderer.rect(19, 280 + 31, 180, 322);
            if (nivel.getAlto() == 15) {
                sRenderer.rect(178 + 20, 280 + 31 + 317, 317, 185);
            } else {
                sRenderer.rect(178 + 20, 280 + 31 + 322, 322, 180);
            }

            for (int i = 0; i < casillas.length; i++) {

                if (i % 2 == 0) {
                    sRenderer.rect(19, 280 + 31 + ((int) sizeCasilla * i), 180, (int) sizeCasilla);
                    if (nivel.getAlto() == 15) {
                        sRenderer.rect(179 + 20 + ((int) sizeCasilla * i), 280 + 31 + 317, (int) sizeCasilla, 184);
                    } else {
                        sRenderer.rect(179 + 20 + ((int) sizeCasilla * i), 280 + 31 + 322, (int) sizeCasilla, 180);
                    }

                }
            }
            if (estado == EstadosJuego.Jugando) {
                sRenderer.setColor(0, 0, 0, 1);
                sRenderer.rect(19, 280 + 31 + ((int) sizeCasilla * (nivel.getAlto() - 1 - posicion.y)), 180, (int) sizeCasilla);
                if (nivel.getAlto() == 15) {
                    sRenderer.rect(179 + 20 + ((int) sizeCasilla * posicion.x), 280 + 31 + 317, (int) sizeCasilla, 184);
                } else {
                    sRenderer.rect(179 + 20 + ((int) sizeCasilla * posicion.x), 280 + 31 + 322, (int) sizeCasilla, 180);
                }
            }
            sRenderer.setColor(1, 1, 1, 1);
            sRenderer.rect(-1 + 20, 280 + 31 + 322, 180, 180);
            sRenderer.setColor(0, 0, 0, 1);
            for (int y = 0; y < nivel.getAlto(); y++) {
                for (int x = 0; x < nivel.getAlto(); x++) {
                    if (casillas[y][x].getEstado() == EstadosCasilla.Marcado) {
                        float size = 180 / nivel.getAncho();
                        sRenderer.rect(-1 + 20 + (x * size), 280 + 31 - size + 322 + 180 - (y * size), size, size);
                    }
                }
            }

            sRenderer.end();
            batch.begin();
            for (int i = 0; i < nivel.getAlto(); i++) {
                int contador = 1;
                int extra = 0;
                for (int y = 0; y < 10; y++) {
                    int num = nivel.getGuiaAlto()[i][9 - y];
                    if (num > 9) {
                        fuenteGuia.draw(batch, Integer.toString(num), 200 - (int) (contador++ * sizeCasilla / 1.5) - 10, 312 + sizeCasilla - (sizeCasilla / 3) + sizeCasilla * i);
                        extra += (int) (sizeCasilla / 1.5);
                    } else if (y == 9 || num != 0) {
                        fuenteGuia.draw(batch, Integer.toString(num), 200 - (int) (contador++ * sizeCasilla / 1.5) - extra, 312 + sizeCasilla - (sizeCasilla / 3) + sizeCasilla * i);
                    }

                }
            }

            for (int i = 0; i < nivel.getAncho(); i++) {
                int contador = 1;

                for (int y = 0; y < 10; y++) {
                    int num = nivel.getGuiaAncho()[i][9 - y];
                    if (num > 9) {
                        fuenteGuia.draw(batch, Integer.toString(num), 200 + (sizeCasilla * i), 634 + (int) (contador++ * sizeCasilla / 1.5));

                    } else if (y == 9 || num != 0) {
                        fuenteGuia.draw(batch, Integer.toString(num), 200 + (sizeCasilla / 4) + (sizeCasilla * i), 634 + (int) (contador++ * sizeCasilla / 1.5));
                    }
                }
            }


            if (estado == EstadosJuego.Completado) {
                if (music.getVolume() > 0) {
                    music.setVolume(music.getVolume() - 0.01f);
                }
                boolean noVisible = true;
                //for (Actor actor : interfazJuego.getActors()) {
                for (int i = 0; i < interfazJuego.getActors().size - 1; i++) {
                    Actor actor = interfazJuego.getActors().get(i);
                    if (actor.getY() > -200) {
                        noVisible = false;
                        actor.setPosition(actor.getX(), actor.getY() - 250 * Gdx.graphics.getDeltaTime());
                    }
                }
                if (noVisible) {
                    if (fuenteVictoria.getColor().a < 1) {
                        fuenteVictoria.setColor(1, 1, 1, fuenteVictoria.getColor().a + Gdx.graphics.getDeltaTime() / 2);
                    } else {
                        Gdx.graphics.setContinuousRendering(false);
                        if (Gdx.input.isTouched()) {
                            Gdx.app.exit();
                        }
                    }
                    fuenteVictoria.draw(batch, "LEVEL COMPLETED!\nTOUCH TO CONTINUE", 0, 200, 540, Align.center, true);
                }
            }
            batch.end();
        }
    }

    /**
     * Método al que se llama cuando la aplicación pierde el foco.
     * Se almacena el estado de la partida.
     */
    @Override
    public void pause() {
        if (estado != EstadosJuego.Completado) {
            preferences.putString("partidasalvada", Utilidades.guardarPartida(nivel, casillas, puntuacion));
            preferences.flush();
        }
    }

    /**
     * Método al que se llama cuando vuelve desde un estado de pausa.
     */
    @Override
    public void resume() {

    }

    /**
     * Método al que se llama cuando la aplicación se cierra. Se utiliza para liberar recursos.
     */
    @Override
    public void dispose() {
        batch.dispose();
        Casilla.dispose();
        sRenderer.dispose();
        interfazJuego.dispose();
        interfazPausa.dispose();
        fuenteGuia.dispose();
        fuenteTiempo.dispose();
        seleccion.dispose();
        music.dispose();
        click.dispose();
        soundVacio.dispose();
        soundMarcado.dispose();
        soundCruz.dispose();
        win.dispose();
    }

    /**
     * Método que inicializa los controles del juego
     * @param interfazJuego Escenario con la interfaz de juego.
     * @param interfazPausa Escenario con la interfaz del menú de pausa.
     */
    private void inicializarControles(Stage interfazJuego, Stage interfazPausa) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(interfazJuego);
        multiplexer.addProcessor(interfazPausa);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.LEFT:
                        delta = -0.5f;
                        botonLeft();
                        mantenerLeft = true;
                        break;
                    case Input.Keys.RIGHT:
                        delta = -0.5f;
                        botonRight();
                        mantenerRight = true;
                        break;
                    case Input.Keys.UP:
                        delta = -0.5f;
                        botonUp();
                        mantenerUp = true;
                        break;
                    case Input.Keys.DOWN:
                        delta = -0.5f;
                        botonDown();
                        mantenerDown = true;
                        break;
                    case Input.Keys.SPACE:
                        botonAccion();
                        break;
                    case Input.Keys.X:
                        botonCruz();
                        break;
                    case Input.Keys.BACK:
                        botonPausa();
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.LEFT:
                        mantenerLeft = false;
                        break;
                    case Input.Keys.RIGHT:
                        mantenerRight = false;
                        break;
                    case Input.Keys.UP:
                        mantenerUp = false;
                        break;
                    case Input.Keys.DOWN:
                        mantenerDown = false;
                        break;
                }
                return true;
            }


        });
        Gdx.input.setInputProcessor(multiplexer);

    }

    /**
     * Define las acciones del botón de dirección Arriba
     */
    private void botonUp() {
        if (estado == EstadosJuego.Jugando && posicion.y > 0) {
            posicion.y--;
            click.play();
        }
    }

    /**
     * Define las acciones del botón de dirección Abajo
     */
    private void botonDown() {
        if (estado == EstadosJuego.Jugando && posicion.y < nivel.getAlto() - 1) {
            posicion.y++;
            click.play();
        }
    }

    /**
     * Define las acciones del botón de dirección Izquierda
     */
    private void botonLeft() {
        if (estado == EstadosJuego.Jugando && posicion.x > 0) {
            posicion.x--;
            click.play();
        }
    }

    /**
     * Define las acciones del botón de dirección Derecha
     */
    private void botonRight() {
        if (estado == EstadosJuego.Jugando && posicion.x < nivel.getAncho() - 1) {
            posicion.x++;
            click.play();
        }
    }

    /**
     * Define las acciones del botón de acción
     */
    private void botonAccion() {
        if (estado == EstadosJuego.Jugando) {
            Casilla marcada = casillas[(int) posicion.y][(int) posicion.x];
            switch (marcada.getEstado()) {
                case Vacio:
                    marcada.setEstado(EstadosCasilla.Marcado);
                    soundMarcado.play();
                    break;
                case Marcado:
                    marcada.setEstado(EstadosCasilla.Vacio);
                    soundVacio.play();
                    break;
                case Cruz:
                    marcada.setEstado(EstadosCasilla.Vacio);
                    soundVacio.play();
                    break;
            }
            if (estado == EstadosJuego.Jugando && comprobarVictoria()) {
                completarJuego();
            }
        }

    }

    /**
     * Define las acciones del botón de cruz
     */
    private void botonCruz() {
        if (estado == EstadosJuego.Jugando) {
            Casilla marcada = casillas[(int) posicion.y][(int) posicion.x];
            switch (marcada.getEstado()) {
                case Vacio:
                    marcada.setEstado(EstadosCasilla.Cruz);
                    soundCruz.play();
                    break;
                case Marcado:
                    marcada.setEstado(EstadosCasilla.Vacio);
                    soundVacio.play();
                    break;
                case Cruz:
                    marcada.setEstado(EstadosCasilla.Vacio);
                    soundVacio.play();
                    break;
            }
        }
    }

    /**
     * Define las acciones del botón de pausa
     */
    private void botonPausa() {
        if (estado == EstadosJuego.Jugando) {
            estado = EstadosJuego.Pausado;
        }
    }

    /**
     * Se le llama cuando se han cumplido las condiciones de victoria. Cambia el estado del juego,
     * elimina la partida pendiente y limpia las cruces de la cuadrícula.
     */
    private void completarJuego() {
        Gdx.graphics.setContinuousRendering(true);
        win.play();
        estado = EstadosJuego.Completado;
        puntuacion.setCompletado(true);
        sqlHelper.insertarPuntuacion(puntuacion);
        sqlHelper.close();
        preferences.putString("partidasalvada", "none");
        preferences.flush();
        for (int y = 0; y < nivel.getAlto(); y++) {
            for (int x = 0; x < nivel.getAncho(); x++) {
                if (casillas[y][x].getEstado() == EstadosCasilla.Cruz) {
                    casillas[y][x].setEstado(EstadosCasilla.Vacio);
                }
            }
        }
    }

}
