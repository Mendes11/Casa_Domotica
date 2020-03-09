# -*- coding: utf-8 -*-
import json
import time
import smbus
import array

class ArduinoCom:
    bus = smbus.SMBus(1)
    address = 0x00
    jsonAmount = 2
    jsonAmountID = "AMOUNT"
    receiveAmount = 0
    receiveCount = 0

    def __init__(self,mAddress):
        self.bus = smbus.SMBus(1)
        if not mAddress:
            raise Exception('Voce precisa definir um endereco a ser enviado')
        else:
            self.address = mAddress
        

    def writeString(self,char):
        global jsonAmount,jsonAmountID,receiveAmount,receiveCount
        print "Entrou WriteString"
        print char
        ascVal = bytearray(char)
        size = len(ascVal)
        
        ##print "size = ",size
        if size < 32:
            self.jsonAmount = 2;
            jsonMessage = json.dumps({self.jsonAmountID:self.jsonAmount}, separators=(',',':'))
            aux = bytearray(jsonMessage)
            self.bus.write_i2c_block_data(self.address,0x1d,list(aux))
            ###print 'enviado amount'
            time.sleep(0.5)
            self.bus.write_i2c_block_data(self.address,0x1d,list(ascVal))
            ###print 'enviado os dados'
            return 1
        elif (size >= 32 & size < 700):
            i = (int) ((size/31))
            ###print i
            self.jsonAmount = 2+i;
            jsonMessage = json.dumps({self.jsonAmountID:self.jsonAmount}, separators=(',',':'))
            aux = bytearray(jsonMessage)
            self.bus.write_i2c_block_data(self.address,0,list(aux))
            print "sent"
            for j in range(0,i+1):
                ##print "J = ",j," of ",i
                auxList = list(ascVal[(0+31*j):(31*(j+1))])
                ###print ascVal[(0+31*j):(31*(j+1))]
            
                self.bus.write_i2c_block_data(self.address,0,auxList)
                time.sleep(0.1)
            return 1
        else:
            raise Exception("Tamanho maior que 300. Reestruturar o codigo para caber.")
        return -1

## ------- ReadI2c ----------- ##
    def readi2c(self):
        global address,bus
        try:
            mList = self.bus.read_i2c_block_data(self.address,0x1d)
            if 255 in mList:
                mList = mList[0:mList.index(255)]
            return mList
        except:
            return -1
    
    def readJson(self):
        global bus,jsonAmount,jsonAmountID,receiveAmount,receiveCount
        self.receiveCount = 0
        self.receiveAmount = 0
        mList = self.readi2c()
        try:
            if mList != -1:
                mByte = bytearray(mList)
                mString = mByte.decode('utf-8')
                mJson = json.loads(mString)
                if 'AMOUNT' not in mJson:
                    ##raise Exception("não há amount")
                    print "Falha no Amount tentando novamente"
                    return self.readJson()
                else:
                    self.receiveAmount = mJson['AMOUNT']
                    #print 'receiveAmount = ',self.receiveAmount
                    self.receiveCount = self.receiveCount+1
                    mList = list();
                    try:
                        while self.receiveCount != self.receiveAmount:
                            mList += self.readi2c()
                            self.receiveCount += 1
                        self.receiveCount = 0
                        self.receiveAmount = 0
                        mByte = bytearray(mList)
                        mString = mByte.decode('utf-8')
                        mJson = json.loads(mString)
                        
                        return mJson
                    except Exception, e:
                        #print str(e)
                        return None
            else:
                ##raise Exception("Erro ao receber do i2c.")
                return None
        except:
            return None
