package com.cars.carsapi.repositories

import com.cars.carsapi.models.Carro
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CarroRepository : CrudRepository<Carro, Long> {

    @Query("SELECT * FROM Carro order by nome asc")
    fun findByUsuarioWithFilter(idUsuario: Long, nome : String?, marca: String?, modelo: String?, foto: List<String>) : List<Carro>?
}