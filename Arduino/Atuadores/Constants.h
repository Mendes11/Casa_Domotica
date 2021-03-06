#define ARDUINO_ATUADORES_ADDRESS 5
#define RECEIVE_AMOUNT  "AMOUNT"
#define JSON_CHAR_BUFFER  500
#define RELAY_ACTIVATE_LOGIC LOW //Ele é por padrão NC
#define RELAY_DEACTIVATE_LOGIC HIGH
#define DEVICE_ABRINDO 1
#define DEVICE_FECHANDO 2
#define DEVICE_ABERTO 4
#define DEVICE_FECHADO 5
#define DEVICE_PRONTO 7
#define DEVICE_MOVENDO 6
#define DEVICE_OBSTRUIDO 3

// #### TOMADAS CONSTANTS
#define TOMADAS_JSON_ID "T"
#define TOMADAS_NUM 3
#define TOMADAS_DIGITAL_PORTS {50,51,52}
#define TOMADAS_JSON_JOB_ARRAY_ID "data"

// #### JANELA CONSTANTS
#define JANELA_JSON_ID "J"
#define JANELA_SETPOINT "SP"
#define JANELA_POS  "P"
#define JANELA_PWM 70

// #### PORTA CONSTANTS
#define PORTA_JSON_ID "PA"
#define PORTA_JSON_JOB_OPEN "O"
#define PORTA_JSON_JOB_TIME "F"
#define PORTA_MANUAL_OPEN_PORT_LOGIC LOW
#define PORTA_JSON_DEVICE_STATUS "D"

// #### PORTÃO CONSTANTS
#define PORTAO_JSON_ID "PO"
#define PORTAO_JSON_JOB "O"
#define PORTAO_JSON_JOB_SET "bSetOpen"
#define PORTAO_JSON_JOB_POS "P"
#define PORTAO_JSON_DEVICE_STATUS "D"
#define PORTAO_JSON_JOB_TIME "F"
#define PORTAO_PWM 100
#define PORTAO_EDNSTOP_LOGIC_CLICKED HIGH
#define PORTAO_EDNSTOP_LOGIC_CLICKED_REVERSE LOW

// #### TETO CONSTANTS
#define TETO_JSON_ID "T"
#define TETO_JSON_JOB "SP"
#define TETO_EDNSTOP_LOGIC_CLICKED LOW
#define TETO_PWM 100
#define TETO_POSITION_ID "P"

// #### LUMINOSIDADE CONSTANTS
#define LUMINOSIDADE_JSON_ID "Lu"
#define LUMINOSIDADE_JOB  "SP"
#define LUMINOSIDADE_SENSOR "S"
#define LUMINOSIDADE_CONTROLE "C"
#define LUMINOSIDADE_MANUAL_ON  "M"
#define LUMINOSIDADE_CALIBRATION "bCalibration"
#define LUMINOSIDADE_LED_PORT 
#define LUMINOSIDADE_SENSOR1_PORT
#define LUMINOSIDADE_SENSOR2_PORT

// #### PISCINA CONSTANTS
#define PISCINA_JSON_ID "P"
#define PISCINA_PASSOS 3
#define PISCINA_LDR A1
#define PISCINA_LED_TETO_PORT 1 //MODIFICAR
#define PISCINA_LED_AGUA_PORT 1 //MODIFICAR
#define PISCINA_OPEN_LOGIC 0

// ### SALA CONSTANTS
#define SALA_JSON_ID "S"
#define SALA_LDR 8
#define SALA_LED_PORT 44 //MODIFICAR
#define SALA_JANELA_MOTOR_NUM  1
#define SALA_JANELA_ANALOG_PORT  9

// ### LAVANDERIA CONSTANTS
#define LAVANDERIA_JSON_ID "L"
#define LAVANDERIA_TETO_MOTOR_NUM 4
#define LAVANDERIA_TETO_ENDSTOP_OPEN_PORT 49 //MODIFICAR
#define LAVANDERIA_TETO_ENDSTOP_CLOSED_PORT 48 //MODIFICAR

// ### ENTRADA CONSTANTS
#define ENTRADA_JSON_ID "E"
#define ENTRADA_PORTA_MANUAL_OPEN_PORT 34 // MODIFICAR
#define ENTRADA_PORTA_MANUAL false
#define ENTRADA_PORTA_DIGITAL_PORT  53
#define ENTRADA_PORTAO_MOTOR_NUM 3
#define ENTRADA_PORTAO_ENDSTOP_OPEN_PORT 28
#define ENTRADA_PORTAO_ENDSTOP_CLOSED_PORT 30 // Modificar
