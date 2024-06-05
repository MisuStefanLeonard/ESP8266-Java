/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Controller;

import JavaESPControl.ESPConnection.ESP8266connection;
import JavaESPControl.Models.ThermometerData;
import JavaESPControl.Service.ThermometerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Date;
import java.util.concurrent.CompletableFuture;
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
public class ThermormeterController {
    
    private final ThermometerService thermometerService;
    private ESP8266connection espConn;
    
    public ThermormeterController(ThermometerService _thermometerService){
        
        thermometerService = _thermometerService;
    }
    
    
    @GetMapping("/mainpage")
    public String welcome(@AuthenticationPrincipal UserDetails userDetails 
                        , Model model , HttpServletRequest request , HttpSession session) throws IOException{
        espConn = new ESP8266connection();
        ThermometerData dataToBeDisplayed = new ThermometerData();
        ThermometerData dataToBeInserted = new ThermometerData();
        model.addAttribute("userdetails", userDetails);
        model.addAttribute("ESPDATA", dataToBeDisplayed);
        Socket connSocket = espConn.getConnection();
       
        thermometerService.save(dataToBeDisplayed).join(); // .join() pentru ca primirea mesajului sa nu fie obiectul in sine
                                                               // si pentru asteptarea terminarii thread-ului.
        
        // message received
        CompletableFuture<String> message = thermometerService.getMessageFromESP(connSocket);
        // messageProcessed
        // waiting for the finishing of the async fu
        String resultFromMessage = message.join();
        // final result . EX -> 253 253 ( first digits until space is the temperature and the bits after space are the humidity
        String[] tempAndHumi = resultFromMessage.split(" ");
        long millis = System.currentTimeMillis();
        java.sql.Date dateNow = new Date(millis);
        dataToBeDisplayed.setDate(dateNow);
        
        System.out.println(tempAndHumi[0].toString());
        System.out.println(tempAndHumi[1].toString());
        dataToBeDisplayed.setTemperature(Integer.parseInt(tempAndHumi[0].toString()));
        dataToBeDisplayed.setHumidity(BigDecimal.valueOf(Double.parseDouble(tempAndHumi[1].toString())));
        
        if(connSocket != null){
            String referer = request.getHeader("Referer");
            session.setAttribute("previousUrl", referer);
            
            return "mainpage";
        }
        
        return  "redirect:/error";
        
    }
    
    
    private boolean isRefreshed(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        String currentURL = request.getRequestURL().toString();
        return referer != null && referer.equals(currentURL);
    }
    
    @GetMapping("/error")
    public String error(Model model, HttpSession session,HttpServletRequest request){
        String previousUrl = (String) session.getAttribute("previousUrl");
        if(previousUrl != null && !previousUrl.equals("/login")&& isRefreshed(request)){
            session.removeAttribute("previousUrl");
            return "redirect:" + previousUrl;
        }
        return "error";
    }
    
    
    
   
    
}
