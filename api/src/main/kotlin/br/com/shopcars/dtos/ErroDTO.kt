package br.com.shopcars.dtos

class ErroDTO(val status: Int, val erro: String? = null, val erros: List<String>? = null)