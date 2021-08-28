package br.com.webcars.projeto.catalogocarros.repositories

import br.com.webcars.projeto.catalogocarros.models.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByEmail(email: String) : Usuario?
}