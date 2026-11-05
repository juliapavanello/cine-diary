package com.example.cinediary.ui.detalhes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;

import java.util.ArrayList;
import java.util.List;

/** Logos dos streamings onde o filme/série está disponível ("Onde assistir"). */
public class ProvedorAdapter extends RecyclerView.Adapter<ProvedorAdapter.ViewHolder> {

    private static final String URL_LOGO = "https://image.tmdb.org/t/p/w154";

    private final List<TmdbDetalhes.Provedor> provedores = new ArrayList<>();

    public void setProvedores(List<TmdbDetalhes.Provedor> novos) {
        provedores.clear();
        if (novos != null) provedores.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_provedor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TmdbDetalhes.Provedor p = provedores.get(position);
        holder.logo.setContentDescription(p.getNome());
        Glide.with(holder.logo).load(URL_LOGO + p.getLogoPath()).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return provedores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView logo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.imgProvedor);
        }
    }
}
