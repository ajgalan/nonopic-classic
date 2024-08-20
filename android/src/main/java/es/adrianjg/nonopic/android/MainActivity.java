package es.adrianjg.nonopic.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import es.adrianjg.nonopic.R;

/**
 * Actividad principal de NonoPic
 */
public class MainActivity extends AppCompatActivity {

    public static final int TIEMPO_APARICION = 300;
    public static final int DURACION_ANIMACION = 1000;
    private boolean animando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus || animando) {
            return;
        }
        animar();
        super.onWindowFocusChanged(hasFocus);
    }
    private void animar() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-200 * getResources().getDisplayMetrics().density)
                .setStartDelay(TIEMPO_APARICION)
                .setDuration(DURACION_ANIMACION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((TIEMPO_APARICION * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((TIEMPO_APARICION * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    public void jugar(View v){
        Intent intent = new Intent(this, LevelSelectorActivity.class);
        startActivity(intent);
    }

    public void acercaDe(View v){
        Intent intent = new Intent(this, AcercaDeActivity.class);
        startActivity(intent);
    }

    public void instrucciones (View v){
        Intent intent = new Intent(this, InstruccionesActivity.class);
        startActivity(intent);
    }

}
