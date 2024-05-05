/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

/**
 *
 * @author misustefan
 */
@Service
public class AuthSuccesHandler implements AuthenticationSuccessHandler{
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) 
    throws IOException, ServletException{
        
        // current session
        HttpSession session = request.getSession();
        // userInfo
        User authUser = (User) SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getPrincipal();
        // SETTING username and authorities
        session.setAttribute("username", authUser.getUsername());
        session.setAttribute("authorities", authUser.getAuthorities());
        
        // status
        response.setStatus(HttpServletResponse.SC_OK);
        
        // redirect to the wanted page after login
        response.sendRedirect("mainpage");
        
    }
}
