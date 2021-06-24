package app.birdsoft.ecocleaningservicos.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class ItemAnimation {
    public static final int BOTTOM_UP = 1;
    private static final long DURATION_IN_BOTTOM_UP = 150;
    private static final long DURATION_IN_FADE_ID = 500;
    private static final long DURATION_IN_LEFT_RIGHT = 150;
    private static final long DURATION_IN_RIGHT_LEFT = 150;
    public static final int FADE_IN = 2;
    public static final int LEFT_RIGHT = 3;
    public static final int NONE = 0;
    public static final int RIGHT_LEFT = 4;

    public static void animate(View view, int i, int i2) {
        switch (i2) {
            case 1:
                animateBottomUp(view, i);
                return;
            case 2:
                animateFadeIn(view, i);
                return;
            case 3:
                animateLeftRight(view, i);
                return;
            case 4:
                animateRightLeft(view, i);
                return;
            default:
                return;
        }
    }

    private static void animateBottomUp(View view, int i) {
        boolean z = i == -1;
        int i2 = i + 1;
        float f = 800.0f;
        view.setTranslationY(z ? 800.0f : 500.0f);
        view.setAlpha(0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        String str = "translationY";
        float[] fArr = new float[2];
        if (!z) {
            f = 500.0f;
        }
        fArr[0] = f;
        fArr[1] = 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, str, fArr);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f});
        ofFloat.setStartDelay(z ? 0 : ((long) i2) * 150);
        ofFloat.setDuration(((long) (z ? 3 : 1)) * 150);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.start();
    }

    private static void animateFadeIn(View view, int i) {
        long j;
        boolean z = i == -1;
        int i2 = i + 1;
        view.setAlpha(0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f, 0.5f, 1.0f});
        ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f}).start();
        if (z) {
            j = 250;
        } else {
            j = (((long) i2) * DURATION_IN_FADE_ID) / 3;
        }
        ofFloat.setStartDelay(j);
        ofFloat.setDuration(DURATION_IN_FADE_ID);
        animatorSet.play(ofFloat);
        animatorSet.start();
    }

    private static void animateLeftRight(View view, int i) {
        boolean z = i == -1;
        int i2 = i + 1;
        view.setTranslationX(-400.0f);
        view.setAlpha(0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationX", new float[]{-400.0f, 0.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f});
        ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f}).start();
        ofFloat.setStartDelay(z ? 150 : ((long) i2) * 150);
        ofFloat.setDuration(((long) (z ? 2 : 1)) * 150);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.start();
    }

    private static void animateRightLeft(View view, int i) {
        boolean z = i == -1;
        int i2 = i + 1;
        view.setTranslationX(view.getX() + 400.0f);
        view.setAlpha(0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationX", new float[]{view.getX() + 400.0f, 0.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f});
        ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f}).start();
        ofFloat.setStartDelay(z ? 150 : ((long) i2) * 150);
        ofFloat.setDuration(((long) (z ? 2 : 1)) * 150);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.start();
    }
}
