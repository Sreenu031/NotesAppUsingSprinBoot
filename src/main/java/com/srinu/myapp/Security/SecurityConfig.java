package com.srinu.myapp.Security;

import com.srinu.myapp.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Bag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private  final UserService userService;

    @Bean
    public UserDetailsService userDetailServices(){
        return userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm->{
                    httpForm
                            .loginPage("/login").permitAll()
                            .loginProcessingUrl("/req/login")
                            .defaultSuccessUrl("/dashboard", true) // Redirect on successful login
                            .failureUrl("/login?error=true");
                })
                .logout(logout->{
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID");
                })
                .authorizeHttpRequests(registry->{
                    registry.requestMatchers("/req/signup","/css/**","/js/").permitAll();
                    registry.anyRequest().authenticated();
                })
                .build();
    }
}
