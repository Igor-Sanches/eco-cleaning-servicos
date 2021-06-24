package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;

public class BloqueadoActivity extends AppCompatActivity {

    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloqueado);
        String msg = getIntent().getStringExtra("msg");
        TextView _msg = findViewById(R.id.msg);
        _msg.setText(String.format("%s %s", getString(R.string.block_msg), msg));
        Conexao.getFirebaseAuth().signOut();
        new NotificationDAO(this).apagar(0, true);
        Settings.delete(this);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - count > 2000){
            count= System.currentTimeMillis();
            MySnackbar.makeText(this, R.string.clique_para_sair).show();
            return;
        }
        finishAffinity();
    }

    public void onZapAction(View view) {
        HelperManager.onZapService(this, this, this);
    }

    public void onEmailAction(View view) {
        HelperManager.onCallService(this, this, this);
    }

    public void onCallAction(View view) {
        HelperManager.onEmailService(this, this, this);
    }
}