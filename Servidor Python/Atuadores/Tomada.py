# -*- coding: utf-8 -*-
from MyBDConnector import MyBDConnector
import json
import time
class Tomada:
    def __init__(self,iIDComodo = -1):
        self.db = MyBDConnector()
        self.iIDTomada = -1
        self.bStatus = -1
        self.iIDComodo = iIDComodo
        self.reSendJob = 0
        self.lastJob = ""

    def setiIDTomada(self,arg):
        self.iIDTomada = arg
    def setbStatus(self,arg):
        self.bStatus = arg
    def setiIDComodo(self,arg):
        self.iIDComodo = arg
    def setreSendJob(self,arg):
        self.reSendJob = arg
    def setLastJob(self,arg):
        self.lastJob = arg
        
    def getiIDTomada(self):
        return self.iIDTomada
    def getbStatus(self):
        return self.bStatus
    def getiIDComodo(self):
        return self.iIDComodo
    def getreSendJob(self):
        return self.reSendJob
    def getLastJob(self):
        return self.lastJob
    
    def compareData(self,arg1):
        aux = True
        if(self.bStatus != arg1.getbStatus()):
            aux = False
        return aux
     
    def updateData(self):
        sql = "update Tomadas set bStatus = %d where iIDTomada = %d;" % (self.getbStatus(),self.getiIDTomada())
        self.db.post(sql)
## Importante, o ideal seria entao o Controller do comodo buscar quantos itens tem e ele jogar os where para cada ID (na vdd ja jogaria o obj msm...)
    def checkTomada(self,arg = ""):
        sql = "Select iIDTomada,cNome,bStatus,iIDComodo from Tomadas%s;" % (arg)
        rows = self.db.get(sql)
        if rows is not None:
            for row in rows:
                self.setiIDTomada(row[0])
                print "\tTomada Encontrada: %s" %(row[1])
                self.setbStatus(row[2])
                self.setiIDComodo(row[3])

    def checkTomadaResponse(self,bStatus): ## Recebe o valor ja
        #self.setbStatus(mJson['bStatus'])
        if(self.getbStatus() != bStatus):
            self.setreSendJob(True)
        self.updateData()

    def checkUserTomadaLog(self,sendQueue,sendLock,cComodo):
        sql = "Select iIDUserTomadaLog, bStatus from UserTomadaLog where bPending = 0 and iIDTomada = %d;" % (self.getiIDTomada())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                aux = Tomada()
                iIDUserTomadaLog = row[0]
                aux.setiIDTomada = self.getiIDTomada()
                aux.setbStatus(row[1])
                print "Found new TomadaLog. bStatus = %i" % row[1]
                if not self.compareData(aux):
                    myName = 'T'
                    ## Aqui ira criar o array de tomadas do comodo (pq o arduino tem que receber todas, ja que nao sabe qm eh qm)
                    mArray = self.createJob(aux)
                    encoded = json.dumps({cComodo:{myName:mArray}}, separators=(',',':'))
                    self.setLastJob(encoded)
                    with sendLock:
                        sendQueue.push(encoded)
                        time.sleep(0.5)
                    self.setbStatus(aux.getbStatus())
                sqlUpdate = "Update Tomadas set bStatus = %d where iIDTomada = %d;" % (self.getbStatus(),self.getiIDTomada())
                #print "Atualizando Janela : %s" % sqlUpdate
                self.db.post(sqlUpdate)
                
                sqlUpdate = "Update UserTomadaLog set bPending = 1 where iIDUserTomadaLog = %d;" % iIDUserTomadaLog
                ##print "Atualizando bd : %s" % sqlUpdate
                self.db.post(sqlUpdate)

    def createJob(self,aux):
        job = []
        sql = "Select iIDTomada,bStatus from Tomadas where iIDComodo = %d;" %(self.getiIDComodo())
        result = self.db.get(sql)
        if result is not None:
            for row in result:
                if row[0] == self.getiIDTomada():
                    job.append(aux.getbStatus())
                else:
                    job.append(row[1])
        return job
    def controleTomada(self,sendQueue,sendLock,getActuatorsEvent,cComodo):
        self.checkUserTomadaLog(sendQueue,sendLock,cComodo)
        if(self.getreSendJob() == True):
            with sendLock:
                if(self.getLastJob() is not ""):
                    sendQueue.push(self.getLastJob())
                    time.sleep(0.1)
                    self.setreSendJob(False)
                else:
                    myName = 'T'
                    ## Aqui ira criar o array de tomadas do comodo (pq o arduino tem que receber todas, ja que nao sabe qm eh qm)
                    mArray = self.createJob(self)
                    encoded = json.dumps({cComodo:{myName:mArray}}, separators=(',',':'))
                    self.setLastJob(encoded)

        #if(self.getb != self.getiSetpoint()):
         #   if not getActuatorsEvent.isSet():
          #      getActuatorsEvent.set()
