package br.com.webcars.projeto.catalogocarros.models

data class Usuario (
    val id: Long,
    val nome: String,
    val email: String,
    val senha: String)