package com.example.cinediary.ui.auth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.cinediary.R;

/** Adapter do carrossel de imagens da tela de boas-vindas. */
public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final List<Integer> imagens;

    public CarouselAdapter(List<Integer> imagens) {
        this.imagens = imagens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrossel, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(imagens.get(position));
    }

    @Override
    public int getItemCount() {
        return imagens.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imgCarrossel);
        }

        void bind(@DrawableRes int res) {
            imagem.setImageResource(res);
        }
    }
}
