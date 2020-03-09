from MyBDConnector import MyBDConnector
import json
import time
class Teto:
    def __init__(self,iIDComodo = -1):
        self.db = MyBDConnector()
        self.iIDTeto = -1
        self.cNome = ""
        self.iSetpoint = -1
        self.iPos = -1
        self.bAvancado = -1
        self.iIDDeviceStatus = ""
        self.iIDComodo = iIDComodo
        self.reSendJob = False
        self.lastJob = ""
        self.bChuva = 0

    def setiIDTeto(self,arg):
        self.iIDTeto = arg
    def setcNome(self,arg):
        self.cNome = arg
    def setiSetpoint(self,arg):
        self.iSetpoint = arg
    def setiPos(self,arg):
        self.iPos = arg
    def setbAvancado(self,arg):
        self.bAvancado = arg
    def setiIDDeviceStatus(self,arg):
        self.iIDDeviceStatus = arg
    def setiIDComodo(self,arg):
        self.iIDComodo = arg
    def setreSendJob(self,arg):
        self.reSendJob = arg
    def setLastJob(self,arg):
        self.lastJob = arg
    def setbChuva(self,arg):
        self.bChuva = arg
        
    def getiIDTeto(self):
        return self.iIDTeto
    def getcNome(self):
        return self.cNome
    def getiSetpoint(self):
        return self.iSetpoint
    def getiPos(self):
        return self.iPos
    def getbAvancado(self):
        return self.bAvancado
    def getiIDDeviceStatus(self):
        return self.iIDDeviceStatus
    def getiIDComodo(self):
        return self.iIDComodo
    def getreSendJob(self):
        return self.reSendJob
    def getLastJob(self):
        return self.lastJob
    def getbChuva(self):
        return self.bChuva
    
    def compareData(self,arg1):
        aux = True
        if(self.iSetpoint != arg1.getiSetpoint()):
            aux = False
        if(self.iPos != arg1.getiPos()):
            aux = False
        if(self.bAvancado != arg1.getbAvancado()):
            aux = False
        return aux
     
    def updateData(self):
        sql = "update Teto set iSetpoint = %d, iPos = %d, bAvancado = %d where iIDTeto = %d;" % (self.getiSetpoint(),self.getiPos(),self.getbAvancado(),self.getiIDTeto())
        self.db.post(sql)

    def checkTeto(self,arg = ""):
        sql = "Select iIDTeto,cNome,iSetpoint,iPos,bAvancado,iIDDeviceStatus,iIDComodo from Teto%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            
            for row in rows:
                self.setiIDTeto(row[0])
                self.setcNome(row[1])
                print "\tTeto Encontrado : %s" % (self.getcNome())
                self.setiSetpoint(row[2])
                self.setiPos(row[3])
                self.setbAvancado(row[4])
                self.setiIDDeviceStatus(row[5])
                self.setiIDComodo(row[6])

    def checkTetoResponse(self,tetoJson):
        if('P' in tetoJson):
            self.setiPos(tetoJson['P'])
        if('SP' in tetoJson):
            if(self.getiSetpoint() != tetoJson['SP']):
                print "\tAtualizando Teto SP"
                self.setreSendJob(True)
        if('bChuva' in tetoJson):
            self.setbChuva(tetoJson['bChuva'])
            
        self.updateData()

    def checkUserTetoLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserTetoLog, bOpen, bAvancado,bNewAvancado from UserTetoLog where bPending = 0 and iIDTeto = %d;" % (self.getiIDTeto())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Teto()
                iIDUserTetoLog = row[0]
                aux.setiSetpoint(row[1])
                print "Found new TetoLog. Setpoint = %i" % row[1]
                aux.setbAvancado(row[2])
                #aux.setiIDTeto(row[4])
                if not self.compareData(aux):
                    myName = 'T'
                    encoded = json.dumps({cComodo:{myName:{'SP':aux.getiSetpoint()}}}, separators=(',',':'))
                    self.setLastJob(encoded)
                    with sendLock:
                        sendQueue.push(encoded)
                    self.setiSetpoint(aux.getiSetpoint())
                    self.setbAvancado(aux.getbAvancado())

                sqlUpdate = "Update Teto set iSetpoint = %d, bAvancado = %d where iIDTeto = %d;" % (self.getiSetpoint(), self.getbAvancado(), self.getiIDTeto())
                #print "Atualizando Janela : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                sqlUpdate = "Update UserTetoLog set bPending = 1 where iIDUserTetoLog = %d;" % iIDUserTetoLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                
    def controleTeto(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.checkUserTetoLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    self.setreSendJob(False)
                    time.sleep(0.1)
                else:
                    myName = 'T'
                    encoded = json.dumps({cComodo:{myName:{'SP':self.getiSetpoint()}}}, separators=(',',':'))
                    self.setLastJob(encoded)
        if(self.getiPos() != self.getiSetpoint()):
            print "Checking Actuators by Teto"
            if not getActuatorsEvent.isSet():
                getActuatorsEvent.set()
        if (self.getbChuva() == 1):# and self.getbAvancado() == 1):
            #verifica se o Teto encontra-se fechado, senao fecha-o
            if (self.getiSetpoint() != 0): ##Mudar para 100 se for o contrario
                sqlUpdate = "Update UserTetoLog set bPending = 1;"
                self.db.post(sqlUpdate) #Cancela qualquer pedido anterior.
                sql = "Insert into UserTetoLog (bOpen,bAvancado,bNewAvancado,iIDTeto,iIDUsuario) values (%d,%d,%d,%d,%d);" % (0,1,0,self.getiIDTeto(),1)
                self.db.post(sql)
                self.checkUserTetoLog(sendQueue,sendLock,cComodo)

