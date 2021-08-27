package br.com.webcars.projeto.catalogocarros.dtos

class ErroDTO (val status: Int, val erro: String? = null, val erros: List<String>? = null)