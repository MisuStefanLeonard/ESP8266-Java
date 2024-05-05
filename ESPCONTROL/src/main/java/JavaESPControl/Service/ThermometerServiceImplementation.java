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
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                System.out.println("aici1212323");
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
                System.out.println("inainte de loop in getMessageFromESP");
                while(messageFlag){
                    StringBuilder messageBuild = new StringBuilder();
                    //System.out.println(" in loop getMessageFromESP");
                    InputStreamReader input = new InputStreamReader(connSocket.getInputStream());
                    //System.out.println(" in loop getMessageFromESP linia 58");
                    BufferedReader bf_reader = new BufferedReader(input);
                    //System.out.println(" in loop getMessageFromESP linia 59");
//                    while(bf_reader.read() >= 0 && bf_reader.read() <= 65535){
//                        System.out.println("inainte de ready");
//                        bf_reader.ready();
//                        System.out.println("dupa ready");
//                        int INTcharacter = bf_reader.read();
//                        char character = (char) INTcharacter;
//                        messageBuild.append(character);
//                        System.out.println(messageBuild);
//                    }
                    boolean readerFlag = true;
                    while(messageFlag){
                        try{
                            if(bf_reader.ready()){
                                System.out.println("aici la ready");
                                String messageReceived = bf_reader.readLine();
                                System.out.println("dupa readline");
                                messageBuild.append(messageReceived);
                                System.out.println(messageBuild.toString());
                                messageFlag = false;
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                            messageFlag = true;
                        }
                    }
                    
                    System.out.println("mesajul -> " + messageBuild.toString());
                    System.out.println("am ajuns aici");
                    String message = messageBuild.toString();
                    System.out.println("citeste linia in getMessageFromESP");
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
                System.out.println("connection opened here");
                if (connSocket.isConnected()) {
                    System.out.println("after isconnected condition ");
                    if (connSocket.isClosed()) {
                        throw new Error("Connection closed");
                    } else {
                        System.out.println("after isconnected condition2 ");
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
