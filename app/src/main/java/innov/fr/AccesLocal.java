package innov.fr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class AccesLocal {

    private String nomBase = "historique.sqLite";
    private Integer versionBase =1;
    private MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;

    public AccesLocal(Context contexte){
        accesBD = new MySQLiteOpenHelper(contexte, nomBase, null, versionBase);
    }

    public void ajout(Mesure mesure){
        bd = accesBD.getWritableDatabase();
        String req = "insert into mesure (datemesure, user) values";
        req+= "("+mesure.getDatemesure()+","+mesure.getUser()+")";
        bd.execSQL(req);
    }

    public Mesure recupDernier(){
        bd=accesBD.getReadableDatabase();
        Mesure mesure =null;
        String req= "select * from mesure";
        Cursor cursor = bd.rawQuery(req,null);
        cursor.moveToLast();
        if(!cursor.isAfterLast()){
            Date date = new Date();
            String user = cursor.getString(1);
            mesure = new Mesure(date,user);
        }
        cursor.close();
        return mesure;
    }
}
