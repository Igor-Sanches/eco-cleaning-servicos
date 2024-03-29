package app.birdsoft.ecocleaningservicos.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorFragments;
import app.birdsoft.ecocleaningservicos.login.fragments.LoginFragment;
import app.birdsoft.ecocleaningservicos.login.fragments.RegistrarFragment;

public class LoginActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static LoginActivity instance;
    public static LoginActivity getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 1){
            onPage(0);
        }else{
            finish();
        }
    }

    public void onPage(int page){
        viewPager.setCurrentItem(page);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_login);
            instance = this;
            viewPager = findViewById(R.id.viewpager);
            viewPager.beginFakeDrag();
            AdaptadorFragments adaptador = new AdaptadorFragments(getSupportFragmentManager());
            adaptador.adicionar(new LoginFragment(), "");
            adaptador.adicionar(new RegistrarFragment(), "");
            viewPager.setAdapter(adaptador);
        }catch (Exception c){
            Toast.makeText(this, c.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}