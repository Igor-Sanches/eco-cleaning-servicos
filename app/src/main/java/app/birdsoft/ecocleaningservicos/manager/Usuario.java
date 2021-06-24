package app.birdsoft.ecocleaningservicos.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class Usuario {
    public static String PREFERENCE_NAME = "AppEcoCleaning";
    public static final int LATITUDE = 0, LONGITUDE = 1;
    private static final String PREFERENCE_FILE_NAME = "Birdsoft_db";
    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getmSharedPreferencesEditor(Context context){
        if(mSharedPreferences ==null){
            mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        }
        return mSharedPreferences;
    }

    public static String getUid(Context context){
        return getmSharedPreferencesEditor(context).getString("Uid", "");
    }

    public static void setUid(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("Uid", value);
        editor.apply();
    }

    public static String getEndereco(Context context){
        return getmSharedPreferencesEditor(context).getString("Endereco", null);
    }

    public static void setEndereco(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("Endereco", value);
        editor.apply();
    }

    public static void remove(String key, Context context){
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getLocalizacao(int type, Context context){
        if(type == LATITUDE){
            return getmSharedPreferencesEditor(context).getString("latitude", null);
        }else{
            return getmSharedPreferencesEditor(context).getString("longitude", null);
        }
    }

    public static void atulizarDadosLocalizacao(double latitude, double longitude, Context context) {
        SharedPreferences.Editor _editor = getmSharedPreferencesEditor(context).edit();
        _editor.putString("latitude", String.valueOf(latitude));
        _editor.apply();
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("longitude", String.valueOf(longitude));
        editor.apply();
    }

}
