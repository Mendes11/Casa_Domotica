from MyBDConnector import MyBDConnector
from Atuadores.Teto import Teto
import json
class Lavanderia:
    def __init__(self):
        self.db = MyBDConnector()
        self.iIDComodo = 2
        self.iUserCont = 0
        self.mTeto = Teto()

    def getiIDComodo(self):
        return self.iIDComodo
    def setiIDComodo(self,iIDComodo):
        self.iIDComodo = iIDComodo
    def getiUserCont(self):
        return self.iUserCont
    def setiUserCont(self,iUserCont):
        self.iUserCont = iUserCont
    def getTeto(self):
        return self.mTeto
    def setTeto(self,mTeto):
        self.mTeto = mTeto

        
    def checkData(self):
        sql = "Select iUserCont from Comodos where iIDComodo = %d;" % (self.iIDComodo)
        rows = self.db.get(sql)
        if rows is not None:
            for row in rows:
                self.setiUserCont(row[0])
                print "Lavanderia Encontrada: %d pessoas" % (self.getiUserCont())
        ## Inicializa o Teto
        self.mTeto.checkTeto(" where iIDComodo = %d" % (self.getiIDComodo()))
    def controleLavanderia(self,sendQueue,sendLock,getActuatorsEvent):
        self.mTeto.controleTeto(sendQueue,sendLock,getActuatorsEvent,'L')

    def checkLavanderiaResponse(self,lavanderiaJson):
        if ("T" in lavanderiaJson):
            self.mTeto.checkTetoResponse(lavanderiaJson["T"]);
        if ("iUserCont" in lavanderiaJson):
            self.setiUserCont(lavanderiaJson["iUserCont"])
            sql = "Update Comodos set iUserCont = %d where iIDComodo = %d" %(self.getiUserCont(),self.getiIDComodo())
            self.db.post(sql)
        if ("bChuva" in lavanderiaJson):
            self.mTeto.checkTetoResponse(lavanderiaJson)
        
