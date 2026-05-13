package com.hoteltransilvania.usuarios.security;

//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;

//import java.io.IOException;
//import java.util.Collections;
//import java.util.ArrayList;
//import java.util.Map;

//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import java.util.List;

//@Component
//public class JwtFilter extends OncePerRequestFilter {

   // private final JwtUtil jwtUtil;

   // public JwtFilter(JwtUtil jwtUtil) {
 //       this.jwtUtil = jwtUtil;
 //   ////}

   // @Ove//rride
    //protected void doFilterInternal(
    //        HttpServletRequest request,
    //        HttpServletResponse response,
    //        FilterChain filterChain
    //) throws ServletException, IOException {

    //    String path = request.getServletPath();

    //    if (
    //    path.equals("/usuarios/login")
        
//) {

 //   filterChain.doFilter(request, response);
  //  return;
//}

   //     String authorizationHeader = request.getHeader("Authorization");

    //    if (authorizationHeader == null ||
    //            !authorizationHeader.startsWith("Bearer ")) {

    //        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //        response.getWriter().write("Token no enviado o formato incorrecto");
    //        return;
    //    }

    //    String token = authorizationHeader.substring(7);

     //   if (!jwtUtil.validarToken(token)) {
       //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //    response.getWriter().write("Token inválido o expirado");
        //    return;
        //}

       // String username = jwtUtil.extraerUsername(token);
        //Long rolId = jwtUtil.extraerRolId(token);
        //Object privilegios = jwtUtil.extraerPrivilegios(token);

        //List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //    if (
        //path.equals("/usuarios/login")
        //|| (path.equals("/usuarios") && request.getMethod().equalsIgnoreCase("GET"))
//){
    //filterChain.doFilter(request, response);
    //return;
//}

        //UsernamePasswordAuthenticationToken authentication =
        //new UsernamePasswordAuthenticationToken(
        //        username,
        //        null,
         //       authorities
        //);

        //SecurityContextHolder.getContext().setAuthentication(authentication);

        //filterChain.doFilter(request, response);
   // }
//}