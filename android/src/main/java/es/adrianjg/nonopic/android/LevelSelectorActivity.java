package es.adrianjg.nonopic.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import es.adrianjg.nonopic.R;

/**
 * Actividad del selector de niveles
 */
public class LevelSelectorActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private FragmentTabHost tabHost;
    private ViewPager viewPager;
    private TabsPagerAdapter pAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selector);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pAdapter);
        viewPager.addOnPageChangeListener(this);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("5x5"),
                Tab5x5.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("10x10"),
                Tab10x10.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("15x15"),
                Tab15x15.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("20x20"),
                Tab20x20.class, null);
        tabHost.setOnTabChangedListener(this);
        mostrarAlertaReanudar(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int i = viewPager.getCurrentItem();
        this.tabHost.setCurrentTab(i);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String s) {
        int i = tabHost.getCurrentTab();
        this.viewPager.setCurrentItem(i);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0:
                    return new Tab5x5();
                case 1:
                    return new Tab10x10();
                case 2:
                    return new Tab15x15();
                case 3:
                    return new Tab20x20();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void mostrarAlertaReanudar(final Context context) {
        SharedPreferences prefs = getSharedPreferences("PartidaSalvada", Context.MODE_PRIVATE);
        final String guardado = prefs.getString("partidasalvada", "none");
        if (!guardado.equals("none")) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle(R.string.reanudar)
                    .setMessage(R.string.reanudar_detalle)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, AndroidLauncher.class);
                            intent.putExtra("guardado", guardado);
                            startActivityForResult(intent, 0);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

    }


}
