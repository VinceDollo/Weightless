#include "WiFi.h"
#include <EEPROM.h>
#include <ArduinoJson.h>
#include "HX711.h"

#define EEPROM_SIZE 512
#define INDEX 10
#define PORTAP 52000
#define PORTWIFI 51000
#define calibration_factor -50000
const int broche_DT = 5;
const int broche_SCK = 4;
int broche_RST = 15;
 

HX711 scale;
float mesure = 0;
int cmpt = 0;
String message;
const char* ssid;
const char* password;
void writeString(char add,String data);
String read_String(char add);
void createAP();
void clearEEPRom();
WiFiServer server(PORTWIFI);


/*
 * 
 *  Verifier le stockage de chaine Json
 *  Faire la fonction qui vide la memoire(clear)
 *  Finir la fonction de reinitialisation
 * 
 */
void setup() {
  
  Serial.begin(115200);
  pinMode(broche_RST, INPUT_PULLDOWN);
  EEPROM.begin(EEPROM_SIZE);
  Serial.println("Le contenu actuel est" + read_String(INDEX));
  //clearEEPRom();
  
  
  if(read_String(INDEX).equals("")){
    createAP();
    WiFiServer serverAP(PORTAP);
    serverAP.begin();
    Serial.println("Attente d'un client de configuration .");
    while(1){
      WiFiClient client = serverAP.available();
      if(client){
        Serial.print("Client à l'adresse : ");
        Serial.println(client.remoteIP());
        while(client.connected()){
          while(client.available() > 0){
            message += (char)client.read();
          }
          if(message.length()!=0){
            Serial.print("Message reçu : ");
            Serial.println(message);
            writeString(INDEX, message);
            Serial.print("Le message stocké est : ");
            Serial.println(read_String(INDEX));
            message="";
            client.println("Config ok");
          }
        }
        client.stop();
        delay(200);
        serverAP.stop();
        delay(200);
        WiFi.softAPdisconnect (true);
        delay(200);
        break;
      }
    }
  }
  Serial.println("Initialisation de la balance...");
  scale.begin(broche_DT, broche_SCK);
  while(!scale.is_ready()){
    ;
  }
  scale.set_scale(20000); //le paramètre dépend de votre cellule de charge.
  scale.tare(); //ajustement du zéro

  Serial.println("La balance est prete!");
  
}
 
void loop() {
  DynamicJsonDocument doc(2048);
  deserializeJson(doc,read_String(INDEX));
  JsonObject obj = doc.as<JsonObject>();
  ssid = obj["ssid"];
  password = obj["passWiFi"];
  
  WiFi.begin(ssid, password);
  Serial.println("Connection au  WiFi");
  delay(200);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connecté au  réseau WiFi");
  Serial.print(" à l'adresse : ");
  Serial.println(WiFi.localIP());
  delay(200);
  server.begin();
  Serial.println("Serveur lancé au port: "+ String(PORTWIFI));
  while(WiFi.status() == WL_CONNECTED){
    delay(500);
    WiFiClient client = server.available();
    while(client){
      Serial.print("Client à l'adresse : ");
      Serial.println(client.remoteIP());
      while(client.connected()){
        while(client.available() > 0){
          message += (char)client.read();
        }
        if(message.length()!=0 && message.equals("get")){
          Serial.print("Message recu : ");
          Serial.println(message);
          String mesure = String(scale.get_units(10));
          Serial.println("Le poids est : " +mesure + " kg");
          client.println(mesure);
          message="";
        }
        delay(200);
      }
      client.stop();
    }
    client.stop();
    if(digitalRead(broche_RST)){ // Mettre une broche qui permettra de reinitialiser l'esp32
      clearEEPRom();
      ESP.restart();
    }
  }
  
  if(digitalRead(broche_RST)){ // Mettre une broche qui permettra de reinitialiser l'esp32
    clearEEPRom();
    ESP.restart();
  }
}


void createAP(){
  char* apName = "WeightLess";
  char* apPass = "weightLess";
  WiFi.softAP(apName);
  IPAddress IP = WiFi.softAPIP();
  Serial.print("AP IP Adresse : ");
  Serial.println(IP);
}

void writeString(char add,String data)
{
  int _size = data.length();
  int i;
  for(i=0;i<_size;i++)
  {
    EEPROM.write(add+i,data[i]);
  }
  EEPROM.write(add+_size,'\0');   //Add termination null character for String Data
  EEPROM.commit();
}


String read_String(char add)
{
  int i;
  char data[255]; //Max 100 Bytes
  int len=0;
  unsigned char k;
  k=EEPROM.read(add);
  while(k != '\0' && len<500)   //Read until null character
  {    
    k=EEPROM.read(add+len);
    data[len]=k;
    len++;
  }
  data[len]='\0';
  Serial.println("La chaine lu est : " + String(data)+ " de taille "+ len);
  return String(data);
}

void clearEEPRom(){
  Serial.println("Nettoyage de la memoire");
  for (int i = 0; i < 512; i++) {
    EEPROM.write(i, 0);
  }
}
