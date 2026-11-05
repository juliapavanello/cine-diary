package com.example.cinediary.ui.detalhes;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinediary.R;
import com.example.cinediary.data.remote.tmdb.TmdbDetalhes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Reviews do TMDB na tela do filme. Toque no card para expandir/recolher o texto. */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String URL_AVATAR = "https://image.tmdb.org/t/p/w92";
    private static final int LINHAS_RECOLHIDO = 4;

    private final List<TmdbDetalhes.Review> reviews = new ArrayList<>();
    private final Set<Integer> expandidos = new HashSet<>();

    public void setReviews(List<TmdbDetalhes.Review> novas) {
        reviews.clear();
        expandidos.clear();
        if (novas != null) reviews.addAll(novas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TmdbDetalhes.Review r = reviews.get(position);

        h.autor.setText(r.getAutor());
        h.data.setText(r.getData() != null ? r.getData() : "");

        if (r.getNota() != null) {
            h.nota.setVisibility(View.VISIBLE);
            h.nota.setText(String.format(Locale.getDefault(), "★ %.0f/10", r.getNota()));
        } else {
            h.nota.setVisibility(View.GONE);
        }

        // As reviews podem vir com marcações HTML/markdown simples
        h.texto.setText(HtmlCompat.fromHtml(r.getTexto(), HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim());

        boolean expandido = expandidos.contains(position);
        h.texto.setMaxLines(expandido ? Integer.MAX_VALUE : LINHAS_RECOLHIDO);
        h.lerMais.setText(expandido ? R.string.detalhes_ler_menos : R.string.detalhes_ler_mais);

        // Só mostra "Ler mais" se o texto realmente passar das linhas visíveis
        h.texto.post(() -> {
            Layout layout = h.texto.getLayout();
            boolean cortado = layout != null && layout.getLineCount() > 0
                    && layout.getEllipsisCount(layout.getLineCount() - 1) > 0;
            h.lerMais.setVisibility(expandido || cortado ? View.VISIBLE : View.GONE);
        });

        h.itemView.setOnClickListener(v -> {
            int pos = h.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            if (!expandidos.remove(pos)) expandidos.add(pos);
            notifyItemChanged(pos);
        });

        String avatar = r.getAvatarPath();
        if (avatar != null && avatar.startsWith("/")) {
            Glide.with(h.foto).load(URL_AVATAR + avatar)
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .into(h.foto);
        } else {
            h.foto.setImageResource(R.drawable.avatar_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView foto;
        final TextView autor, data, nota, texto, lerMais;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgAutorReview);
            autor = itemView.findViewById(R.id.txtAutorReview);
            data = itemView.findViewById(R.id.txtDataReview);
            nota = itemView.findViewById(R.id.txtNotaReview);
            texto = itemView.findViewById(R.id.txtTextoReview);
            lerMais = itemView.findViewById(R.id.txtLerMais);
        }
    }
}
