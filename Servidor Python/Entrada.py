from MyBDConnector import MyBDConnector
from Atuadores.Porta import Porta
from Atuadores.Portao import Portao

import json
class Entrada:
    def __init__(self):
        self.db = MyBDConnector()
        self.iIDComodo = 4
        #self.iUserCont = 0
        self.mPorta = Porta()
        self.mPortao = Portao()
        self.bMigue = 0
    def getiIDComodo(self):
        return self.iIDComodo
    def setiIDComodo(self,iIDComodo):
        self.iIDComodo = iIDComodo
    def getPorta(self):
        return self.mPorta
    def setPorta(self,arg):
        self.mPorta = arg
    def setPortao(self,arg):
        self.mPortao = arg
    def getPortao(self):
        return self.mPortao
    def getbMigue(self):
        return self.bMigue
    def setbMigue(self,arg):
        self.bMigue = arg
    def checkData(self):
        #sql = "Select iUserCont from Comodos where iIDComodo = %d;" % (self.iIDComodo)
        #rows = self.db.get(sql)
        #if rows is not None:
            
         #   for row in rows:
          #      self.setiUserCont(row[0])
           #     print "Lavanderia Encontrada: %d pessoas" % (self.getiUserCont)
        ## Inicializa o Teto
        print "Inicializando Entrada : "
        self.mPorta.checkPorta(" where iIDComodo = %d" % (self.getiIDComodo)())
        self.mPortao.checkPortao(" where iIDComodo = %d" %(self.getiIDComodo()))
        
    def controleEntrada(self,sendQueue,sendLock,getActuatorsEvent):
        self.mPorta.controlePorta(sendQueue,sendLock,getActuatorsEvent,'E')
        self.mPortao.controlePortao(sendQueue,sendLock,getActuatorsEvent,'E')

    def checkEntradaResponse(self,mJson):
        if ("PA" in mJson):
            self.mPorta.checkPortaResponse(mJson["PA"])
        if ("PO" in mJson):
            self.mPortao.checkPortaoResponse(mJson["PO"])
    def checkRFIDResponse(self,mJson):
        print mJson
        if(mJson['bNewEntry'] == 1):
            print "New RFID Entry : "
            sql = "Select bMigue from Casa;"
            rows = self.db.get(sql)
            if rows is not None:
                for row in rows:
                    self.setbMigue(row[0])
            if self.getbMigue() == 1:
                #if mJson['action'] == "PA":
                print "\t Action = Porta"
                sql = "Select iIDUsuario,cNome from Usuarios where cRFID = '%s';" % (mJson['cRFID'])
            #elif mJson['action'] == "PO":
            else:
                print "\t Action = Portao"
                sql = "Select iIDUsuario,cNome from Usuarios where cRFID2 = '%s';" % (mJson['cRFID'])
            rows = self.db.get(sql)
            iIDUsuario = -1
            if rows is not None:
                for row in rows:
                    iIDUsuario = row[0]
                    print "\t User - %s" % row[1]
                if mJson['action'] == "PA":
                    self.mPorta.openPorta(iIDUsuario)
                elif mJson['action'] == "PO":
                    self.mPortao.openPortao(iIDUsuario)
