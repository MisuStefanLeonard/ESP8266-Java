#include <LiquidCrystal.h>

#include <ESP8266WiFi.h>
#include <Wire.h>
#include <DHT11.h>
#include <LiquidCrystal_I2C.h>


LiquidCrystal_I2C lcd(0x27,16,2);
DHT11 dht11(2);

int LedPin = 0;
int port = 5055;
//const char* ssid = "TP-Link_0E3C";
const char* ssid = "misu";
// const char* ssid = "Misu";
//const char* password = "66976578";
const char* password = "misu30@12";
// const char* password = "FamiliaMisu21";
WiFiServer server(port);

void sendSensor(){
  int h = 0;
  int t = 0;
  t = dht11.readTemperature();
  h = dht11.readHumidity();
  Serial.println(t);
  Serial.println(h);
  if(isnan(t) || isnan(h)){
    Serial.println("Failed to read from DHT sensor");
    return;
  }
  lcd.begin(16,2);
  lcd.setCursor(0, 0);
  lcd.print("Temp : ");
  lcd.print(t);
  lcd.setCursor(0,1);
  lcd.print("Humi : ");
  lcd.print(h);
  lcd.print("%");
  // return tempAndHumidity;
}

void setup() {
  // put your setup code here, to run once:
  pinMode(LedPin, OUTPUT);

  Serial.begin(9600);
  Serial.println();
  Serial.print("Connecting to Wi-Fi -> ");
  Serial.print(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid,password);

  Serial.println();
  Serial.print("Connecting");

  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }

  Serial.print("Controller IP Address: ");
  Serial.print(WiFi.localIP());
  Serial.println();
  Serial.print("Connected to port: ");
  Serial.println();
  server.begin();
  Serial.print(port);
  Serial.println();
  dht11.setDelay(5000);
  //Wire.begin(D2,D1);
  lcd.init();
  lcd.backlight();
}

void loop() {
  // put your main code here, to run repeatedly:
  WiFiClient client = server.available();
  if (client) {
    if (client.connected()) {
      Serial.println("Client connected");
      digitalWrite(LedPin, HIGH);
    }
      // Read data from the client
      while(client.connected()){
        if(client.available() > 0) {
         String msg = client.readStringUntil('\n');
         if(!msg.isEmpty())
          Serial.println(msg);
        }
        //sendSensor();
        client.print(dht11.readTemperature());
        client.print(" ");
        client.print(dht11.readHumidity());
        client.print("\n");
        digitalWrite(LedPin, HIGH);
        // Send data to the client

        // if(Serial.available() == 0){
        //   String msg = Serial.readStringUntil('\n');
        //   if(!msg.isEmpty()){
        //     client.println(msg);
        //   }
        // }
        if(!client.connected()){
          client.stop();
          Serial.println("Client disconnected");
          digitalWrite(LedPin, LOW);
        }
      }
  }
}
