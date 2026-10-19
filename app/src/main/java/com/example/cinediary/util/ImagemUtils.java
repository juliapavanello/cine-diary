package com.example.cinediary.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** Carrega, recorta e converte imagens de perfil. Rodar fora da main thread. */
public final class ImagemUtils {

    private ImagemUtils() {}

    /** Lê a imagem do Uri, recorta o centro em quadrado e redimensiona para lado x lado. */
    public static Bitmap carregarQuadrado(Context ctx, Uri uri, int lado) throws IOException {
        Bitmap original = decodificarReduzido(ctx.getContentResolver(), uri, lado);

        int menor = Math.min(original.getWidth(), original.getHeight());
        int x = (original.getWidth() - menor) / 2;
        int y = (original.getHeight() - menor) / 2;
        Bitmap quadrado = Bitmap.createBitmap(original, x, y, menor, menor);
        return Bitmap.createScaledBitmap(quadrado, lado, lado, true);
    }

    public static String paraBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
    }

    public static Bitmap deBase64(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /** Decodifica já reduzida para não estourar memória com fotos de 12+ MP. */
    private static Bitmap decodificarReduzido(ContentResolver cr, Uri uri, int lado) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ImageDecoder já corrige a rotação das fotos da câmera (EXIF)
            ImageDecoder.Source fonte = ImageDecoder.createSource(cr, uri);
            return ImageDecoder.decodeBitmap(fonte, (decoder, info, src) -> {
                int w = info.getSize().getWidth();
                int h = info.getSize().getHeight();
                float escala = (lado * 2f) / Math.min(w, h);
                if (escala < 1f) {
                    decoder.setTargetSize(Math.round(w * escala), Math.round(h * escala));
                }
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE);
            });
        }

        // Android 7–8: BitmapFactory com inSampleSize (não corrige rotação EXIF)
        BitmapFactory.Options limites = new BitmapFactory.Options();
        limites.inJustDecodeBounds = true;
        try (InputStream in = cr.openInputStream(uri)) {
            BitmapFactory.decodeStream(in, null, limites);
        }
        int amostra = 1;
        while (Math.min(limites.outWidth, limites.outHeight) / (amostra * 2) >= lado * 2) {
            amostra *= 2;
        }
        BitmapFactory.Options opcoes = new BitmapFactory.Options();
        opcoes.inSampleSize = amostra;
        Bitmap bmp;
        try (InputStream in = cr.openInputStream(uri)) {
            bmp = BitmapFactory.decodeStream(in, null, opcoes);
        }
        if (bmp == null) throw new IOException("Imagem inválida");
        return bmp;
    }
}
