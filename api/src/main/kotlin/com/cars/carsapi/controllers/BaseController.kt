package com.cars.carsapi.controllers

import com.cars.carsapi.models.Usuario
import com.cars.carsapi.repositories.UsuarioRepository
import com.cars.carsapi.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull


open class BaseController(val usuarioRepository: UsuarioRepository) {

    fun lerToken(authorization: String) : Usuario {
        val token = authorization.substring(7)
        var userIdStr = JWTUtils().getUsuarioId(token)

        if(userIdStr == null || userIdStr.isNullOrEmpty() || userIdStr.isNullOrBlank()) {
            throw IllegalArgumentException("Você não têm acesso a este recurso")
        }

        var usuario = usuarioRepository.findByIdOrNull(userIdStr.toLong())

        if(usuario == null) {
            throw IllegalArgumentException("Usuário não encontrado")
        }

        return usuario
    }
}