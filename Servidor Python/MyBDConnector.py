import MySQLdb

class MyBDConnector:

    def __init__(self):
        pass
    def get(self,sql):
        db = MySQLdb.connect(user='root',passwd='biboca1234',
                                 host='localhost',db='Domotica')
        try:
            cursor = db.cursor()
            #print sql
            cursor.execute(sql)
            if cursor.rowcount > 0:
                return cursor.fetchall()
            else:
                #print "Row = 0"
                return None
        except:
            return None
        finally:
            cursor.close()
            db.close()

    def post(self,sql):
        db = MySQLdb.connect(user='root',passwd='biboca1234',
                                 host='localhost',db='Domotica')
        cursor = db.cursor()
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            print "postFailed :\n%s" %sql
        finally:
            cursor.close()
            db.close()
