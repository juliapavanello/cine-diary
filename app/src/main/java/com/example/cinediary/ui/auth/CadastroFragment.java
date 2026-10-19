package com.example.cinediary.ui.auth;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.cinediary.R;
import com.example.cinediary.ui.main.MainActivity;

/** Tela de cadastro: foto (câmera ou galeria), nome, e-mail, senha e confirmação. */
public class CadastroFragment extends Fragment {

    private static final String KEY_URI_CAMERA = "uri_camera";

    private CadastroViewModel viewModel;

    private ImageView imgAvatar;
    private TextView txtFoto;
    private TextInputLayout tilNome, tilEmail, tilSenha, tilConfirmar;
    private TextInputEditText edtNome, edtEmail, edtSenha, edtConfirmar;
    private MaterialButton btnCriar;
    private ProgressBar progresso;

    private Uri uriFotoCamera;

    // Galeria: Photo Picker do Android, não precisa de permissão
    private final ActivityResultLauncher<PickVisualMediaRequest> abrirGaleria =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) viewModel.selecionarFoto(uri);
            });

    // Câmera: abre o app de câmera e salva a foto no Uri que passamos
    private final ActivityResultLauncher<Uri> abrirCamera =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), salvou -> {
                if (Boolean.TRUE.equals(salvou) && uriFotoCamera != null) {
                    viewModel.selecionarFoto(uriFotoCamera);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);
        if (savedInstanceState != null) {
            uriFotoCamera = savedInstanceState.getParcelable(KEY_URI_CAMERA);
        }

        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtFoto = view.findViewById(R.id.txtFoto);
        tilNome = view.findViewById(R.id.tilNome);
        tilEmail = view.findViewById(R.id.tilEmail);
        tilSenha = view.findViewById(R.id.tilSenha);
        tilConfirmar = view.findViewById(R.id.tilConfirmarSenha);
        edtNome = view.findViewById(R.id.edtNome);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtSenha = view.findViewById(R.id.edtSenha);
        edtConfirmar = view.findViewById(R.id.edtConfirmarSenha);
        btnCriar = view.findViewById(R.id.btnCriarConta);
        progresso = view.findViewById(R.id.progressoCadastro);

        view.findViewById(R.id.btnVoltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        view.findViewById(R.id.areaAvatar).setOnClickListener(v -> escolherFoto());

        btnCriar.setOnClickListener(v -> {
            limparErros();
            viewModel.cadastrar(texto(edtNome), texto(edtEmail), texto(edtSenha), texto(edtConfirmar));
        });

        view.findViewById(R.id.txtJaTenhoConta).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_cadastro_to_login));

        viewModel.getFoto().observe(getViewLifecycleOwner(), this::mostrarFoto);
        viewModel.getEstado().observe(getViewLifecycleOwner(), this::renderizar);

        aplicarInsets(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (uriFotoCamera != null) outState.putParcelable(KEY_URI_CAMERA, uriFotoCamera);
    }

    // ---------- Foto ----------

    private void escolherFoto() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add(getString(R.string.tirar_foto));
        opcoes.add(getString(R.string.escolher_galeria));
        if (viewModel.temFoto()) opcoes.add(getString(R.string.remover_foto));

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.foto_perfil)
                .setItems(opcoes.toArray(new String[0]), (dialog, which) -> {
                    if (which == 0) tirarFoto();
                    else if (which == 1) abrirGaleria.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                    else viewModel.removerFoto();
                })
                .show();
    }

    private void tirarFoto() {
        File pasta = new File(requireContext().getCacheDir(), "fotos");
        //noinspection ResultOfMethodCallIgnored
        pasta.mkdirs();
        File arquivo = new File(pasta, "foto_perfil.jpg");
        uriFotoCamera = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".fileprovider", arquivo);
        try {
            abrirCamera.launch(uriFotoCamera);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Nenhum app de câmera encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarFoto(@Nullable Bitmap bmp) {
        if (bmp == null) {
            imgAvatar.setImageResource(R.drawable.avatar_placeholder);
            txtFoto.setText(R.string.adicionar_foto);
        } else {
            imgAvatar.setImageBitmap(bmp);
            txtFoto.setText(R.string.alterar_foto);
        }
    }

    // ---------- Estado ----------

    private void renderizar(AuthState estado) {
        if (estado == null) return;

        boolean carregando = estado.status == AuthState.Status.CARREGANDO;
        progresso.setVisibility(carregando ? View.VISIBLE : View.GONE);
        btnCriar.setEnabled(!carregando);
        btnCriar.setText(carregando ? "" : getString(R.string.criar_conta));

        if (carregando || !estado.consumir()) return;

        if (estado.status == AuthState.Status.SUCESSO) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        if (estado.status == AuthState.Status.ERRO) {
            switch (estado.campo) {
                case NOME: tilNome.setError(estado.mensagem); break;
                case EMAIL: tilEmail.setError(estado.mensagem); break;
                case SENHA: tilSenha.setError(estado.mensagem); break;
                case CONFIRMAR_SENHA: tilConfirmar.setError(estado.mensagem); break;
                default: Toast.makeText(requireContext(), estado.mensagem, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void limparErros() {
        tilNome.setError(null);
        tilEmail.setError(null);
        tilSenha.setError(null);
        tilConfirmar.setError(null);
    }

    private String texto(TextInputEditText edt) {
        return edt.getText() == null ? "" : edt.getText().toString();
    }

    private void aplicarInsets(View raiz) {
        View header = raiz.findViewById(R.id.headerCadastro);
        int topoBase = header.getPaddingTop();
        View conteudo = raiz.findViewById(R.id.conteudoCadastro);
        int baseBase = conteudo.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(raiz, (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets teclado = insets.getInsets(WindowInsetsCompat.Type.ime());
            header.setPadding(header.getPaddingLeft(), topoBase + barras.top,
                    header.getPaddingRight(), header.getPaddingBottom());
            conteudo.setPadding(conteudo.getPaddingLeft(), conteudo.getPaddingTop(),
                    conteudo.getPaddingRight(), baseBase + Math.max(barras.bottom, teclado.bottom));
            return insets;
        });
    }
}
