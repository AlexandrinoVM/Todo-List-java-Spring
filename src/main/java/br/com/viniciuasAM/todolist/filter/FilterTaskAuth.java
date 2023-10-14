package br.com.viniciuasAM.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.viniciuasAM.todolist.user.IUuserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends  OncePerRequestFilter {
    @Autowired
    private IUuserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var serveletPath = request.getServletPath(); 

                if(serveletPath.startsWith("/tasks/")){

            var authorization = request.getHeader("authorization");
            System.out.println("autorizathion");
            var user_password = authorization.substring("Basic".length()).trim();

            byte[] authDECODE =Base64.getDecoder().decode(user_password);
            var authString = new String (authDECODE);
            String[] credentials = authString.split(":");
            String password = credentials[1];
            String username = credentials[0];
            System.out.println(password);
            System.out.println(username);
            
            var user =this.userRepository.findByUsername(username);
            if(user== null){
                response.sendError(401, "usuario sem autorização");
            }else{
                var passwordVerify =BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());
                if(passwordVerify.verified){
                    request.setAttribute("idUser",user.getId());
                    filterChain.doFilter(request, response);
                }else{
                    response.sendError(401);
            }

        
    }
    }else{
         filterChain.doFilter(request, response);
    }   
}
            
         
}
