package app.birdsoft.ecocleaningservicos.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import app.birdsoft.ecocleaningservicos.model.Notification;

public class NotificationDAO extends SQLiteOpenHelper{
    private static final String TABELA = "EcoCleaning",
            ID = "rowid",
            TITULO = "TITULO",
            MENSAGEM = "MENSAGEM",
            DATA_CONTRATO = "DATA_CONTRATO",
            UID = "UID",
    LIDA = "LITA", ENVIADA = "ENVIADA", UID_CLIENTE = "UID_CLIENTE", UID_PEDIDO = "UID_PEDIDO", TYPE = "TYPE", RESPONDER = "RESPONDER", CLIENTE  = "CLIENTE";

    private final String[] dados = new String[]{ID, TITULO, MENSAGEM, DATA_CONTRATO, UID, LIDA, ENVIADA, UID_CLIENTE, UID_PEDIDO, TYPE, RESPONDER, CLIENTE};
    private Context context;

    public Notification getNotification(Notification notification) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] isDados = dados;
        Notification mNotificationEntry;
        String[] isDadosDB = new String[]{notification.getUid()};
        try{
            Cursor cursor = sqLiteDatabase.query(TABELA, isDados, UID + " = ?", isDadosDB, null, null, null, null);
            if(cursor != null){
                cursor.moveToNext();
                mNotificationEntry = new Notification();
                mNotificationEntry.setId(cursor.getLong(0));
                mNotificationEntry.setTitulo(cursor.getString(1));
                mNotificationEntry.setMensagem(cursor.getString(2));
                mNotificationEntry.setData(cursor.getLong(3));
                mNotificationEntry.setUid(cursor.getString(4));
                mNotificationEntry.setLida(cursor.getString(5).equals("true"));
                mNotificationEntry.setRecebida(cursor.getString(6).endsWith("true"));
                mNotificationEntry.setUidCliente(cursor.getString(7));
                mNotificationEntry.setUidServico(cursor.getString(8));
                mNotificationEntry.setType(cursor.getString(9));
                mNotificationEntry.setResponder(cursor.getString(10).equals("true"));
                mNotificationEntry.setCliente(cursor.getString(11));
                cursor.close();
            }else
                mNotificationEntry = null;

            sqLiteDatabase.close();
        }catch (Exception x){
            mNotificationEntry = null;
        }
        return mNotificationEntry;
    }

    public NotificationDAO(Context mContext){
        super(mContext, "Notifications", null, 3);
        this.context = mContext;
    }

    public Notification getNotification(String uid){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] isDados = dados;
        Notification mNotificationEntry;
        String[] isDadosDB = new String[]{uid};
        try{
            Cursor cursor = sqLiteDatabase.query(TABELA, isDados, UID + " = ?", isDadosDB, null, null, null, null);
            if(cursor != null){
                cursor.moveToNext();
                mNotificationEntry = new Notification();
                mNotificationEntry.setId(cursor.getLong(0));
                mNotificationEntry.setTitulo(cursor.getString(1));
                mNotificationEntry.setMensagem(cursor.getString(2));
                mNotificationEntry.setData(cursor.getLong(3));
                mNotificationEntry.setUid(cursor.getString(4));
                mNotificationEntry.setLida(cursor.getString(5).equals("true"));
                mNotificationEntry.setRecebida(cursor.getString(6).endsWith("true"));
                mNotificationEntry.setUidCliente(cursor.getString(7));
                mNotificationEntry.setUidServico(cursor.getString(8));
                mNotificationEntry.setType(cursor.getString(9));
                mNotificationEntry.setResponder(cursor.getString(10).equals("true"));
                mNotificationEntry.setCliente(cursor.getString(11));
                cursor.close();
            }else
                mNotificationEntry = null;

            sqLiteDatabase.close();
        }catch (Exception x){
            mNotificationEntry = null;
        }
        return mNotificationEntry;
    }

    public List<Notification> lista(){
        SQLiteDatabase sqLiteDatabase;
        String ordem = "ASC";
        String por = DATA_CONTRATO;
        Cursor cursor;
        LinkedList lista = new LinkedList();
        switch (1){
            case 0:
                por = DATA_CONTRATO;
                ordem = "ASC";
                break;
            case 1:
                por = DATA_CONTRATO;
                ordem = "DESC";
                break;
        }
        if((cursor = (sqLiteDatabase = this.getWritableDatabase()).rawQuery("SELECT rowid,* FROM " + TABELA + " ORDER BY " + por + " " + ordem, null)).moveToFirst()){
            do{
                Notification mNotificationEntry = new Notification();
                mNotificationEntry = new Notification();
                mNotificationEntry.setId(cursor.getLong(0));
                mNotificationEntry.setTitulo(cursor.getString(1));
                mNotificationEntry.setMensagem(cursor.getString(2));
                mNotificationEntry.setData(cursor.getLong(3));
                mNotificationEntry.setUid(cursor.getString(4));
                mNotificationEntry.setLida(cursor.getString(5).equals("true"));
                mNotificationEntry.setRecebida(cursor.getString(6).endsWith("true"));
                mNotificationEntry.setUidCliente(cursor.getString(7));
                mNotificationEntry.setUidServico(cursor.getString(8));
                mNotificationEntry.setType(cursor.getString(9));
                mNotificationEntry.setResponder(cursor.getString(10).equals("true"));
                mNotificationEntry.setCliente(cursor.getString(11));
                lista.add(mNotificationEntry);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return lista;
    }

    public void adicionar(Notification notification){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(TITULO, notification.getTitulo());
        valores.put(MENSAGEM, notification.getMensagem());
        valores.put(DATA_CONTRATO, notification.getData());
        valores.put(UID, notification.getUid());
        valores.put(LIDA, notification.isLida() ? "true" : "false");
        valores.put(ENVIADA, notification.isRecebida() ? "true" : "false");
        valores.put(UID_CLIENTE, notification.getUidCliente());
        valores.put(UID_PEDIDO, notification.getUidServico());
        valores.put(TYPE, notification.getType());
        valores.put(RESPONDER, notification.isResponder() ? "true" : "false");
        valores.put(CLIENTE, notification.getCliente());
        sqLiteDatabase.insert(TABELA, null, valores);
        sqLiteDatabase.close();
    }

    public void apagar(long id, boolean deletaAll){
        SQLiteDatabase sql = this.getReadableDatabase();
        if(deletaAll){
            sql.delete(TABELA, null, null);
        }else{
            String[] ids = new String[]{String.valueOf(id)};
            sql.delete(TABELA, ID + " = ?", ids);
            sql.close();
        }
        // if(show)
        //   Toast.makeText(mContext, Resource.getString(R.string.nota_apagada, mContext), Toast.LENGTH_LONG).show();
    }

    public void atualizar(Notification notification){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(TITULO, notification.getTitulo());
        valores.put(MENSAGEM, notification.getMensagem());
        valores.put(DATA_CONTRATO, notification.getData());
        valores.put(UID, notification.getUid());
        valores.put(LIDA, notification.isLida() ? "true" : "false");
        valores.put(ENVIADA, notification.isRecebida() ? "true" : "false");
        valores.put(UID_CLIENTE, notification.getUidCliente());
        valores.put(UID_PEDIDO, notification.getUidServico());
        valores.put(TYPE, notification.getType());
        valores.put(RESPONDER, notification.isResponder() ? "true" : "false");
        valores.put(CLIENTE, notification.getCliente());
        String[] id = new String[]{String.valueOf(notification.getId())};
        sqLiteDatabase.update(TABELA, valores, ID + " = ?", id);
        sqLiteDatabase.close();
        //Toast.makeText(mContext, Resource.getString(R.string.nota_atualizada, mContext), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABELA + " (" + TITULO + " TEXT, " + MENSAGEM + " TEXT, " + DATA_CONTRATO + " NUMBER, " +UID + " TEXT, " + LIDA +  " TEXT, " + ENVIADA + " TEXT, " +UID_CLIENTE + " TEXT, " +UID_PEDIDO + " TEXT, " +TYPE + " TEXT, "  +RESPONDER + " TEXT, " + CLIENTE +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i == 2 && i1 == 3){
            sqLiteDatabase.execSQL("ALTER TABLE " + TABELA + " ADD COLUMN " + UID + " TEXT;");
        }
    }

}