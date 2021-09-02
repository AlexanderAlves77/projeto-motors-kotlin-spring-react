package br.com.shopcars.repositories

import br.com.shopcars.models.Carro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface CarroRepository : CrudRepository<Carro, Long> {

    interface CarroRepository : CrudRepository<Carro, Long> {

        @Query("SELECT * FROM Carro order by nome asc")
        fun findByUsuarioWithFilter(idUsuario: Long, nome : String?, marca: String?, modelo: String?, foto: List<String>) : List<Carro>?
    }