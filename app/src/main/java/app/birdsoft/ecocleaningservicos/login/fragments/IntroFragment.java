package app.birdsoft.ecocleaningservicos.login.fragments;

import android.content.Context;

import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;

public class IntroFragment extends WelcomeFragment implements WelcomeActivity.WelcomeContent {
    public boolean shouldDisplay(Context context) {
        return false;
    }

}
