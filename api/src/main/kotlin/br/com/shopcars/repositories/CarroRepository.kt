package br.com.shopcars.repositories

import br.com.shopcars.models.Carro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CarroRepository : JpaRepository<Carro, Long> {

    @Query("SELECT c FROM Carro c " +
            " WHERE c.usuario.id  = :idUsuario " +
            "   AND (:periodoDe IS NULL OR t.dataPrevistaConclusao >= :periodoDe) " +
            "   AND (:periodoAte IS NULL OR t.dataPrevistaConclusao <= :periodoAte) " +
            "   AND (:status = 0 OR (:status = 1 AND c.dataConclusao IS NULL) " +
            "           OR (:status = 2 AND c.dataConclusao IS NOT NULL)) ")
    fun findByUsuarioWithFilter(
        idUsuario: Long,
        periodoDe: LocalDate?,
        periodoAte: LocalDate?,
        status: Int
    ) : List<Carro>?
}