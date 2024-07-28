#include "DHT.h"
enum CONTROL_PINS{
  TEMPER_HUMID = A0,
  RED_LED = 8U,
  BLUE_LED,
  GREEN_LED
};
// 매크로 정의로 핀 번호를 쉽게 참조
#define RGB_RED RED_LED
#define RGB_GREEN GREEN_LED
#define RGB_BLUE BLUE_LED

class DHT dht(TEMPER_HUMID, 11);
void setup() {
  // put your setup code here, to run once:
  dht.begin();
  Serial.begin(115200UL);
  Serial1.begin(9600UL);
  pinMode(TEMPER_HUMID, INPUT);
  pinMode(RED_LED, OUTPUT);
  pinMode(BLUE_LED, OUTPUT);
  pinMode(GREEN_LED, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(dht.read()){
    const float temperature {dht.readTemperature()};
    const float humidity {dht.readHumidity()};
    if(Serial.available()){
      String in_comming_String {Serial.readStringUntil('\n')};
      in_comming_String.trim();  // \r, \n 잘라낸다. 순수값만 받아낸다.
      if(in_comming_String.equals("RED_LED_ON")){
        digitalWrite(RED_LED, HIGH);
        digitalWrite(RGB_RED, HIGH);
      }else if(in_comming_String.equals("RED_LED_OFF")){
        digitalWrite(RED_LED, LOW);
        digitalWrite(RGB_RED, LOW);
      }else if(in_comming_String.equals("BLUE_LED_ON")){
        digitalWrite(BLUE_LED, HIGH);
        digitalWrite(RGB_BLUE, HIGH);
      }else if(in_comming_String.equals("BLUE_LED_OFF")){
        digitalWrite(BLUE_LED, LOW);
        digitalWrite(RGB_BLUE, LOW);
      }else if(in_comming_String.equals("GREEN_LED_ON")){
        digitalWrite(GREEN_LED, HIGH);
        digitalWrite(RGB_GREEN, HIGH);
      }else if(in_comming_String.equals("GREEN_LED_OFF")){
        digitalWrite(GREEN_LED, LOW);
        digitalWrite(RGB_GREEN, LOW);
      }else{}
    }
    const String sending_data {String(temperature) + ',' + String(humidity)};  //csv파일
    Serial.println(sending_data);
    delay(100UL);
  }
}
