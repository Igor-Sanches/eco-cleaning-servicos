package app.birdsoft.ecocleaningservicos.dialogo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class DialogObservacao extends AlertDialog {
    public interface OnClickecPositive{
        void onClicked(String observacao);
    }
    private OnClickecPositive onClickecPositive;

    public void setOnClickecPositive(OnClickecPositive onClickecPositive) {
        this.onClickecPositive = onClickecPositive;
    }

    public DialogObservacao(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        TextView message = findViewById(R.id.message);
        assert message != null;
        Objects.requireNonNull((View) findViewById(R.id.confirmar)).setOnClickListener((v) -> {
            String msg = message.getText().toString().trim();
            if(!msg.equals("")){
                dismiss();
                onClickecPositive.onClicked(msg);
            }else MyToast.makeText(getContext(), R.string.digite_msg_obs, ModoColor._falha).show();
        });
        Objects.requireNonNull((View) findViewById(R.id.cancel)).setOnClickListener((v) -> dismiss());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editar_observacao);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
