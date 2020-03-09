from MyBDConnector import MyBDConnector
import json
import struct
class Porta:
    def __init__(self,iIDComodo = -1):
        self.db = MyBDConnector()
        self.iIDPorta = -1
        self.bOpen = -1
        self.fAutoCloseTime = -1.0
        self.iIDDeviceStatus = -1
        self.iIDComodo = iIDComodo
        self.reSendJob = 0
        self.lastJob = ""

    def setiIDPorta(self,arg):
        self.iIDPorta = arg
    def setbOpen(self,arg):
        self.bOpen = arg
    def setfAutoCloseTime(self,arg):
        self.fAutoCloseTime = arg
    def setiIDDeviceStatus(self,arg):
        self.iIDDeviceStatus = arg
    def setiIDComodo(self,arg):
        self.iIDComodo = arg
    def setreSendJob(self,arg):
        self.reSendJob = arg
    def setLastJob(self,arg):
        self.lastJob = arg
        
    def getiIDPorta(self):
        return self.iIDPorta
    def getbOpen(self):
        return self.bOpen
    def getfAutoCloseTime(self):
        return self.fAutoCloseTime
    def getiIDDeviceStatus(self):
        return self.iIDDeviceStatus
    def getiIDComodo(self):
        return self.iIDComodo
    def getreSendJob(self):
        return self.reSendJob
    def getLastJob(self):
        return self.lastJob
    
    def compareData(self,arg1):
        aux = True
        if(self.bOpen != arg1.getbOpen()):
            aux = False
        if(self.fAutoCloseTime != arg1.getfAutoCloseTime()):
            aux = False
        return aux
     
    def updateData(self):
        sql = "update Porta set bOpen = %d, fAutoCloseTime = %f, iIDDeviceStatus = %d where iIDPorta = %d;" % (self.getbOpen(),self.getfAutoCloseTime(),self.getiIDDeviceStatus(),self.getiIDPorta())
        self.db.post(sql)

    def checkPorta(self,arg = ""):
        sql = "Select iIDPorta,bOpen,fAutoCloseTime,iIDComodo,iIDDeviceStatus from Porta%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            
            for row in rows:
                self.setiIDPorta(row[0])
                print "\tPorta Encontrada"
                self.setbOpen(row[1])
                self.setfAutoCloseTime(row[2])
                self.setiIDComodo(row[3])
                self.setiIDDeviceStatus(row[4])
                
    def checkPortaResponse(self,mJson):
        #if(self.getbOpen() is not mJson['bOpen']):
         #   self.setreSendJob(True)
        if ('F' in mJson):
            if((self.getfAutoCloseTime()*1000) != mJson['F']):
                self.setreSendJob(True)
        if ('O' in mJson):
            self.setbOpen(mJson['O'])
        if ('D' in mJson):
            self.setiIDDeviceStatus(mJson['D'])
        self.updateData()

    def checkUserPortaLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserPortaLog, bOpen,fAutoCloseTime from UserPortaLog where bPending = 0 and iIDPorta = %d;" % (self.getiIDPorta())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Porta()
                iIDUserPortaLog = row[0]
                print "Found new PortaLog. bOpen = %i" % row[1]
                aux.setbOpen(row[1])
                aux.setfAutoCloseTime(row[2])
                if not self.compareData(aux):
                    myName = 'PA'
                    #mFloat = struct.unpack("f", struct.pack("f", aux.getfAutoCloseTime()))
                    encoded = json.dumps({cComodo:{myName:{'O':aux.getbOpen(),'F':int(aux.getfAutoCloseTime()*1000)}}}, separators=(',',':'))
                    self.setLastJob(encoded)
                    with sendLock:
                        sendQueue.push(encoded)
                    self.setbOpen(aux.getbOpen())
                    self.setfAutoCloseTime(aux.getfAutoCloseTime())
                    self.setiIDDeviceStatus(4)
                #sqlUpdate = "Update Porta set iSetpoint = %d, bControle = %d, bManualOn = %d where iIDPorta = %d;" % (self.getiSetpoint(), self.getbControle(),self.getbManualOn(), self.getiIDPorta())
                #print "Atualizando Janela : %s" % sqlUpdate
                #self.db.post(sqlUpdate)
                self.updateData()
                sqlUpdate = "Update UserPortaLog set bPending = 1 where iIDUserPortaLog = %d;" % iIDUserPortaLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                
    def controlePorta(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.checkUserPortaLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    self.setreSendJob(False)
                else: ##Alguma info diferiu no start provavelmente
                    myName = 'PA'
                    #mFloat = struct.unpack("f", struct.pack("f", self.getfAutoCloseTime()))
                    encoded = json.dumps({cComodo:{myName:{'O':self.getbOpen(),'F':int(self.getfAutoCloseTime()*1000)}}}, separators=(',',':'))
                    self.setLastJob(encoded)
        if(self.getiIDDeviceStatus()!= 5):
            print "Checking Actuator by Porta"
            if not getActuatorsEvent.isSet():
                getActuatorsEvent.set()

    def openPorta(self,iIDUsuario):
        sql = "Insert into UserAccessLog (iIDUsuario) values(%d);" % (iIDUsuario)
        print sql
        self.db.post(sql)
        sql = "Insert into UserPortaLog (bOpen,fAutoCloseTime,iIDPorta,iIDUsuario) values (%d,%f,%d,%d);" % (1,self.getfAutoCloseTime(),self.getiIDPorta(),iIDUsuario)
        print sql
        self.db.post(sql)
