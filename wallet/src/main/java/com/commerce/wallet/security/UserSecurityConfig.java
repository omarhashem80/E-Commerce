//package com.commerce.wallet.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class UserSecurityConfig {
//
////    @Bean
////    public UserDetailsManager userDetailsManager(DataSource dataSource) {
////        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
////
////        jdbcUserDetailsManager.setUsersByUsernameQuery(
////                "select email, password, active as enabled from users where email=? and active=true"
////        );
////
////        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
////                "select email, role from users where email=?"
////        );
////
////        return jdbcUserDetailsManager;
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth-> auth.anyRequest().permitAll());
//
//        http.csrf(csrf -> csrf.disable());
//        return http.build();
//    }
//}
