package br.com.webcars.projeto.catalogocarros.filters

import br.com.webcars.projeto.catalogocarros.authorization
import br.com.webcars.projeto.catalogocarros.bearer
import br.com.webcars.projeto.catalogocarros.impl.UsuarioDetalheImpl
import br.com.webcars.projeto.catalogocarros.models.Usuario
import br.com.webcars.projeto.catalogocarros.utils.JWTUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAutorizadorFilter(authenticationManager: AuthenticationManager, val jwtUtils: JWTUtils)
    : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)

        if(authorizationHeader != null && authorizationHeader.startsWith(bearer)){
            val autorizado = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = autorizado
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader.substring(7)
        if(jwtUtils.isTokenValido(token)){
            val idString = jwtUtils.getUsuarioId(token)
            if( !idString.isNullOrBlank() && !idString.isNullOrEmpty() ) {
                val usuario = Usuario(idString.toLong(), "Usuário Teste", "admin@admin.com", "Admin1234@")
                val usuarioImpl = UsuarioDetalheImpl(usuario)
                return UsernamePasswordAuthenticationToken(usuarioImpl, null, usuarioImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Token informado não está válido ou não tem uma informação de identificação do usuário.")
    }
}