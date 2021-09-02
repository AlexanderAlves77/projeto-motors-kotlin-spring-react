package com.cars.carsapi.filters

import com.cars.carsapi.authorization
import com.cars.carsapi.bearer
import com.cars.carsapi.impl.UsuarioDetalheImpl
import com.cars.carsapi.repositories.UsuarioRepository
import com.cars.carsapi.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAutorizadorFilter(
    authenticationManager: AuthenticationManager,
    val jwtUtils: JWTUtils,
    val usuarioRepository: UsuarioRepository,
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)

        if(authorizationHeader != null && authorizationHeader.startsWith(bearer)) {
            val autorizado = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = autorizado
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(athorization: String): UsernamePasswordAuthenticationToken {
        val token = athorization.substring(7)
        if(jwtUtils.isTokenValido(token)){
            val idString = jwtUtils.getUsuarioId(token)

            if(!idString.isNullOrBlank() && !idString.isNullOrEmpty()){
                val usuario = usuarioRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("Usuário não enontrado")
                val usuarioImpl = UsuarioDetalheImpl(usuario)
                return UsernamePasswordAuthenticationToken(usuarioImpl, null, usuarioImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Token informado não está válido, ou não tem uma informação de identificação do usuário")
    }
}