package com.jonda.munidenunciasapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonda.munidenunciasapp.R;
import com.jonda.munidenunciasapp.models.Denuncia;
import com.jonda.munidenunciasapp.service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DenunciasAdapter extends RecyclerView.Adapter<DenunciasAdapter.ViewHolder> {

    private static final String TAG = DenunciasAdapter.class.getSimpleName();

    private List<Denuncia> denuncias;

    public DenunciasAdapter(){
        this.denuncias = new ArrayList<>();
    }

    public void setDenuncias(List<Denuncia> denuncias){
        this.denuncias = denuncias;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView fotoImage;
        public TextView tituloText;
        public TextView descripcionText;
        public TextView ubicacionText;

        public ViewHolder(View itemView) {
            super(itemView);

            fotoImage = (ImageView) itemView.findViewById(R.id.foto_image);
            tituloText = (TextView) itemView.findViewById(R.id.titulo_text);
            descripcionText = (TextView) itemView.findViewById(R.id.descripcion_txt);
            ubicacionText = (TextView) itemView.findViewById(R.id.ubicacion_txt);
        }
    }

    @Override
    public DenunciasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_denuncias, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DenunciasAdapter.ViewHolder viewHolder, int position) {

        Denuncia denuncia = this.denuncias.get(position);

        viewHolder.tituloText.setText(denuncia.getTitulo());
        viewHolder.descripcionText.setText("Casua: " + denuncia.getDescripcion());

        viewHolder.ubicacionText.setText(denuncia.getUbicacion());

        String url = ApiService.API_BASE_URL + "/images/" + denuncia.getImagen();
        Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.fotoImage);

    }

    @Override
    public int getItemCount() {
        return this.denuncias.size();

    }
}
