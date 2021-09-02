package com.cars.carsapi.controllers

import com.cars.carsapi.dtos.ErroDTO
import com.cars.carsapi.dtos.SuccessoDTO
import com.cars.carsapi.extensios.md5
import com.cars.carsapi.extensios.toHex
import com.cars.carsapi.models.Usuario
import com.cars.carsapi.repositories.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.xml.ws.Response

@RestController
@RequestMapping("api/usuario")
class UsuarioController(val usuarioRepository: UsuarioRepository) {

    @PostMapping
    fun criarUsuario(@RequestBody usuario: Usuario) : ResponseEntity<Any> {
        try{
            val erros = mutableListOf<String>()

            if(usuario == null) {
                return ResponseEntity(
                    ErroDTO(HttpStatus.BAD_REQUEST.value(),
                "Parâmetros de entrada não enviado"), HttpStatus.BAD_REQUEST)
            }

            if(usuario.nome.isNullOrEmpty() || usuario.nome.isNullOrBlank() || usuario.nome.length < 2) {
                erros.add("Nome inválido")
            }

            if(usuario.email.isNullOrEmpty() || usuario.email.isNullOrBlank() || usuario.email.length < 5) {
                erros.add("Email inválido")
            }

            if(usuario.senha.isNullOrBlank() || usuario.senha.isNullOrEmpty() || usuario.senha.length < 4) {
                erros.add("Senha inválida")
            }

            if(usuarioRepository.findByEmail(usuario.email) != null) {
                erros.add("Já existe usuário com o email informado")
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(), erros = erros),
                    HttpStatus.BAD_REQUEST)
            }

            usuario.senha = md5(usuario.senha).toHex()
            usuarioRepository.save(usuario)

            return ResponseEntity(SuccessoDTO("Usuário criado com sucesso"), HttpStatus.OK)

        } catch (excecao: Exception) {
            return ResponseEntity(
                ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível cadastrar o usuário, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}