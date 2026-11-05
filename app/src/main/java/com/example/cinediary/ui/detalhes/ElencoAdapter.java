package com.example.cinediary.ui.detalhes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;

import java.util.ArrayList;
import java.util.List;

/** Lista horizontal do elenco na tela do filme. */
public class ElencoAdapter extends RecyclerView.Adapter<ElencoAdapter.ViewHolder> {

    private static final String URL_FOTO = "https://image.tmdb.org/t/p/w185";

    private final List<TmdbDetalhes.Ator> atores = new ArrayList<>();

    public void setAtores(List<TmdbDetalhes.Ator> novos) {
        atores.clear();
        if (novos != null) atores.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_elenco, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(atores.get(position));
    }

    @Override
    public int getItemCount() {
        return atores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView foto;
        private final TextView nome, personagem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgAtor);
            nome = itemView.findViewById(R.id.txtNomeAtor);
            personagem = itemView.findViewById(R.id.txtPersonagem);
        }

        void bind(TmdbDetalhes.Ator ator) {
            nome.setText(ator.getNome());
            personagem.setText(ator.getPersonagem());

            if (ator.getFotoPath() != null) {
                Glide.with(foto).load(URL_FOTO + ator.getFotoPath())
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                        .into(foto);
            } else {
                foto.setImageResource(R.drawable.avatar_placeholder);
            }
        }
    }
}
