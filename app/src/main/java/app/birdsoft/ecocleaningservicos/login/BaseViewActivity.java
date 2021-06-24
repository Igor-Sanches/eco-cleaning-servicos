package app.birdsoft.ecocleaningservicos.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.birdsoft.ecocleaningservicos.App;
import app.birdsoft.ecocleaningservicos.MainActivity;
import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.UpdateAllViewModel;

public abstract class BaseViewActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (WelcomeActivity.shouldDisplay(this)) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }else{
            new UpdateAllViewModel(this).execute(this);
            App.isBloqueado(this);
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
