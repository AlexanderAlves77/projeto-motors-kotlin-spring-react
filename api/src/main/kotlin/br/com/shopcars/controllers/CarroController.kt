package br.com.shopcars.controllers

import br.com.shopcars.dtos.ErroDTO
import br.com.shopcars.dtos.SucessoDTO
import br.com.shopcars.models.Carro
import br.com.shopcars.repositories.CarroRepository
import br.com.shopcars.repositories.UsuarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/carro")
class CarroController(
    usuarioRepository: UsuarioRepository,
    val carroRepository: CarroRepository
) : BaseController(usuarioRepository) {

    @GetMapping
    fun ListaCarro() : ResponseEntity<Any> {

        try{


            val resultado = carroRepository.findByUsuarioWithFilter(usuario.id, periodoDeDt, periodoAteDt, statusInt)

            return ResponseEntity(resultado, HttpStatus.OK)
        } catch (e: Exception){
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Não foi possível listar as atividades do usuário"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping
    fun AdicionarCarro(@RequestBody req: Carro, @RequestHeader("Authorization") authorization: String) : ResponseEntity<Any> {
        try{
            var usuario = lerToken(authorization)
            var erros = mutableListOf<String>()

            if(req == null) {
                erros.add("Tarefa não encontrada")
            } else {
                if(req.nome.isNullOrBlank() || req.nome.isNullOrEmpty() || req.nome.length < 4) {
                    erros.add("Nome inválido")
                }

                if (req.dataPrevistaConclusao.isBefore(LocalDate.now())) {
                    erros.add("Data de previsão não pode ser menor que hoje")
                }
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(), erros = erros),
                    HttpStatus.BAD_REQUEST)
            }

            var carro = Carro(
                nome = req.nome,
                dataPrevistaConclusao = req.dataPrevistaConclusao,
                usuario = usuario
            )

            carroRepository.save(carro)

            return ResponseEntity(SucessoDTO("Carro adicionada com sucesso."), HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foipossível adicionar carro, tente novamente."),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun DeletarCarro(@PathVariable id: Long, @RequestHeader("Authorization") authorization: String) : ResponseEntity<Any> {
        try{
            val usuario = lerToken(authorization)
            val carro = carroRepository.findByIdOrNull(id)

            if(carro == null || carro.usuario?.id != usuario.id) {
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "carro informado não existe"), HttpStatus.BAD_REQUEST)
            }

            carroRepository.delete(carro)

            return ResponseEntity(SucessoDTO("Carro deletado com sucesso"), HttpStatus.OK)
        } catch(e: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível deletar este carro, tente novamente."),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun atualizarCarro(
        @PathVariable id: Long,
        @RequestBody updateModel: Carro,
        @RequestHeader authorization: String) : ResponseEntity<Any> {

        try{
            var usuario = lerToken(authorization)
            var carro = carroRepository.findByIdOrNull(id)

            var erros = mutableListOf<String>()

            if(usuario == null || carro == null){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Carro informado não existe"),HttpStatus.BAD_REQUEST)
            }

            if(updateModel == null){
                erros.add("Favor enviar os dados que deseja atualizar")
            } else {
                if(!updateModel.nome.isNullOrEmpty() && !updateModel.nome.isNullOrBlank()
                    && updateModel.nome.length > 4){
                    erros.add("Nome inválido")
                }

                if(updateModel.dataConclusao != null && updateModel.dataConclusao == LocalDate.MIN ){
                    erros.add("Data de conclusão inválida")
                }
            }

            if(erros.size > 0) {
                return ResponseEntity(ErroDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    erros = erros), HttpStatus.BAD_REQUEST)
            }

            if(updateModel.nome.isNullOrEmpty() && updateModel.nome.isNullOrEmpty()) {
                carro.nome = updateModel.nome
            }

            if(updateModel.dataPrevistaConclusao.isBefore(LocalDate.now())){
                carro.dataPrevistaConclusao = updateModel.dataPrevistaConclusao
            }

            if(updateModel.dataConclusao != null && updateModel.dataConclusao != LocalDate.MIN) {
                carro.dataConclusao = updateModel.dataConclusao
            }

            carroRepository.save(carro)

            return ResponseEntity(SucessoDTO("Carro atualizado com sucesso"), HttpStatus.OK)

        } catch (e: Exception) {
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Não foi possível atualizar, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}