package com.lifesaver.helpdesk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    DataSource dataSource;

    @Value("${spring.queryes.oper-query}")
    private String operQuery;


    @Value("${spring.queryes.rol-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(operQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/perfil").permitAll()
                .antMatchers("/template").permitAll()
                .antMatchers("/revisa").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/login-template").permitAll()
                .antMatchers("/operador").permitAll()
                .antMatchers("/registro").permitAll()
                .antMatchers("/guardaSolicitud").permitAll()
                .antMatchers("/manager/**").hasAnyAuthority("OPERADOR", "ADMIN")
                .antMatchers("/client/**").hasAnyAuthority("CLIENTE")
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/manager/dashboard")
                .usernameParameter("usuario")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied");

    }

    @Override
    public void configure(WebSecurity web)throws Exception{
            web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                        "/webjars/**", "/vendor/**","/vendor/jquery-validation/**","/vendor/jquery-validation/localization/**",
                        "/vendor/malihu-custom-scrollbar-plugin/**", "/fonts/**",
                        "/img/**", "/img/flags", "/img/photos", "/vendor/chart.js/**", "/vendor/jquery.cookie/**"
                ,"/vendor/popper.js/**","/vendor/popper.js/umd/**","/vendor/popper.js/esm/**","/vendor/popper.js/umd/popper.min.js.map");
    }


    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
