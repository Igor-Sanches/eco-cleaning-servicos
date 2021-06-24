package app.birdsoft.ecocleaningservicos.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;

public class MyToast{

    private Context context;
    private ModoColor color;
    private String msg;

    public static MyToast makeText(Context context, String msg, ModoColor color){
        return new MyToast(context, msg, color);
    }

    private MyToast(Context context, String msg, ModoColor color){
        this.context = context;
        this.color = color;
        this.msg = msg;
    }

    public static MyToast makeText(Context context, @StringRes int msg, ModoColor color){
        return new MyToast(context, msg, color);
    }

    private MyToast(Context context, @StringRes int msg, ModoColor color){
        this.context = context;
        this.color = color;
        this.msg = context.getString(msg);
    }

    private MyToast(Context context, String msg){
        this.context = context;
        this.msg = msg;
    }

    public static MyToast makeText(Context context, String msg){
        return new MyToast(context, msg);
    }

    private MyToast(Context context, @StringRes int msg){
        this.context = context;
        this.msg = context.getString(msg);
    }

    public static MyToast makeText(Context context, @StringRes int msg){
        return new MyToast(context, msg);
    }

    public void show(){
        int colorTextRes = 0;
        int colorCardRes = 0;
        if(color != null){
            switch (color){
                case _falha:
                    colorTextRes = context.getResources().getColor(R.color.branco);
                    colorCardRes = context.getResources().getColor(R.color.vermelho);
                    break;
                case _success:
                    colorTextRes = context.getResources().getColor(R.color.branco);
                    colorCardRes = context.getResources().getColor(R.color.verde);
                    break;
                case _default:
                    colorTextRes = context.getResources().getColor(R.color.branco);
                    colorCardRes = context.getResources().getColor(R.color.colorPrimaryDark);
                    break;
            }
        }else{
            colorTextRes = context.getResources().getColor(R.color.branco);
            colorCardRes = context.getResources().getColor(R.color.colorPrimaryDark);
        }
        View root = View.inflate(context, R.layout.my_toast_layout, null);
        if(root != null){
            CardView card = root.findViewById(R.id.toastCard);
            TextView text = root.findViewById(R.id.toastTextColor);
            card.setCardBackgroundColor(colorCardRes);
            text.setTextColor(colorTextRes);
            text.setText(msg);
            Toast toast = new Toast(context);
            toast.setView(root);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
