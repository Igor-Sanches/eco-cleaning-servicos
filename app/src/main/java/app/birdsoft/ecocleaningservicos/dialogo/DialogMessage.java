package app.birdsoft.ecocleaningservicos.dialogo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.view.ContratosActivity;

public class DialogMessage extends AlertDialog {
    private String msg;
    private boolean isButtonPositive;
    private OnPossiveButtonClicked onPossiveButtonClicked;
    private OnNegativeButtonClicked onNegativeButtonClicked;
    private String nomeButtomPositive;
    private String titulo = null;

    public DialogMessage(Context context, @StringRes int msg) {
        super(context);
        this.msg=context.getString(msg);
        this.isButtonPositive=false;
        this.nomeButtomPositive="";
    }

    public DialogMessage(Context context, @StringRes int msg, boolean isButtonPositive, @StringRes int nomeButtomPositive) {
        super(context);
        this.msg=context.getString(msg);
        this.isButtonPositive=isButtonPositive;
        this.nomeButtomPositive=context.getString(nomeButtomPositive);
    }

    public DialogMessage(Context context, @StringRes int msg, boolean isConfirmeBtn) {
        super(context);
        this.msg=context.getString(msg);
        this.isButtonPositive=isConfirmeBtn;
        this.nomeButtomPositive= context.getString(R.string.confirmar);
    }

    public interface OnPossiveButtonClicked{
        void onClicked();
    }

    public interface OnNegativeButtonClicked{
        void onClicked();
    }

    public void setOnNegativeButtonClicked(OnNegativeButtonClicked onNegativeButtonClicked) {
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }

    public void setOnPossiveButtonClicked(OnPossiveButtonClicked onPossiveButtonClicked) {
        this.onPossiveButtonClicked = onPossiveButtonClicked;
    }

    public DialogMessage(@NonNull Context context, String msg, boolean isButtonPositive, String nomeButtomPositive) {
        super(context);
        this.msg=msg;
        this.isButtonPositive=isButtonPositive;
        this.nomeButtomPositive=nomeButtomPositive;
    }

    public DialogMessage(@NonNull Activity context, String msg, boolean isButtonPositive, String nomeButtomPositive, String titulo) {
        super(context);
        this.msg=msg;
        this.titulo = titulo;
        this.isButtonPositive=isButtonPositive;
        this.nomeButtomPositive=nomeButtomPositive;
    }

    @Override
    public void show() {
        if(!isShowing()) {
            super.show();
            if(titulo != null){
                ((TextView) Objects.requireNonNull((View) findViewById(R.id.titulo))).setText(titulo);
            }
            Button positive = findViewById(R.id.confirmar);
            Objects.requireNonNull((View) findViewById(R.id.btn_positivo)).setVisibility(isButtonPositive ? View.VISIBLE : View.GONE);
            assert positive != null;
            positive.setText(nomeButtomPositive);
            ((TextView) Objects.requireNonNull((View) findViewById(R.id.messageDialog))).setText(msg);
            Objects.requireNonNull((View) findViewById(R.id.cancel)).setOnClickListener((v -> {
                if(onNegativeButtonClicked != null)
                    onNegativeButtonClicked.onClicked();

                dismiss();
            }));
            positive.setOnClickListener((v -> {
                dismiss();
                onPossiveButtonClicked.onClicked();
            }));

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_message);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
