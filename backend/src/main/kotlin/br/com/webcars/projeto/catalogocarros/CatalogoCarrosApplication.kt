package br.com.webcars.projeto.catalogocarros

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalogoCarrosApplication

fun main(args: Array<String>) {
	runApplication<CatalogoCarrosApplication>(*args)
}
