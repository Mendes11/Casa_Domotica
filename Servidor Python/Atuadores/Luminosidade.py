from MyBDConnector import MyBDConnector
import json
import time
class Luminosidade:
    def __init__(self,iIDComodo = -1):
        self.db = MyBDConnector()
        self.iIDLuminosidade = -1
        self.iSetpoint = -1
        self.iSensor = -1
        self.bControle = -1
        self.bManualOn = -1
        self.iIDComodo = iIDComodo
        self.reSendJob = 0
        self.lastJob = ""

    def setiIDLuminosidade(self,arg):
        self.iIDLuminosidade = arg
    def setiSetpoint(self,arg):
        self.iSetpoint = arg
    def setiSensor(self,arg):
        self.iSensor = arg
    def setbControle(self,arg):
        self.bControle = arg
    def setbManualOn(self,arg):
        self.bManualOn = arg
    def setiIDComodo(self,arg):
        self.iIDComodo = arg
    def setreSendJob(self,arg):
        self.reSendJob = arg
    def setLastJob(self,arg):
        self.lastJob = arg
        
    def getiIDLuminosidade(self):
        return self.iIDLuminosidade
    def getiSetpoint(self):
        return self.iSetpoint
    def getiSensor(self):
        return self.iSensor
    def getbControle(self):
        return self.bControle
    def getbManualOn(self):
        return self.bManualOn
    def getiIDComodo(self):
        return self.iIDComodo
    def getreSendJob(self):
        return self.reSendJob
    def getLastJob(self):
        return self.lastJob
    
    def compareData(self,arg1):
        aux = True
        if(self.iSetpoint != arg1.getiSetpoint()):
            aux = False
        if(self.iSensor != arg1.getiSensor()):
            aux = False
        if(self.bControle != arg1.getbControle()):
            aux = False
        if(self.bManualOn != arg1.getbManualOn()):
            aux = False
        return aux
     
    def updateData(self):
        sql = "update Luminosidade set iSetpoint = %d, iSensor = %d, bControle = %d, bManualOn = %d where iIDLuminosidade = %d;" % (self.getiSetpoint(),self.getiSensor(),self.getbControle(),self.getbManualOn(),self.getiIDLuminosidade())
        self.db.post(sql)

    def checkLuminosidade(self,arg = ""):
        sql = "Select iIDLuminosidade,iSetpoint,iSensor,bControle,bManualOn,iIDComodo from Luminosidade%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            
            for row in rows:
                self.setiIDLuminosidade(row[0])
                print "\tLuminosidade Encontrada"
                self.setiSetpoint(row[1])
                self.setiSensor(row[2])
                self.setbControle(row[3])
                self.setbManualOn(row[4])
                self.setiIDComodo(row[5])

    def checkLuminosidadeResponse(self,mJson):
        if('S' in mJson):
            self.setiSensor(mJson['S']/100)
        if('SP' in mJson):
            if(self.getiSetpoint() != mJson['SP']):
                self.setreSendJob(True)
        self.updateData()

    def checkUserLuminosidadeLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserLuminosidadeLog, iSetpoint, bControle,bManualOn from UserLuminosidadeLog where bPending = 0 and iIDLuminosidade = %d;" % (self.getiIDLuminosidade())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Luminosidade()
                iIDUserLuminosidadeLog = row[0]
                aux.setiSetpoint(row[1])
                print "Found new LuminosidadeLog. Setpoint = %i" % row[1]
                aux.setbControle(row[2])
                aux.setbManualOn(row[3])
                if(aux.getbControle() == 1):
                    aux.setiSetpoint = aux.getbManualOn()*100
                if not self.compareData(aux):
                    myName = 'Lu'
                    encoded = json.dumps({cComodo:{myName:{'SP':aux.getiSetpoint(),'C':aux.getbControle(),'M':aux.getbManualOn()}}}, separators=(',',':'))
                    self.setLastJob(encoded)
                    with sendLock:
                        sendQueue.push(encoded)
                    self.setiSetpoint(aux.getiSetpoint())
                    self.setbControle(aux.getbControle())
                    self.setbManualOn(aux.getbManualOn())
                sqlUpdate = "Update Luminosidade set iSetpoint = %d, bControle = %d, bManualOn = %d where iIDLuminosidade = %d;" % (self.getiSetpoint(), self.getbControle(),self.getbManualOn(), self.getiIDLuminosidade())
                #print "Atualizando Janela : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                sqlUpdate = "Update UserLuminosidadeLog set bPending = 1 where iIDUserLuminosidadeLog = %d;" % iIDUserLuminosidadeLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                
    def controleLuminosidade(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.checkUserLuminosidadeLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    self.setreSendJob(False)
                    time.sleep(0.1)
                else:
                    myName = 'Lu'
                    encoded = json.dumps({cComodo:{myName:{'SP':self.getiSetpoint(),'C':self.getbControle(),'M':self.getbManualOn()}}}, separators=(',',':'))
                    self.setLastJob(encoded)
        if(self.getiSensor() != self.getiSetpoint() and self.getbControle == 0):
            print "checkingActuators by Luz"
            if not getActuatorsEvent.isSet():
                getActuatorsEvent.set()
