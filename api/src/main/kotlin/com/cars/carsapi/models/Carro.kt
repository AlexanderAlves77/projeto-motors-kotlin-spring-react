package com.cars.carsapi.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class Carro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    val id: Long = 0,
    var nome: String = "",
    val marca: String = "",
    val modelo: String = "",
    val foto: List<String>,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    val usuario: Usuario? = null
)