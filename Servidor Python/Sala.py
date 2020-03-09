from MyBDConnector import MyBDConnector
from Atuadores.Janela import Janela
from Atuadores.Luminosidade import Luminosidade
from Atuadores.Tomada import Tomada
import json
class Sala:
    def __init__(self):
        self.db = MyBDConnector()
        self.iIDComodo = 1
        self.iUserCont = 0
        self.mJanela = Janela()
        self.mLuz = Luminosidade()
        self.mTomada = []
        
    def getiIDComodo(self):
        return self.iIDComodo
    def setiIDComodo(self,iIDComodo):
        self.iIDComodo = iIDComodo
    def setiUserCont(self,arg):
        self.iUserCont = arg
    def getiUserCont(self):
        return self.iUserCont
    def getJanela(self):
        return self.mJanela
    def setJanela(self,mJanela):
        self.mJanela = mJanela
    def setLuminosidade(self,mLuz):
        self.mLuz = mLuz
    def getLuminosidade(self):
        return self.mLuz
    def getTomada(self,index):
        return self.mTomada[index]
    def setTomada(self,arg):
        self.mTomada.append(arg)
        
    def checkData(self):
        sql = "Select iUserCont from Comodos where iIDComodo = %d;" % (self.iIDComodo)
        rows = self.db.get(sql)
        if rows is not None:
            
            for row in rows:
                self.setiUserCont(row[0])
                print "Sala Encontrada: %d pessoas" % (self.getiUserCont())
        ## Inicializa o Janela
        print "Inicializando Sala : "
        self.mJanela.checkJanela(" where Janela.iIDComodo = %d" % (self.getiIDComodo()))
        self.mLuz.checkLuminosidade(" where iIDComodo = %d" %(self.getiIDComodo()))
        ## Checando as tomadas e inserindo na lista
        sql = "Select iIDTomada from Tomadas where iIDComodo = %d;" % (self.getiIDComodo())
        rows = self.db.get(sql)
        if rows is not None:
            for row in rows:
                x = Tomada()
                x.checkTomada(" where iIDTomada = %d" % (row[0]))
                self.setTomada(x)
                
    def controleSala(self,sendQueue,sendLock,getActuatorsEvent):
        self.mJanela.controleJanela(sendQueue,sendLock,getActuatorsEvent,'S')
        self.mLuz.controleLuminosidade(sendQueue,sendLock,getActuatorsEvent,'S')
        for mObj in self.mTomada:
            mObj.controleTomada(sendQueue,sendLock,getActuatorsEvent,'S')

    def checkSalaResponse(self,mJson):
        if ("J" in mJson):
            self.mJanela.checkJanelaResponse(mJson["J"])
        if ("Lu" in mJson):
            self.mLuz.checkLuminosidadeResponse(mJson["Lu"])
        if ("T" in mJson):
            print "dados Tomadas = %s" %mJson["T"]
            i = 0
            for mObj in self.mTomada:
                print "Enviando dado para Tomada%d = %d" % (i,mJson["T"][i])
                mObj.checkTomadaResponse(mJson["T"][i])
                i = i+1
        if ("iUserCont" in mJson):
            #print "iUserCont = %d" % mJson['iUserCont']
            self.setiUserCont(mJson["iUserCont"])
            sql = "Update Comodos set iUserCont = %d where iIDComodo = %d" %(self.getiUserCont(),self.getiIDComodo())
            self.db.post(sql)
        if ("bChuva" in mJson):
            print "Status Chuva : %d" % mJson['bChuva']
            self.mJanela.checkJanelaResponse(mJson)
