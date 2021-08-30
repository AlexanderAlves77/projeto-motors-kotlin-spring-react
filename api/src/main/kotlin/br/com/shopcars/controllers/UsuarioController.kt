package br.com.shopcars.controllers

import br.com.shopcars.dtos.ErroDTO
import br.com.shopcars.dtos.SucessoDTO
import br.com.shopcars.extensions.md5
import br.com.shopcars.extensions.toHex
import br.com.shopcars.models.Usuario
import br.com.shopcars.repositories.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/usuario")
class UsuarioController(val usuarioRepository: UsuarioRepository) {

    @PostMapping
    fun criarUsuario(@RequestBody usuario: Usuario) : ResponseEntity<Any> {
        try {
            val erros = mutableListOf<String>()

            if(usuario == null) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Parâmetros de entrada não enviado."), HttpStatus.BAD_REQUEST)
            }

            if(usuario.nome.isNullOrEmpty() || usuario.nome.isNullOrBlank() || usuario.nome.length < 2) {
                erros.add("Nome inválido")
            }

            if(usuario.email.isNullOrEmpty() || usuario.email.isNullOrBlank() || usuario.email.length < 5) {
                erros.add("Email inválido")
            }

            if(usuario.senha.isNullOrEmpty() || usuario.senha.isNullOrBlank() || usuario.senha.length < 4) {
                erros.add("Senha inválido")
            }

            if(usuarioRepository.findByEmail(usuario.email) != null){
                erros.add("Já existe usuário com o email informado")
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(), null, erros),
                    HttpStatus.BAD_REQUEST)
            }

            usuario.senha = md5(usuario.senha).toHex()
            usuarioRepository.save(usuario)

            return ResponseEntity(SucessoDTO("Usuário criado com sucesso"), HttpStatus.OK)
        } catch (excecao : Exception) {
            return ResponseEntity(
                ErroDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Não foi possível cadastrar o usuário, tente novamente"),
                        HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
