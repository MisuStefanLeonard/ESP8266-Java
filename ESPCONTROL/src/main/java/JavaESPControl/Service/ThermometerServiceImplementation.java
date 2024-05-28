/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Service;

import JavaESPControl.ESPConnection.ESP8266connection;
import JavaESPControl.Models.ThermometerData;
import JavaESPControl.Repository.ThermometerDataRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Date;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThermometerServiceImplementation implements ThermometerService {

    private final ThermometerDataRepository infoRepo;
    private ESP8266connection espConn;

    @Autowired
    public ThermometerServiceImplementation(ThermometerDataRepository repo) {
        infoRepo = repo;
    }

    public CompletableFuture<Socket> getAlreadyOpenedConnectionAsync() throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                espConn = new ESP8266connection();
               
                return espConn.getConnection();
            } catch (IOException e) {
                throw new RuntimeException("Error while getting connecting", e);
            }
        });
    }
    @Override
    public CompletableFuture<String> getMessageFromESP(Socket connSocket) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean messageFlag = true;
                String OUTmessage = "";
                while(messageFlag){
                    StringBuilder messageBuild = new StringBuilder();
                   
                    InputStreamReader input = new InputStreamReader(connSocket.getInputStream());
                    
                    BufferedReader bf_reader = new BufferedReader(input);
                    
                    boolean readerFlag = true;
                    while(readerFlag){
                        try{
                            if(bf_reader.ready()){
                               
                                String messageReceived = bf_reader.readLine();
                               
                                messageBuild.append(messageReceived);
                                System.out.println(messageBuild.toString());
                                readerFlag = false;
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                            readerFlag = true;
                        }
                    }
                    
                   
                    String message = messageBuild.toString();
                   
                    if(message.isEmpty()){
                        System.out.println("empty message");
                    }else{
                        OUTmessage = message;
                        messageFlag = false;
                    }
                }
                return OUTmessage;
            } catch (IOException e) {
                throw new RuntimeException("Error while reading message from ESP", e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<Void> save(ThermometerData data) throws IOException{
        return getAlreadyOpenedConnectionAsync().thenCompose(connSocket -> {
            try {
                
                if (connSocket.isConnected()) {
                    
                    if (connSocket.isClosed()) {
                        throw new Error("Connection closed");
                    } else {
                        
                        return getMessageFromESP(connSocket).thenApply(message -> {
                            System.out.println(message);
                            ThermometerData dataToBeInserted = new ThermometerData();
                            String[] tempAndHumi = message.split(" "); // it will be in the format: Temperature:253 Humidity:253
                            Integer temperature = Integer.parseInt(tempAndHumi[0]);
                            BigDecimal humidity = BigDecimal.valueOf(Double.parseDouble(tempAndHumi[1]));
                            long millis = System.currentTimeMillis();
                            java.sql.Date sqlDate = new Date(millis);
                            dataToBeInserted.setTemperature(temperature);
                            dataToBeInserted.setHumidity(humidity);
                            dataToBeInserted.setDate(sqlDate);
                            infoRepo.save(dataToBeInserted);
                            return null; 
                        });
                    }
                }
                return null; 
            } catch (Exception e) {
                e.printStackTrace();
                return null; 
            }
        });
    }
}
