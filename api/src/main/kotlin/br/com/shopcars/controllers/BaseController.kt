package br.com.shopcars.controllers

import br.com.shopcars.models.Usuario
import br.com.shopcars.repositories.UsuarioRepository
import br.com.shopcars.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull

open class BaseController(val usuarioRepository: UsuarioRepository) {

    fun lerToken(authorization: String) : Usuario {
        val token = authorization.substring(7)
        val userIdSt = JWTUtils().getUsuarioId(token)

        if(userIdSt == null || userIdSt.isNullOrEmpty() || userIdSt.isNullOrBlank()) {
            throw IllegalArgumentException("Você não tem acesso a este recurso")
        }

        var usuario = usuarioRepository.findByIdOrNull(userIdSt.toLong())
        if(usuario == null){
            throw IllegalArgumentException("Usuário não encontrado")
        }

        return usuario
    }
}