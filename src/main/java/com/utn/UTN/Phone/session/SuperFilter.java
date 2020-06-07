package com.utn.UTN.Phone.session;

import com.utn.UTN.Phone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.utn.UTN.Phone.model.User.Type.cliente;
import static com.utn.UTN.Phone.model.User.Type.empleado;

@Service
public class SuperFilter extends OncePerRequestFilter {

        @Autowired
        private SessionManager sessionManager;

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            String sessionToken = request.getHeader("Authorization");
            Session session = sessionManager.getSession(sessionToken);

            if(session==null) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
            else{
                User user= session.getLoggedUser();
                if(user.getUserType()==empleado) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }
        }
}

