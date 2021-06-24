package app.birdsoft.ecocleaningservicos.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.view.ContaActivity;
import app.birdsoft.ecocleaningservicos.view.ContratosActivity;
import app.birdsoft.ecocleaningservicos.view.FavoritosActivity;

public class Navigate {
    private long timer = 0;
    private Activity activity;
    private Context context;
    public Navigate(Activity activity){
        this.activity=activity;
    }

    public Navigate(Context context) {
        this.context = context;
    }

    public static Navigate activity(Activity activity){
        return new Navigate(activity);
    }

    public static Navigate context(Context context) {
        return new Navigate(context);
    }

    public void navigate(Class<?> classe){
        if(System.currentTimeMillis() - timer > 4000){
            timer = System.currentTimeMillis();
            activity.startActivity(new Intent(activity, classe));
        }
    }

    public void navigate(Intent intent) {
        if(System.currentTimeMillis() - timer > 3000){
            timer = System.currentTimeMillis();
            if(activity == null)
                context.startActivity(intent);
            else activity.startActivity(intent);
        }
    }

    public void navigateForResult(Intent intent, int i) {
        if(System.currentTimeMillis() - timer > 3000){
            timer = System.currentTimeMillis();
            activity.startActivityForResult(intent, i);
        }
    }

    public void navigateForResult(Class<?> classe, int i) {
        if(System.currentTimeMillis() - timer > 3000){
            timer = System.currentTimeMillis();
            activity.startActivityForResult(new Intent(activity, classe), i);
        }
    }

    public void navigate(int itemId) {
        if(itemId == R.id.contratos){
            navigate(ContratosActivity.class);
        }else if(itemId == R.id.conta){
            navigate(ContaActivity.class);
        }else if(itemId == R.id.favoritos){
            navigate(FavoritosActivity.class);
        }
    }
}
