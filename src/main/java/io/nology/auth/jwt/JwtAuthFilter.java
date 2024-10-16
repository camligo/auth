package io.nology.auth.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.nology.auth.user.User;
import io.nology.auth.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwtToken = authHeader.substring(7);

    Long userId = this.jwtService.extractUserId(jwtToken);

    if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      User user = this.userService.getById(userId);

      if(this.jwtService.isTokenValid(jwtToken, user)) {
        UsernamePasswordAuthenticationToken userPassToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        userPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(userPassToken);
      }
    }

    filterChain.doFilter(request, response);
  }

}
