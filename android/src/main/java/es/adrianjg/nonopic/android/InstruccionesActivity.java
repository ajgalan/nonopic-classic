package es.adrianjg.nonopic.android;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.adrianjg.nonopic.R;

/**
 * Actividad de las instrucciones del juego
 */

public class InstruccionesActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_instrucciones);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


    }

    /**
     * Fragmento que contiene una vista
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * El argumento del fragment representa el número de página del mismo.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_TOTAL_SECTION = "total_section_number";

        public PlaceholderFragment() {
        }

        /**
         * Devuelve una nueva instancia de este fragment dado el número de página.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int totalSection) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_TOTAL_SECTION, totalSection);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_instrucciones, container, false);

            ImageView img = (ImageView) rootView.findViewById(R.id.tutorial_img);
            int idImagen = getResources().getIdentifier("tutorial" + getArguments().getInt(ARG_SECTION_NUMBER), "drawable", getActivity().getPackageName());
            img.setImageResource(idImagen);

            TextView tutorialText = (TextView) rootView.findViewById(R.id.tutorial_text);
            int idTexto = getResources().getIdentifier("tutorial" + getArguments().getInt(ARG_SECTION_NUMBER), "string", getActivity().getPackageName());
            tutorialText.setText(getString(idTexto));

            TextView pageText = (TextView) rootView.findViewById(R.id.page_text);
            pageText.setText(getArguments().getInt(ARG_SECTION_NUMBER) + "/" + getArguments().getInt(ARG_TOTAL_SECTION));


            return rootView;
        }
    }

    /**
     * Un {@link FragmentPagerAdapter} que devuelve un frament correspondiente a una de las páginas.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, getCount());
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }
}
