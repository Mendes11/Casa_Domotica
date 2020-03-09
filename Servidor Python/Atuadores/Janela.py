# -*- coding: utf-8 -*-
from MyBDConnector import MyBDConnector
import json
import time
class Janela:
    def __init__(self):
        self.db = MyBDConnector()
        self.iIDJanela = -1
        self.cNome = ""
        self.iSetpoint = -1
        self.iPos = -1
        self.bAvancado = -1
        self.cDeviceStatus = ""
        self.reSendJob = False
        self.lastJob = ""
        self.bChuva = 0
        self.cComodo = ""
        
    def setiIDJanela(self,arg):
        self.iIDJanela = arg
    def setcNome(self,arg):
        self.cNome = arg
    def setiSetpoint(self,arg):
        self.iSetpoint = arg
    def setiPos(self,arg):
        self.iPos = arg
    def setbAvancado(self,arg):
        self.bAvancado = arg
    def setcDeviceStatus(self,arg):
        self.cDeviceStatus = arg
    def setbChuva(self,arg):
        self.bChuva = arg
    def setreSendJob(self,arg):
        self.reSendJob = arg
    def setLastJob(self,arg):
        self.lastJob = arg
    def setcComodo(self,arg):
        self.cComodo = arg
        
    def getiIDJanela(self):
        return self.iIDJanela
    def getcNome(self):
        return self.cNome
    def getiSetpoint(self):
        return self.iSetpoint
    def getiPos(self):
        return self.iPos
    def getbAvancado(self):
        return self.bAvancado
    def getcDeviceStatus(self):
        return self.cDeviceStatus
    def getbChuva(self):
        return self.bChuva
    def getreSendJob(self):
        return self.reSendJob
    def getLastJob(self):
        return self.lastJob
    def getcComodo(self):
        return self.cComodo
    
    def compareData(self,arg1):
        aux = True
        if(self.iSetpoint != arg1.getiSetpoint()):
            aux = False
        #if(self.iPos != arg1.getiPos()):
            #aux = False
        if(self.bAvancado != arg1.getbAvancado()):
            aux = False
        return aux
    
    def updateData(self):
        sql = "update Janela set isetPoint = %d, iPos = %d, bAvancado = %d where iIDJanela = %d;" % (self.getiSetpoint(),self.getiPos(),self.getbAvancado(),self.getiIDJanela())
        self.db.post(sql)

    def checkJanela(self,arg=""):
        sql = "Select Janela.iIDJanela,Janela.cNome,Janela.iSetpoint,Janela.iPos,Janela.bAvancado,DeviceStatus.cNome as 'cDeviceStatus' from Janela inner join DeviceStatus on DeviceStatus.iIDDeviceStatus = Janela.iIDDeviceStatus%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            for row in rows:
                self.setiIDJanela(row[0])
                self.setcNome(row[1])
                print "\tJanela Encontrada : %s" % (self.getcNome())
                self.setiSetpoint(row[2])
                self.setiPos(row[3])
                self.setbAvancado(row[4])
                self.setcDeviceStatus(row[5])

    def checkJanelaResponse(self,janelaJson):
        if 'SP' in janelaJson:
            if self.getiSetpoint() != janelaJson['SP']:
                self.createJob()
        if 'P' in janelaJson:
            print "Updating Janela iPos = %d" % janelaJson['P']
            self.setiPos(janelaJson['P'])
        if 'bChuva' in janelaJson:
            self.setbChuva(janelaJson['bChuva'])
        self.updateData()

    def checkUserJanelaLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserJanelaLog, iSetpoint, bAvancado,bNewAvancado,iIDJanela from UserJanelaLog where bPending = 0;"
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Janela()
                iIDUserJanelaLog = row[0]
                aux.setiSetpoint(row[1])
                print "Found new JanelaLog. Setpoint = %i" % row[1]
                aux.setbAvancado(row[2])
                aux.setiIDJanela(row[4])
                if not self.compareData(aux):
                    encoded = json.dumps({cComodo:{'J':{'SP':aux.getiSetpoint()}}}, separators=(',',':'))
                    with sendLock:
                        sendQueue.push(encoded)
                    self.setiSetpoint(aux.getiSetpoint())
                    self.setbAvancado(aux.getbAvancado())
                sqlUpdate = "Update UserJanelaLog set bPending = 1 where iIDUserJanelaLog = %d;" % iIDUserJanelaLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                sqlUpdate = "Update Janela set iSetpoint = %d, bAvancado = %d where iIDJanela = %d;" % (self.getiSetpoint(), self.getbAvancado(), self.getiIDJanela())
                #print "Atualizando Janela : %s" % sqlUpdate
                self.db.post(sqlUpdate)

    def createJob(self):
        myName = 'J'
        encoded = json.dumps({self.getcComodo():{myName:{'SP':self.getiSetpoint()}}}, separators=(',',':'))
        self.setLastJob(encoded)
        self.setreSendJob(True)
    def controleJanela(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.setcComodo(cComodo) ## Preguica de colocar isso no init
        self.checkUserJanelaLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    self.setreSendJob(False)
                    time.sleep(0.1)
                else:
                    myName = 'SP'
                    encoded = json.dumps({cComodo:{myName:{'SP':self.getiSetpoint()}}}, separators=(',',':'))
                    self.setLastJob(encoded)
        if(self.getiPos() != self.getiSetpoint()):
            print "Checking Actuators by Janela, my Pos = %d, my SP = %d" % (self.getiPos(),self.getiSetpoint())
            if not getActuatorsEvent.isSet():
                getActuatorsEvent.set()
        if (self.getbChuva() == 1):# and self.bAvancado() == 1):
            #verifica se a janela encontra-se fechada, senao fecha-a
            if (self.getiSetpoint() != 0): ##Mudar para 100 se for o contrario
                sqlUpdate = "Update UserJanelaLog set bPending = 1;"
                self.db.post(sqlUpdate) #Cancela qualquer pedido anterior.
                sql = "Insert into UserJanelaLog (iSetpoint,bAvancado,bNewAvancado,iIDJanela,iIDUsuario) values (%d,%d,%d,%d,%d);" % (0,1,0,self.getiIDJanela(),1)
                self.db.post(sql)
                self.checkUserJanelaLog(sendQueue,sendLock,cComodo)
