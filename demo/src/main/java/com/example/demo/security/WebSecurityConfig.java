package com.example.demo.security;//package com.javatpoint.security;


import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.security.jwt.AuthTokenFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


//********תפקיד המחלקה:
//להגדיר כיצד הגישה למשאבים תתבצעה כולל אישור כניסה ואימות המשתמשים
////מחלקה שבה מגדירים את כל הגדרות האבטחה
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {


  @Qualifier("customUserDetailsService")
  @Autowired
  CustomUserDetailsService userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }


  //********תפקיד הפונקציה:
  //מה הפונקציה מחזירה?
  //  מחזיר את אובייקט `DaoAuthenticationProvider` המוגדר,
  //  אשר ישמש את Spring Security לביצוע משימות אימות בתוך האפליקציה
  //זה הפונקציה שדרכה נאמת את המשתמש מתוך מסד הנתונים
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

      authProvider.setUserDetailsService(userDetailsService);
    //קידוד סיסמה
      authProvider.setPasswordEncoder(passwordEncoder());

      return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  //********תפקיד הפונקציה:
  //מגדירה את שרשרת מסנן האבטחה
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //משבית את הגנת CSRF על ידי הפעלת שיטת `csrf()` והשבתתה
    http.csrf(csrf -> csrf.disable()).cors(cors->cors.configurationSource(request -> {
      CorsConfiguration corsConfiguration=new CorsConfiguration();
      corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
      corsConfiguration.setAllowedMethods(List.of("*"));
      corsConfiguration.setAllowedHeaders(List.of("*"));
      corsConfiguration.setAllowCredentials(true);
      return corsConfiguration;
    }))

        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->
          auth.requestMatchers("/h2-console/**").permitAll()
              .requestMatchers("/api/user/sign**").permitAll()
          //    .requestMatchers("/api/sharings/get").permitAll()
//                  .requestMatchers("/api/user/signIn").permitAll()
              .anyRequest().authenticated()
        );

 // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
    http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

    http.authenticationProvider(authenticationProvider());


    //***********משמעות הגדרה זו:
    //משמעות ההוספה של פילטר זה היא כי הנתיבים בהם מחייבת התחברות באמצעות אימות טוקן JWT (JSON Web Token)
    // יעברו תחילה דרך הפילטר שמטפל באימות הטוקנים,
    // ורק לאחר מכן ייעברו לפילטר המטפל באימות באמצעות שם משתמש וסיסמה.
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


    return http.build();
  }
//
//  @Bean
//  public CorsFilter corsFilter(){
//    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//    CorsConfiguration configuration=new CorsConfiguration();
//    configuration.setAllowCredentials(true);
//    configuration.addAllowedOriginPattern("*");
//    configuration.addAllowedMethod("*");
//    configuration.addAllowedHeader("*");
//    source.registerCorsConfiguration("/**",configuration);
//    return new CorsFilter();
//  }
//  @Bean
//  public CorsConfigurationSource corsConfigurationSource(){
//    CorsConfiguration corsConfiguration=new CorsConfiguration();
//    corsConfiguration.applyPermitDefaultValues();
//    corsConfiguration.setAllowedMethods(List.of("GET","POST","DELETE","PUT"));
//    corsConfiguration.setAllowCredentials(true);
//    corsConfiguration.addAllowedHeader("*");
//    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**",corsConfiguration);
//    return source;
//  }
}




//package com.javatpoint.security;
//
//import com.example.demo.security.jwt.AuthEntryPointJwt;
//import com.example.demo.security.jwt.AuthTokenFilter;
//import com.javatpoint.security.jwt.AuthEntryPointJwt;
//import com.javatpoint.security.jwt.AuthTokenFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
////מחלקה שבה מגדירים את כל הגדרות האבטחה
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//מאפשר preAuthrize
////נמלא את זה בהמשך
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//  @Autowired
//  private UserDetailsService userDetailsService;
//  @Autowired
//  private AuthEntryPointJwt unHandler;
//  @Bean
//  //זה הפונקציה שדרכה נאמת את המשתמש מתוך מסד הנתונים
//  public AuthenticationProvider authenticationProvider() {
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//    provider.setUserDetailsService(userDetailsService);
//    provider.setPasswordEncoder(new BCryptPasswordEncoder());
//    return provider;
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//    return authConfig.getAuthenticationManager();
//  }
//  @Bean
//
//  public AuthTokenFilter authenticationJwtTokenFilter() {
//    return new AuthTokenFilter();
//  }
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    //כאן נגדיר את כל ההגדרות האבטחה
//    http.csrf().disable().exceptionHandling().authenticationEntryPoint(unHandler).and().
//            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and().authorizeRequests().antMatchers("/h2-console/**")
//            .permitAll()
//            .antMatchers("/api/users/signup").permitAll()
//            .antMatchers("/api/users/signin").permitAll()
//            .anyRequest().authenticated()
//            .and().httpBasic();
//
//    http.cors();//תאפשר crossOrigion
//    http.headers().frameOptions().sameOrigin();//פקודה שחשובה לכניסה ל-h2
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//  }
//}

