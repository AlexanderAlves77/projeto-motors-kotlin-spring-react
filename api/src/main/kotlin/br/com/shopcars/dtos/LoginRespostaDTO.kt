package br.com.shopcars.dtos

data class LoginRespostaDTO(val nome: String, val email: String, val token: String = "")