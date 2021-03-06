package com.spring.site.etc.security;


import com.spring.site.etc.security.login.LoginAuthProvider;
import com.spring.site.etc.security.login.LoginSecurityService;
import com.spring.site.etc.token.TokenFilter;
import com.spring.site.etc.token.TokenProvider;
import com.spring.site.etc.filter.LoginSuccessHandler;
import com.spring.site.etc.filter.LoginFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    private LoginAuthProvider loginAuthProvider;

    @Autowired
    private LoginFailHandler loginCheckFilter;

    @Autowired
    private TokenProvider jwtToken;

    @Autowired
    private LoginSecurityService loginSecurityService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;


    /* static 관련설정은 무시 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web .ignoring().antMatchers(
            "/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .httpBasic().disable()// 로그인 기본 주소 막기
                .cors()
                .and()
                .authorizeRequests() //
                .antMatchers("/login","/loginForm","/home","/add").permitAll() // 누구나 접근 허용
                .antMatchers("/member/").hasRole("USER") // USER, ADMIN만 접근 가능
                .antMatchers("/admin/").hasRole("ADMIN") // ADMIN만 접근 가능
                .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().disable().and()
                .formLogin()//.disable()
                .loginPage("/loginForm") // 로그인 페이지 링크
//                .loginProcessingUrl("/login") //로그인 동작
                .defaultSuccessUrl("/")
//                .usernameParameter("id")
//                .passwordParameter("pw")
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginCheckFilter)
                .permitAll()
                .and()
//                .rememberMe()
//                .key("rememberMe")
//                .userDetailsService(loginSecurityService)
//                .and()
                .logout() // 8
                .logoutUrl("/home")
                .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true) // 세션 날리기
                .permitAll()
                .and()
                .addFilterBefore(new TokenFilter(jwtToken),
                        UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new LoginAuthProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}