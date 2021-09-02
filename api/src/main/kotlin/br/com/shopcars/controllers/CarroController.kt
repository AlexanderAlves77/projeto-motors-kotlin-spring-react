package br.com.shopcars.controllers

import br.com.shopcars.dtos.ErroDTO
import br.com.shopcars.dtos.SuccessoDTO
import br.com.shopcars.models.Carro
import br.com.shopcars.repositories.CarroRepository
import br.com.shopcars.repositories.UsuarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/carro")
class CarroController(usuarioRepository: UsuarioRepository,
                      val carroRepository: CarroRepository) : BaseController(usuarioRepository) {

    @GetMapping
    fun listaTodosCarros(): MutableIterable<Carro> = carroRepository.findAll()

    @PostMapping
    fun adicionarCarro(@RequestBody car: Carro, @RequestHeader("Authorization")
    authorization : String) : ResponseEntity<Any>{

        try{
            var usuario = lerToken(authorization)
            var erros = mutableListOf<String>()

            if(car == null) {
                erros.add("Carro não encontrado")
            } else {
                if(car.nome.isNullOrBlank() || car.nome.isNullOrEmpty() || car.nome.length < 2){
                    erros.add("Nome de carro inválido")
                }

                if(car.marca.isNullOrBlank() || car.marca.isNullOrEmpty() || car.marca.length < 4) {
                    erros.add("Marca de carro inválido")
                }

                if(car.modelo.isNullOrBlank() || car.modelo.isNullOrEmpty() || car.modelo.length < 4){
                    erros.add("Modelo de carro inválido")
                }

                if(car.modelo.isNullOrBlank() || car.modelo.isNullOrEmpty()){
                    erros.add("Foto de carro necessária")
                }
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    erros = erros), HttpStatus.BAD_REQUEST)
            }

            var carro = Carro(
                nome = car.nome,
                marca = car.marca,
                modelo = car.modelo,
                foto = car.foto
            )

            carroRepository.save(carro)

            return ResponseEntity(SuccessoDTO("Carro cadastrado com sucesso"), HttpStatus.OK)

        } catch (e: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível cadastrar o carro, tente novamente."),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun DeletarCarro(@PathVariable id: Long, @RequestHeader("Authorization")
    authorization: String) : ResponseEntity<Any> {

        try {
            var usuario = lerToken(authorization)
            var carro = carroRepository.findByIdOrNull(id)

            if(carro == null || carro.usuario?.id != usuario.id) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Carro informado não existe"), HttpStatus.BAD_REQUEST)
            }

            carroRepository.delete(carro)

            return ResponseEntity(SuccessoDTO("Carro removido com sucesso"),
                HttpStatus.OK)

        } catch (exception: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível remover o carro, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun atualizarCarro(
        @PathVariable id: Long,
        @RequestBody updateModel: Carro,
        @RequestHeader authorization: String) : ResponseEntity<Any>{

        try{
            var usuario = lerToken(authorization)
            var carro = carroRepository.findByIdOrNull(id)
            var erros = mutableListOf<String>()

            if(usuario == null || carro == null) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Carro informado não existe"), HttpStatus.BAD_REQUEST)
            }

            if(updateModel == null) {
                erros.add("Favor enviar os dados que deseja atualizar")
            } else {
                if(!updateModel.nome.isNullOrBlank() && !updateModel.nome.isNullOrEmpty()
                    && updateModel.nome.length < 2) {
                    erros.add("Nome inválido")
                }

                if(!updateModel.marca.isNullOrBlank() && !updateModel.marca.isNullOrEmpty()
                    && updateModel.marca.length < 4) {
                    erros.add("Marca inválido")
                }

                if(!updateModel.modelo.isNullOrBlank() && !updateModel.modelo.isNullOrEmpty()
                    && updateModel.modelo.length < 4) {
                    erros.add("Modelo inválido")
                }

                if(!updateModel.foto.isNullOrEmpty()) {
                    erros.add("Foto inválida")
                }
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    erros = erros), HttpStatus.BAD_REQUEST)
            }

            if(!updateModel.nome.isNullOrBlank() && !updateModel.nome.isNullOrEmpty()) {
                carro.nome = updateModel.nome
            }

            if(!updateModel.marca.isNullOrBlank() && !updateModel.marca.isNullOrEmpty()) {
                carro.marca = updateModel.marca
            }

            if(!updateModel.modelo.isNullOrBlank() && !updateModel.modelo.isNullOrEmpty()) {
                carro.modelo = updateModel.modelo
            }

            if(!updateModel.foto.isNullOrEmpty()) {
                carro.foto = updateModel.foto
            }

            carroRepository.save(carro)

            return ResponseEntity(SuccessoDTO("Carro atualizado com sucesso"), HttpStatus.OK)

        } catch (exception: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível atualizar o carro, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}