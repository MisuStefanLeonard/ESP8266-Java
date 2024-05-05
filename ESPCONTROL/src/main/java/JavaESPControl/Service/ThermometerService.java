/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package JavaESPControl.Service;

import JavaESPControl.Models.ThermometerData;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

/**
 *
 * @author misustefan
 */

public interface ThermometerService {
    
    public CompletableFuture<Void> save(ThermometerData data) throws IOException;
    public CompletableFuture<String> getMessageFromESP(Socket consSocket) ;
}
