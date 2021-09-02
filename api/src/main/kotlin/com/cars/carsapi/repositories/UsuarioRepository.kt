package com.cars.carsapi.repositories

import com.cars.carsapi.models.Usuario
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : CrudRepository<Usuario, Long> {
    fun findByEmail(email: String) : Usuario?
}