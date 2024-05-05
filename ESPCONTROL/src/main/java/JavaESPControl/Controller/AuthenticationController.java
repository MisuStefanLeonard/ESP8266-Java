/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Controller;

import JavaESPControl.DTOModels.AccountsDTO;
import JavaESPControl.ESPConnection.ESP8266connection;
import JavaESPControl.Models.Accounts;
import JavaESPControl.Service.AccountsService;
import JavaESPControl.Service.AccountsServiceImplementation;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.Socket;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author misustefan
 */
@Controller
public class AuthenticationController {
    
    
    private final AccountsService accountService;
    
    public AuthenticationController(AccountsService acc){
        this.accountService = acc;
    }
    
    @GetMapping(value = {"/index","/"})
    public String home(){
        return "index";
    }
    
    
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        AccountsDTO account = new AccountsDTO();
        model.addAttribute("account", account);
        return "register";
    }
    
    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("account") AccountsDTO accDTO,
                                BindingResult binding, Model model ){
        
        Accounts existingAccount = accountService.findByemail(accDTO.getEmail());
        
        if(existingAccount != null && existingAccount.getEmail()!= null
                && !existingAccount.getEmail().isEmpty()){
            binding.rejectValue("email", null,"Email already exists!");
        }
        
        if(binding.hasErrors()){
            model.addAttribute("account",accDTO);
            return "/register";
        }
        accountService.saveAccount(accDTO);
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    
    
}
