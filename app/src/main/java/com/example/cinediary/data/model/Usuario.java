package com.example.cinediary.data.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/** Documento salvo em Firestore: usuarios/{uid} */
public class Usuario {

    private String nome;
    private String email;
    private String fotoBase64;   // JPEG 256x256 em Base64, ou null
    @ServerTimestamp
    private Date criadoEm;       // preenchido pelo servidor

    /** Construtor vazio exigido pelo Firestore para ler o documento. */
    public Usuario() {}

    public Usuario(String nome, String email, String fotoBase64) {
        this.nome = nome;
        this.email = email;
        this.fotoBase64 = fotoBase64;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    public Date getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Date criadoEm) { this.criadoEm = criadoEm; }
}
