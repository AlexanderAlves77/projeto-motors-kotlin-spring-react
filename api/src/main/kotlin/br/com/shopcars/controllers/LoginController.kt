package br.com.shopcars.controllers

import br.com.shopcars.dtos.ErroDTO
import br.com.shopcars.dtos.LoginDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/login")
class LoginController {

    @PostMapping
    fun efeturarLogin(@RequestBody dto : LoginDTO) : ResponseEntity<Any> {
        try {
            if(dto == null || dto.login.isNullOrBlank() || dto.login.isNullOrEmpty()
                || dto.senha.isNullOrEmpty() || dto.senha.isNullOrBlank()) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                "Parâmetros de entrada inválidos"), HttpStatus.BAD_REQUEST)
            }
            return ResponseEntity("Login autenticado com sucesso", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}