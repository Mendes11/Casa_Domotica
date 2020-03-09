# -*- coding: utf-8 -*-
from MyBDConnector import MyBDConnector
import json
import time
import struct

class Portao:
    def __init__(self,iIDComodo = -1):
        self.db = MyBDConnector()
        self.iIDPortao = -1
        self.bOpen = -1
        self.bSetOpen = 0;
        self.bPos = -1
        self.fAutoCloseTime = -1.0
        self.iIDDeviceStatus = -1
        self.iIDComodo = iIDComodo
        self.reSendJob = 0
        self.lastJob = ""
        
    def setiIDPortao(self,arg):
        self.iIDPortao = arg
    def setbOpen(self,arg):
        self.bOpen = arg
    def setbSetOpen(self,arg):
        self.bSetOpen = arg
    def setbPos(self,arg):
        self.bPos = arg
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
        
    def getiIDPortao(self):
        return self.iIDPortao
    def getbOpen(self):
        return self.bOpen
    def getbSetOpen(self):
        return self.bSetOpen
    def getbPos(self):
        return self.bPos
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
        #if(self.bPos != arg1.getbPos()):
            #aux = False
        return aux
     
    def updateData(self):
        sql = "update Portao set bOpen = %d, fAutoCloseTime = %f, bPos = %d, iIDDeviceStatus = %d where iIDPortao = %d;" % (self.getbOpen(),self.getfAutoCloseTime(),self.getbPos(),self.getiIDDeviceStatus(), self.getiIDPortao())
        self.db.post(sql)

    def checkPortao(self,arg = ""):
        sql = "Select iIDPortao,bOpen,bPos,fAutoCloseTime,iIDDeviceStatus,iIDComodo from Portao%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            for row in rows:
                self.setiIDPortao(row[0])
                print "\tPortao Encontrado"
                self.setbOpen(row[1])
                self.setbPos(row[2])
                self.setfAutoCloseTime(row[3])
                self.setiIDDeviceStatus(row[4])
                self.setiIDComodo(row[5])

    def checkPortaoResponse(self,mJson):
        if "D" in mJson:
            self.setiIDDeviceStatus(mJson['D'])
        if 'O' in mJson:
            self.setbOpen(mJson['O'])
        if 'P' in mJson:
            self.setbPos(mJson['P'])
        if 'F' in mJson:
            if((self.getfAutoCloseTime()*1000) != mJson['F']):
                self.setreSendJob(True) #deixa esse como verificacao, por hora.
        self.updateData()

    def checkUserPortaoLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserPortaoLog, bOpen,fAutoCloseTime from UserPortaoLog where bPending = 0 and iIDPortao = %d;" % (self.getiIDPortao())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Portao()
                iIDUserPortaoLog = row[0]
                print "Found new PortaoLog. bOpen = %i" % row[1]
                aux.setbOpen(row[1])
                aux.setfAutoCloseTime(row[2])
                if not self.compareData(aux):
                    myName = 'PO'
                    #//mFloat = struct.unpack("f", struct.pack("f", ))
                    encoded = json.dumps({cComodo:{myName:{'O':aux.getbOpen(),'F':int(aux.getfAutoCloseTime()*1000)}}}, separators=(',',':'))
                    self.setLastJob(encoded)
                    with sendLock:
                        sendQueue.push(encoded)
                    if(self.getbOpen() != aux.getbOpen()):
                        ## Setar deviceStatusAbrindo
                        if(aux.getbOpen() == 1):
                            self.setiIDDeviceStatus(1)
                        else:
                            self.setiIDDeviceStatus(2)
                    self.setbOpen(aux.getbOpen())
                    self.setfAutoCloseTime(aux.getfAutoCloseTime())
                #sqlUpdate = "Update Portao set iSetpoint = %d, bControle = %d, bManualOn = %d where iIDPortao = %d;" % (self.getiSetpoint(), self.getbControle(),self.getbManualOn(), self.getiIDPortao())
                #print "Atualizando Janela : %s" % sqlUpdate
                #self.db.post(sqlUpdate)
                self.updateData()
                sqlUpdate = "Update UserPortaoLog set bPending = 1 where iIDUserPortaoLog = %d;" % iIDUserPortaoLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                
    def controlePortao(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.checkUserPortaoLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    self.setreSendJob(False)
                else:
                    myName = 'PO'
                    #mFloat = struct.unpack("f", struct.pack("f", self.getfAutoCloseTime()))
                    encoded = json.dumps({cComodo:{myName:{'O':self.getbOpen(),'F':int(self.getfAutoCloseTime()*1000)}}}, separators=(',',':'), cls=DecimalEncoder)
                    self.setLastJob(encoded)
                    
        if(self.getiIDDeviceStatus() != 5): ## 5 = fechado
            #print "Teste %d" % self.getiIDDeviceStatus()
            
            if not getActuatorsEvent.isSet():
                pass#getActuatorsEvent.set() ## Começa a rotina de verificar.

    def openPortao(self,iIDUsuario):
        sql = "Insert into UserAccessLog (iIDUsuario) values(%d);" % (iIDUsuario)
        self.db.post(sql)
        sql = "Insert into UserPortaoLog (bOpen,fAutoCloseTime,iIDPortao,iIDUsuario) values (%d,%f,%d,%d);" % (1,self.getfAutoCloseTime(),self.getiIDPortao(),iIDUsuario)
        self.db.post(sql)
        
class DecimalEncoder(json.JSONEncoder):
    def _iterencode(self, o, markers=None):
        if isinstance(o, decimal.Decimal):
            # wanted a simple yield str(o) in the next line,
            # but that would mean a yield on the line with super(...),
            # which wouldn't work (see my comment below), so...
            return (str(o) for o in [o])
        return super(DecimalEncoder, self)._iterencode(o, markers)
