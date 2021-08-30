package br.com.shopcars.configurations

import br.com.shopcars.filters.JWTAutorizadorFilter
import br.com.shopcars.repositories.UsuarioRepository
import br.com.shopcars.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .antMatchers(HttpMethod.POST, "/api/usuario").permitAll()
            .anyRequest().authenticated()

        http.cors().configurationSource(configuracaoCors())
        http.addFilter(JWTAutorizadorFilter(authenticationManager(), jwtUtils, usuarioRepository))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun configuracaoCors() : CorsConfigurationSource? {
        val configuracao = CorsConfiguration()

        configuracao.allowedOrigins = mutableListOf("*")
        configuracao.allowedMethods = mutableListOf("*")
        configuracao.allowedHeaders = mutableListOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuracao)
        return source
    }
}