package com.cars.carsapi.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    val id: Long = 0L,
    val nome: String = "",
    val email: String = "",
    var senha: String = "",

    @JsonBackReference
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    val carros : List<Carro> = emptyList()
)