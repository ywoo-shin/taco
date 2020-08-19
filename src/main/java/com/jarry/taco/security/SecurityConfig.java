package com.jarry.taco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private DataSource dataSource;
    @Autowired private UserRepositoryUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/design", "orders")
                .access("hasRole('ROLE_USER')")
            .antMatchers("/", "/**")
                .permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .csrf();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    // TODO: 19/08/2020  ldapAuthentication
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.ldapAuthentication()
//            .userSearchBase("ou=people").userSearchFilter("(uid={0})")
//            .groupSearchBase("ou=groups").groupSearchFilter("member={0}")
//            .contextSource()
//            .root("dc=tacocloud,dc=com")
//            .ldif("classpath:users.ldif")
//            .and()
//            .passwordCompare()
//            .passwordEncoder(new BCryptPasswordEncoder())
//            .passwordAttribute("userPasscode");
//    }

    // TODO: 19/08/2020  jdbcAuthentication
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource)
//            .usersByUsernameQuery("select username, password, enabled from users where username=?")
//            .authoritiesByUsernameQuery("select username, authority from authorities where username=?")
//            .passwordEncoder(new NoEncodingPasswordEncoder());
//    }

    // TODO: 19/08/2020  inMemoryAuthentication
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("user1").password("{noop}password1").authorities("ROLE_USER")
//            .and()
//            .withUser("user2").password("{noop}password2").authorities("ROLE_USER");
//    }
}
