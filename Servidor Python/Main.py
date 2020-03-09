# -*- coding: utf-8 -*-
import json
import time
import threading
from ArduinoCom import ArduinoCom
from Queue import Queue
from MyBDConnector import MyBDConnector
from Lavanderia import Lavanderia
from Piscina import Piscina
from Entrada import Entrada
from Sala import Sala

myPiscina = Piscina()
myLavanderia = Lavanderia()
myEntrada = Entrada()
mySala = Sala()
db = MyBDConnector()
sendQueue = Queue()
receiveActuatorQueue = Queue()
receiveSensorQueue = Queue()
## Eventos para pedir a busca de dados e eventos para anunciar os dados prontos
getActuatorsEvent = threading.Event()
getSensorsEvent = threading.Event()
hasNewActuatorEvent = threading.Event()
hasNewSensorEvent = threading.Event()

## o lock para acesso das queues. Com os eventos pode ser inútil.
sendLock = threading.Lock()
receiveActuatorLock = threading.Lock()
receiveSensorLock = threading.Lock()
            
    
############### Thread de Comunicação ###############################

def i2cThread():
    global sendQueue,receiveSensorQueue,receiveActuatorQueue,getActuatorsEvent,getSensorsEvent,hasNewActuatorEvent,hasNewSensorEvent,sendLock,receiveSensorLock,receiveActuatorLock
    aActuators = ArduinoCom(0x05)
    aSensors = ArduinoCom(0x06)
    while True:
        with sendLock:
            while(sendQueue.isEmpty()!= True):
                mJob = sendQueue.pop()
                try:
                    aActuators.writeString(mJob)
                except:## Falhou, re-enviar.
                    print "Falhou em enviar tarefa"
                    time.sleep(0.1)
                    sendQueue.push(mJob)
                    
        if(getActuatorsEvent.isSet()):
            with receiveActuatorLock:
                #time.sleep(3)
                mJson = aActuators.readJson()
                if not mJson:
                    pass#print "readActuators retornou null"
                else:
                    #mJson = json.dumps({'Actuator':mJson}, separators=(',',':'))
                    #print mJson
                    #mJson = json.loads(mJson)
                    receiveActuatorQueue.clear()
                    receiveActuatorQueue.push(mJson)
                    if not hasNewActuatorEvent.isSet():
                        hasNewActuatorEvent.set()
                    getActuatorsEvent.clear()
        if(getSensorsEvent.isSet()):
            with receiveSensorLock:
                mJson = aSensors.readJson()
                if not mJson:
                    pass#print "readSensors retornou null"
                else:
                    #mJson = json.dumps({'Sensors':mJson}, separators=(',',':'))
                    #mJson = json.loads(mJson)
                    receiveSensorQueue.clear()
                    receiveSensorQueue.push(mJson)
                    if not hasNewSensorEvent.isSet():
                        hasNewSensorEvent.set()
                    getSensorsEvent.clear()

########################### Tomadores de Decisão para os eventos de Leitura ##############
def checkSensorEvent():
    global db,receiveSensorQueue,receiveSensorLock,myLavanderia,myPiscina,myEntrada,mySala
    with receiveSensorLock:
        if receiveSensorQueue.size() > 0:
            mJson = receiveSensorQueue.pop()            
            if('RFID' in mJson):
                myEntrada.checkRFIDResponse(mJson['RFID'])
            if('Sala' in mJson):
                mySala.checkSalaResponse(mJson['Sala'])
            if('Lavanderia' in mJson):
                myLavanderia.checkLavanderiaResponse(mJson['Lavanderia'])
            if('Chuva' in mJson):
                mySala.checkSalaResponse(mJson['Chuva'])
                myLavanderia.checkLavanderiaResponse(mJson['Chuva'])
                myPiscina.checkPiscinaResponse(mJson['Chuva'])
                sql = "Update Casa set bChuva = %d;" % (mJson['Chuva']['bChuva'])
                db.post(sql)
        hasNewSensorEvent.clear()
            #else:
                #pass
                #deu ruim...
                #receiveQueue.push(mJson)
def checkActuatorEvent():
    global receiveActuatorQueue,receiveActuatorLock,myLavanderia,myPiscina,myEntrada,mySala
    with receiveActuatorLock:
        if receiveActuatorQueue.size() > 0:
            mJson = receiveActuatorQueue.pop()
            if('L' in mJson):
                myLavanderia.checkLavanderiaResponse(mJson['L'])
            if('P' in mJson):
                myPiscina.checkPiscinaResponse(mJson['P'])
            if('E' in mJson):
                myEntrada.checkEntradaResponse(mJson['E'])
            if('S' in mJson):
                mySala.checkSalaResponse(mJson['S'])
                #getActuatorsEvent.clear()
            #else:
                pass#receiveQueue.push(mJson)
        hasNewActuatorEvent.clear()
                
## Inicializando a thread e setando como Daemonica.
i2cT = threading.Thread(target=i2cThread)
i2cT.daemon = True
i2cT.setDaemon(True)
try:
    i2cT.start()
except (KeyboardInterrupt, SystemExit):
    cleanup_stop_thread()
    sys.exit()


########################## MAIN PROGRAM #########################
readComb = 0
myLavanderia.checkData()
myPiscina.checkData()
myEntrada.checkData()
mySala.checkData()
mSensorTime = time.time()
mAtuadorTime = time.time()
while True:
    #choice = input("Insira\n 1 - Atualizar\n 2 - Ler Status");
    choice = -1
    myLavanderia.controleLavanderia(sendQueue,sendLock,getActuatorsEvent)
    myPiscina.controlePiscina(sendQueue,sendLock,getActuatorsEvent)
    myEntrada.controleEntrada(sendQueue,sendLock,getActuatorsEvent)
    mySala.controleSala(sendQueue,sendLock,getActuatorsEvent)
    if(hasNewActuatorEvent.isSet()):
        checkActuatorEvent()
    if(hasNewSensorEvent.isSet()):
        checkSensorEvent()
    if(time.time() - mSensorTime > 1):
        if(not getSensorsEvent.isSet()):
            #pass
            getSensorsEvent.set()
        mSensorTime = time.time()
    
    if(time.time() - mAtuadorTime > 3):
        if(not getActuatorsEvent.isSet()):
            getActuatorsEvent.set()
            mAtuadorTime = time.time()
