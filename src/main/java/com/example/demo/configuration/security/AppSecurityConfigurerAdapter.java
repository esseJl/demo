package com.example.demo.configuration.security;

import com.example.demo.filter.authentication.custom.CustomAuthenticationFilter;
import com.example.demo.model.user.User;
import com.example.demo.model.user.principal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;


@Configuration
@EnableWebSecurity
public class AppSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    private AccessDeniedHandler accessDeniedHandler;

    private Environment environment;

    @Autowired
    public AppSecurityConfigurerAdapter(UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler, Environment environment) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.environment = environment;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.requiresChannel().anyRequest().requiresSecure().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .filterSecurityInterceptorOncePerRequest(true)
                .antMatchers("/**/admin/**").hasRole("ADMIN")
                .antMatchers("/**/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/home/**", "/assets/**").permitAll()
                //.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")

                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
                /*.and()
                .rememberMe()
                .key("rem-me-key")
                .rememberMeParameter("rememberMe") // it is name of checkbox at login page
                .rememberMeCookieName("rememberLogin") // it is name of the cookie
                .tokenValiditySeconds(100) // remember for number of seconds*/
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .and().httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return ((request, response, authentication) -> {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            //System.out.println(principal.getUsername());

            //TODO filter principal for redirect
            response.sendRedirect(getServerContextPath());
        });
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return ((request, response, exception) -> {
            String message = exception.getMessage();
            response.sendRedirect(getServerContextPath() + "login?error=" + message);
        });
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect(getServerContextPath() + "login?logout");
        });
    }

    private String getServerContextPath() {

        if (!environment.containsProperty("server.servlet.context-path")) {
            return "/";
        } else {
            String serverContextPath = environment.getProperty("server.servlet.context-path");
            if (serverContextPath == null || serverContextPath.trim().equals(""))
                return "/";

            return serverContextPath + "/";
        }
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }


    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }
   /*





@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService);
    }


// create two users, admin and user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("$2a$12$EZRbHfhKWAyeV23wI7dQf.P1aWZkz2DSYlPmC8333qqVanvg6M9Li").roles("ADMIN");
    }

   @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder().username("admin").password("1234").roles("USER").build());
        return new InMemoryUserDetailsManager(users);
    }*/


}
