package imwi.imiu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import static java.sql.Types.NULL;

public class CatFace extends AppCompatActivity
{
    private ImageView REyeBG;
    private ImageView LEyeBG;
    private ImageView REyePupil;
    private ImageView LEyePupil;
    private ImageView REyelid;
    private ImageView LEyelid;
    private ImageView Nose;
    private ImageView Mouth;
    private ImageView LBeard;
    private ImageView RBeard;
    private ImageView Forehead;

    private Context MainContext;

    private static final String TAG = "Cat Log";

    private int CurrentFace = NORMAL_FACE;

    public static final int NORMAL_FACE = 0;
    public static final int HAPPY_FACE = 1;
    public static final int SAD_FACE = 2;

    public static final int CLOSE_MOUTH = 0;
    public static final int OPEN_MOUTH = 1;

    public CatFace(Context context)
    {
        MainContext = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void InitFace(ImageView rEyeBG, ImageView lEyeBG, ImageView rEyePupil, ImageView lEyePupil, ImageView rEyelid, ImageView lEyelid, ImageView nose, ImageView mouth, ImageView lBeard, ImageView rBeard, ImageView foreHead)
    {
        REyeBG = rEyeBG;
        LEyeBG = lEyeBG;
        REyePupil = rEyePupil;
        LEyePupil = lEyePupil;
        REyelid = rEyelid;
        LEyelid = lEyelid;
        Nose = nose;
        Mouth = mouth;
        LBeard = lBeard;
        RBeard = rBeard;
        Forehead = foreHead;
    }

    public void SetFace(int faceId)
    {
        if (faceId == HAPPY_FACE)
        {
            REyeBG.setImageResource(R.drawable.h_r_eye);
            LEyeBG.setImageResource(R.drawable.h_l_eye);
            REyePupil.setImageResource(NULL);
            LEyePupil.setImageResource(NULL);
            Nose.setImageResource(R.drawable.h_nose);
            Mouth.setImageResource(R.drawable.h_mouth);
            RBeard.setImageResource(R.drawable.h_r_beard);
            LBeard.setImageResource(R.drawable.h_l_beard);
            Forehead.setImageResource(R.drawable.h_forehead);

            CurrentFace = HAPPY_FACE;
        }
        else if (faceId == SAD_FACE)
        {
            REyeBG.setImageResource(R.drawable.s_r_eye_background);
            LEyeBG.setImageResource(R.drawable.s_l_eye_background);
            REyePupil.setImageResource(R.drawable.s_r_eye_pupil);
            LEyePupil.setImageResource(R.drawable.s_l_eye_pupil);
            Nose.setImageResource(R.drawable.s_nose);
            Mouth.setImageResource(R.drawable.s_mouth);
            RBeard.setImageResource(R.drawable.s_r_beard);
            LBeard.setImageResource(R.drawable.s_l_beard);
            Forehead.setImageResource(R.drawable.s_forehead);

            CurrentFace = SAD_FACE;
        }
        else
        {
            REyeBG.setImageResource(R.drawable.n_r_eye_background);
            LEyeBG.setImageResource(R.drawable.n_l_eye_background);
            REyePupil.setImageResource(R.drawable.n_r_eye_pupil);
            LEyePupil.setImageResource(R.drawable.n_l_eye_pupil);
            Nose.setImageResource(R.drawable.n_nose);
            Mouth.setImageResource(R.drawable.n_mouth);
            RBeard.setImageResource(R.drawable.n_r_beard);
            LBeard.setImageResource(R.drawable.n_l_beard);
            Forehead.setImageResource(R.drawable.n_forehead);

            CurrentFace = NORMAL_FACE;
        }
    }

    public int GetFace()
    {
        return CurrentFace;
    }

    public void SetMouth(int state)
    {
        if (state == OPEN_MOUTH)
            Mouth.setImageResource(R.drawable.n_open_mouth);
        else
        {
            if (CurrentFace == HAPPY_FACE)
                Mouth.setImageResource(R.drawable.h_mouth);
            else if (CurrentFace == SAD_FACE)
                Mouth.setImageResource(R.drawable.s_mouth);
            else
                Mouth.setImageResource(R.drawable.n_mouth);
        }
    }

    public void Move(String facePart, float fromX, float toX, float fromY, float toY, int duration, int loop, boolean isReverse)
    {
        ImageView ivFacePart = getPart(facePart);

        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(duration);
        translateAnimation.setRepeatCount(loop);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(false);

        if (isReverse == true)
            translateAnimation.setRepeatMode(Animation.REVERSE);
        else
            translateAnimation.setRepeatMode(Animation.ABSOLUTE);

        doAnimation(ivFacePart, translateAnimation, MainContext.getString(R.string.animation_set));
    }

    public void Rotate(String facePart, float fromD, float toD, float pX, float pY, int duration, int loop, boolean isReverse)
    {
        ImageView ivFacePart = getPart(facePart);

        RotateAnimation rotateAnimation = new RotateAnimation(fromD, toD, pX, pY);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatCount(loop);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setFillBefore(false);

        if (isReverse == true)
            rotateAnimation.setRepeatMode(Animation.REVERSE);
        else
            rotateAnimation.setRepeatMode(Animation.ABSOLUTE);

        doAnimation(ivFacePart, rotateAnimation, MainContext.getString(R.string.animation_set));
    }

    public void MoveAndRotate(String facePart, float fromX, float toX, float fromY, float toY, float fromD, float toD, float pX, float pY, int duration, int loop, boolean isReverse)
    {
        ImageView ivFacePart = getPart(facePart);

        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(duration);
        translateAnimation.setRepeatCount(loop);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(false);

        RotateAnimation rotateAnimation = new RotateAnimation(fromD, toD, pX, pY);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatCount(loop);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setFillBefore(false);

        if (isReverse == true)
        {
            rotateAnimation.setRepeatMode(Animation.REVERSE);
            translateAnimation.setRepeatMode(Animation.REVERSE);
        }
        else
        {
            translateAnimation.setRepeatMode(Animation.ABSOLUTE);
            rotateAnimation.setRepeatMode(Animation.ABSOLUTE);
        }

        AnimationSet innerAnimationSet = new AnimationSet(true);
        innerAnimationSet.setInterpolator(new LinearInterpolator());

        innerAnimationSet.addAnimation(translateAnimation);
        innerAnimationSet.addAnimation(rotateAnimation);

        doAnimation(ivFacePart, innerAnimationSet, MainContext.getString(R.string.animation_set));
    }

    public void Scale(String facePart, float fromX, float toX, float fromY, float toY, float pivotX, float pivotY, int duration, int loop, boolean isReverse)
    {
        ImageView ivFacePart = getPart(facePart);

        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX,fromY, toY,pivotX, pivotY);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setRepeatCount(loop);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        if (isReverse == true)
            scaleAnimation.setRepeatMode(Animation.REVERSE);
        else
            scaleAnimation.setRepeatMode(Animation.ABSOLUTE);

        doAnimation(ivFacePart, scaleAnimation, MainContext.getString(R.string.animation_set));
    }

    ImageView getPart(String name)
    {
        if (name == MainContext.getString(R.string.right_eye_bg))
            return REyeBG;
        else if (name == MainContext.getString(R.string.left_eye_bg))
            return LEyeBG;
        else if (name == MainContext.getString(R.string.right_eye_pupil))
            return REyePupil;
        else if (name == MainContext.getString(R.string.left_eye_pupil))
            return LEyePupil;
        else if (name == MainContext.getString(R.string.right_eyelid))
            return REyelid;
        else if (name == MainContext.getString(R.string.left_eyelid))
            return LEyelid;
        else if (name == MainContext.getString(R.string.nose))
            return Nose;
        else if (name == MainContext.getString(R.string.mouth))
            return Mouth;
        else if (name == MainContext.getString(R.string.right_beard))
            return RBeard;
        else if (name == MainContext.getString(R.string.left_beard))
            return LBeard;

        return Forehead;
    }

    private void doAnimation(ImageView facePart, Animation animation, @Nullable final String animationType) {
        Animation oldAnimation = facePart.getAnimation();
        if (oldAnimation != null) {
            if (oldAnimation.hasStarted() || (!oldAnimation.hasEnded())) {
                oldAnimation.cancel();
                facePart.clearAnimation();
            }
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, animationType + " start;");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, animationType + " end;");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, animationType + " repeat;");
            }
        });
        facePart.startAnimation(animation);
    }
}
