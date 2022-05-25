package pro.keenetic.marketbot.bot.market_bot.config;

import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pro.keenetic.marketbot.bot.market_bot.models.Permission;
import pro.keenetic.marketbot.bot.market_bot.security.CustomAuthenticationFailureHandler;
import pro.keenetic.marketbot.bot.market_bot.security.CustomAuthenticationSuccessHandler;
import pro.keenetic.marketbot.bot.market_bot.security.CustomLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/auth/registration").not().fullyAuthenticated()
                    .antMatchers(HttpMethod.GET,    "/guest").hasAnyAuthority(Permission.OBSERVER.getPermission())
                    .antMatchers(HttpMethod.GET,    "/admin").hasAnyAuthority(
                            Permission.ADMIN.getPermission(), Permission.OBSERVER.getPermission())
                    .antMatchers(HttpMethod.GET,    "/trader", "/bots/**").hasAnyAuthority(
                            Permission.ADMIN.getPermission(), Permission.OBSERVER.getPermission(), Permission.TRADER.getPermission())
                    .antMatchers(HttpMethod.POST,   "/bots/**").hasAuthority(Permission.TRADER.getPermission())
                    .antMatchers(HttpMethod.GET,    "/users/**").hasAnyAuthority(
                            Permission.ADMIN.getPermission(), Permission.OBSERVER.getPermission())
                    .antMatchers(HttpMethod.POST,   "/users/**").hasAuthority(Permission.ADMIN.getPermission())
                    .antMatchers(HttpMethod.PATCH,  "/users/**").hasAuthority(Permission.ADMIN.getPermission())
                    .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(Permission.ADMIN.getPermission())
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/auth/login").permitAll()
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies()
                    .logoutSuccessUrl("/auth/login")
                    .logoutSuccessHandler(logoutSuccessHandler());

        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }
}
