package es.adrianjg.nonopic.android;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
 * Fragment que muestra los niveles de 20x20
 */
public class Tab20x20 extends Fragment {
    private GridView gridview;

    public Tab20x20() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gridview, container, false);
        gridview = (GridView) view.findViewById(R.id.gridview);

        List<Nivel> niveles = AlmacenNiveles.obtenerListaNiveles20x20();
        Adaptador adaptador = new Adaptador(getActivity(), niveles);
        gridview.setAdapter(adaptador);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AndroidLauncher.class);
                intent.putExtra("tipo", 20);
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
