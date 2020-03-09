from MyBDConnector import MyBDConnector
from Atuadores.Teto import Teto
from Atuadores.Luminosidade import Luminosidade

import json
class Piscina:
    def __init__(self):
        self.db = MyBDConnector()
        self.iIDComodo = 3
        #self.iUserCont = 0
        self.mTeto = Teto()
        self.mLuz = Luminosidade()
        
    def getiIDComodo(self):
        return self.iIDComodo
    def setiIDComodo(self,iIDComodo):
        self.iIDComodo = iIDComodo
    def getTeto(self):
        return self.mTeto
    def setTeto(self,mTeto):
        self.mTeto = mTeto
    def setLuminosidade(self,mLuz):
        self.mLuz = mLuz
    def getLuminosidade(self):
        return self.mLuz
        
    def checkData(self):
        #sql = "Select iUserCont from Comodos where iIDComodo = %d;" % (self.iIDComodo)
        #rows = self.db.get(sql)
        #if rows is not None:
            
         #   for row in rows:
          #      self.setiUserCont(row[0])
           #     print "Lavanderia Encontrada: %d pessoas" % (self.getiUserCont)
        ## Inicializa o Teto
        print "Inicializando Piscina : "
        self.mTeto.checkTeto(" where iIDComodo = %d" % (self.getiIDComodo)())
        self.mLuz.checkLuminosidade(" where iIDComodo = %d" %(self.getiIDComodo()))
        
    def controlePiscina(self,sendQueue,sendLock,getActuatorsEvent):
        self.mTeto.controleTeto(sendQueue,sendLock,getActuatorsEvent,'P')
        self.mLuz.controleLuminosidade(sendQueue,sendLock,getActuatorsEvent,'P')

    def checkPiscinaResponse(self,mJson):
        if ("T" in mJson):
            self.mTeto.checkTetoResponse(mJson["T"])
        if ("Lu" in mJson):
            self.mLuz.checkLuminosidadeResponse(mJson["Lu"])
        if ("bChuva" in mJson):
            self.mTeto.checkTetoResponse(mJson)
