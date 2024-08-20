package es.adrianjg.nonopic.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import es.adrianjg.nonopic.AlmacenNiveles;
import es.adrianjg.nonopic.Nivel;
import es.adrianjg.nonopic.R;

/**
 * Fragment que muestra los niveles de 5x5
 */

public class Tab5x5 extends Fragment {
    private GridView gridview;
    public Tab5x5() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gridview, container, false);
        gridview = (GridView) view.findViewById(R.id.gridview);

        List<Nivel> niveles = AlmacenNiveles.obtenerListaNiveles5x5();
        Adaptador adaptador = new Adaptador(getActivity(), niveles);
        gridview.setAdapter(adaptador);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AndroidLauncher.class);
                intent.putExtra("tipo", 5);
                intent.putExtra("index", position);
                startActivityForResult(intent, 0);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            gridview.invalidateViews();
        }
    }


}
