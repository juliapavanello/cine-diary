package com.example.cinediary.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.cinediary.R;
import com.example.cinediary.ui.main.MainActivity;

/** Tela "login 2": e-mail, senha, manter conectado, entrar e esqueci a senha. */
public class LoginFragment extends Fragment {

    private AuthViewModel viewModel;

    private TextInputLayout tilEmail, tilSenha;
    private TextInputEditText edtEmail, edtSenha;
    private CheckBox chkManterConectado;
    private MaterialButton btnEntrar;
    private ProgressBar progresso;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        tilEmail = view.findViewById(R.id.tilEmail);
        tilSenha = view.findViewById(R.id.tilSenha);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtSenha = view.findViewById(R.id.edtSenha);
        chkManterConectado = view.findViewById(R.id.chkManterConectado);
        btnEntrar = view.findViewById(R.id.btnEntrar);
        progresso = view.findViewById(R.id.progressoLogin);

        view.findViewById(R.id.btnVoltar).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        btnEntrar.setOnClickListener(v -> {
            limparErros();
            viewModel.login(texto(edtEmail), texto(edtSenha), chkManterConectado.isChecked());
        });

        view.findViewById(R.id.txtEsqueciSenha).setOnClickListener(v -> {
            limparErros();
            viewModel.recuperarSenha(texto(edtEmail));
        });

        viewModel.getEstado().observe(getViewLifecycleOwner(), this::renderizar);

        aplicarInsets(view);
    }

    private void renderizar(AuthState estado) {
        if (estado == null) return;

        boolean carregando = estado.status == AuthState.Status.CARREGANDO;
        progresso.setVisibility(carregando ? View.VISIBLE : View.GONE);
        btnEntrar.setEnabled(!carregando);
        btnEntrar.setText(carregando ? "" : getString(R.string.entrar));

        if (carregando || !estado.consumir()) return;

        switch (estado.status) {
            case SUCESSO:
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case EMAIL_ENVIADO:
                Toast.makeText(requireContext(), estado.mensagem, Toast.LENGTH_LONG).show();
                break;
            case ERRO:
                if (estado.campo == AuthState.Campo.EMAIL) tilEmail.setError(estado.mensagem);
                else if (estado.campo == AuthState.Campo.SENHA) tilSenha.setError(estado.mensagem);
                else Toast.makeText(requireContext(), estado.mensagem, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void limparErros() {
        tilEmail.setError(null);
        tilSenha.setError(null);
    }

    private String texto(TextInputEditText edt) {
        return edt.getText() == null ? "" : edt.getText().toString();
    }

    /** Status bar no topo e teclado/barra de navegação embaixo (edge-to-edge). */
    private void aplicarInsets(View raiz) {
        View header = raiz.findViewById(R.id.headerLogin);
        int topoBase = header.getPaddingTop();
        View conteudo = raiz.findViewById(R.id.conteudoLogin);
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
