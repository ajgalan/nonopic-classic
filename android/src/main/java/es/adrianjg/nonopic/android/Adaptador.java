package es.adrianjg.nonopic.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.adrianjg.nonopic.Nivel;
import es.adrianjg.nonopic.Puntuacion;
import es.adrianjg.nonopic.R;

/**
 * Adaptador que infla el diseño de los elementos del GridView del selector de niveles
 */

public class Adaptador extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Nivel> niveles;
    private Context context;
    private SqlHelperAndroid sqlHelperAndroid;

    public Adaptador(Context context, List<Nivel> niveles) {
        this.context = context;
        this.niveles = niveles;
        sqlHelperAndroid = new SqlHelperAndroid(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return niveles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = inflater.inflate(R.layout.grid_element, parent, false);
        }
        ImageView preview = (ImageView) view.findViewById(R.id.preview);
        TextView nombre = (TextView) view.findViewById(R.id.nombre_nivel);
        TextView tiempo = (TextView) view.findViewById(R.id.tiempo_nivel);

        nombre.setText(niveles.get(position).getNombre());
        Puntuacion puntuacion = sqlHelperAndroid.leerPuntuacion(niveles.get(position).getId());
        if (puntuacion.isCompletado()) {
            tiempo.setText(String.format("Tiempo: %02d:%02d", puntuacion.getMinutos(), puntuacion.getSegundos()));
            int idImagen = context.getResources().getIdentifier("n" + niveles.get(position).getId(), "drawable", context.getPackageName());
            preview.setImageResource(idImagen);
        } else {
            tiempo.setText("Tiempo: --:--");
            preview.setImageResource(R.drawable.unknown);
        }

        sqlHelperAndroid.close();
        return view;
    }

}
