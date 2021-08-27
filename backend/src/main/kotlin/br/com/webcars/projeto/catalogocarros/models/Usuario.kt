package br.com.webcars.projeto.catalogocarros.models

import javax.persistence.*

@Entity
data class Usuario (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    val id: Long = 0,
    val nome: String = "",
    val email: String = "",
    val senha: String = "")