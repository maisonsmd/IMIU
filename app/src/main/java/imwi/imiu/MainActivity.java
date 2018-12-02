package imwi.imiu;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements IMIUTouchHandler.OnActionRecorededHandler, UsbSerial.OnUsbReconnectHandler {

    private static final String TAG = "View Animation";
    private View mPuppet;
    private CatFace MiuMiuFace;

    private UsbSerial serial;

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
    private ImageView ForceHead;

    private boolean CommercialMode = false;

    CountDownTimer EyeTimer;
    CountDownTimer BeardTimer;
    CountDownTimer EyelidTimer;
    CountDownTimer OpenMouseTimer;

    private boolean isMouthOpened = false;
    private IMIUTouchHandler touchHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigProgram();
        InitVariables();
        InitFace();


        serial = new UsbSerial(this);
        serial.Init(this);
        serial.Open();

        WakeScreenUp();
    }

    private void ConfigProgram() {
        //getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    void EyeMotion() {
        if (MiuMiuFace.GetFace() == CatFace.HAPPY_FACE) {
            MiuMiuFace.MoveAndRotate(getString(R.string.right_eye_bg), 0, 2, 0, -8, -3, 3, 50, 50, 1000, 1, true);
            MiuMiuFace.MoveAndRotate(getString(R.string.left_eye_bg), 0, 2, 0, -8, -3, 3, 50, 50, 1000, 1, true);
        } else if (MiuMiuFace.GetFace() == CatFace.NORMAL_FACE) {
            MiuMiuFace.Move(getString(R.string.right_eye_pupil), 0, 2, 0, 4, 1000, 1, true);
            MiuMiuFace.Move(getString(R.string.left_eye_pupil), 0, -2, 0, 4, 1000, 1, true);
        } else if (MiuMiuFace.GetFace() == CatFace.SAD_FACE) {
            MiuMiuFace.Move(getString(R.string.right_eye_pupil), 0, 2, 0, 4, 1000, 1, true);
            MiuMiuFace.Move(getString(R.string.left_eye_pupil), 0, -2, 0, 4, 1000, 1, true);
        }
    }

    void ForeheadMotion() {
        MiuMiuFace.Move(getString(R.string.forehead), 0, 2, 0, 0, 1000, 1, true);
    }

    void BeardMotion() {
        MiuMiuFace.Rotate(getString(R.string.right_beard), 0, 4, 150, 0, 2000, 1, true);
        MiuMiuFace.Rotate(getString(R.string.left_beard), 0, -4, 0, 0, 2000, 1, true);
    }

    void MouthMotion() {
        MiuMiuFace.Scale(getString(R.string.mouth), 1.0f, 1.1f, 1.0f, 1.1f, Mouth.getWidth() / 2, 0, 2000, 1, true);
        MiuMiuFace.Scale(getString(R.string.forehead), 1.0f, 1.0f, 1.0f, 1.05f, Mouth.getWidth() / 2, 0, 2000, 1, true);

    }

    void EyelidMotion() {
        if (MiuMiuFace.GetFace() == CatFace.HAPPY_FACE) {
            LEyelid.setVisibility(View.INVISIBLE);
            REyelid.setVisibility(View.INVISIBLE);
            return;
        }

        LEyelid.setVisibility(View.VISIBLE);
        REyelid.setVisibility(View.VISIBLE);

        MiuMiuFace.Move(getString(R.string.right_eyelid), 0, 0, 0, 175, 30, 1, true);
        MiuMiuFace.Move(getString(R.string.left_eyelid), 0, 0, 0, 175, 30, 1, true);
    }

    void OpenMouth(boolean isTurnOn) {
        if (isTurnOn == false) {
            OpenMouseTimer.cancel();
            return;
        }

        OpenMouseTimer = new CountDownTimer(10000, 100) {
            public void onFinish() {
                MiuMiuFace.SetMouth(CatFace.CLOSE_MOUTH);
                this.start();
            }

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 1000)
                    isMouthOpened = false;
                if (millisUntilFinished < 1000 && !isMouthOpened) {
                    isMouthOpened = true;
                    MiuMiuFace.SetMouth(CatFace.OPEN_MOUTH);
                }
            }
        }.start();

    }

    private void InitFace() {
        EyeTimer = new CountDownTimer(2000, 1000) {
            public void onFinish() {
                EyeMotion();
                ForeheadMotion();

                this.start();
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();

        BeardTimer = new CountDownTimer(8000, 1000) {
            public void onFinish() {
                BeardMotion();
                MouthMotion();

                this.start();
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();

        EyelidTimer = new CountDownTimer(6000, 1000) {
            public void onFinish() {
                EyelidMotion();
                this.start();
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();

        OpenMouth(true);
    }

    private void InitVariables() {
        MiuMiuFace = new CatFace(this);

        REyeBG = findViewById(R.id.iv_R_Eye_Background);
        LEyeBG = findViewById(R.id.iv_L_Eye_Background);
        REyePupil = findViewById(R.id.iv_R_Eye_Pupil);
        LEyePupil = findViewById(R.id.iv_L_Eye_Pupil);
        REyelid = findViewById(R.id.iv_R_Eyelid);
        LEyelid = findViewById(R.id.iv_L_Eyelid);

        Nose = findViewById(R.id.iv_Nose);
        Mouth = findViewById(R.id.iv_Mouth);
        LBeard = findViewById(R.id.iv_L_Beard);
        RBeard = findViewById(R.id.iv_R_Beard);
        ForceHead = findViewById(R.id.iv_Forehead);

        MiuMiuFace.InitFace(REyeBG, LEyeBG, REyePupil, LEyePupil, REyelid, LEyelid, Nose, Mouth, LBeard, RBeard, ForceHead);


        touchHandler = new IMIUTouchHandler(this);
    }

    int currentFace = CatFace.NORMAL_FACE;

    public void ChangeFace(View v) {
        currentFace++;
        if (currentFace > 3)
            currentFace = CatFace.NORMAL_FACE;

        MiuMiuFace.SetFace(currentFace);
    }

    public void MoveFace(View v) {
        MiuMiuFace.Move(getString(R.string.right_eye_pupil), 0, 2, 0, 4, 500, 100, true);
        MiuMiuFace.Move(getString(R.string.left_eye_pupil), 0, -2, 0, 4, 500, 100, true);
    }

    public void Blink(View v) {
        MiuMiuFace.Move(getString(R.string.right_eyelid), 0, 0, 0, 175, 1000, 1, true);
        MiuMiuFace.Move(getString(R.string.left_eyelid), 0, 0, 0, 175, 1000, 1, true);
    }

    public void Rotate(View view) {
        MiuMiuFace.Rotate(getString(R.string.right_beard), 0, 6, 150, 0, 1000, 100, true);
    }

    public void MoveAndRotate(View v) {
        MiuMiuFace.MoveAndRotate(getString(R.string.mouth), 0, 40, 0, 15, 0, 10, 50, 0, 1000, 100, true);
    }

    void MakeRealMotion(double velocity) {
        double motionSpeed = velocity;

        if (velocity > 10000) {
            motionSpeed = 10000;
        }
        motionSpeed = (int) ((10000 - motionSpeed) / 70);
        if (motionSpeed < 10) {
            motionSpeed = 10;
        }
        serial.Write("<action>3</action><speed>" + motionSpeed + "</speed>");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        touchHandler.Handle(ev);
        return super.dispatchTouchEvent(ev);
    }

    long TouchTimeSpace = 10000;
    long CurrentTime = 20000;
    long TimeToSad = 10000;
    double WipeSpeed = 0;
    boolean IsHappyFeeling = false;

    double XPos;
    double YPos;

    double RXDir;
    double RYDir;
    double LXDir;
    double LYDir;
    double Radial = 8;


    boolean isWatingAds = false;
    boolean isWatingBoring = false;

    @Override
    public void OnActionRecorded(Actions action, Position startPosition, double velocity) {
        Log.i("touch", "" + startPosition.X + "," + startPosition.Y);


        TouchTimeSpace = System.currentTimeMillis() - CurrentTime;
        CurrentTime = System.currentTimeMillis();

        XPos = touchHandler.getCurrentPosition().X;
        YPos = touchHandler.getCurrentPosition().Y;

       /* int[] location = new int[2];
        REyePupil.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        XPos = XPos - x;
        YPos = YPos - y;

        RXDir = (Radial * XPos) / (Math.sqrt(((XPos * XPos + YPos * YPos))));
        RYDir = (Radial * YPos) / (Math.sqrt(((XPos * XPos + YPos * YPos))));
        RXDir = Radial **/

        Log.i("xy:", "" + RXDir + "-" + RYDir);


        switch (action) {

            case TOUCH_DOWN:
                isWatingBoring = false;
                if (TouchTimeSpace > TimeToSad) {
                    MakeUnhappyFace();
                    IsHappyFeeling = false;
                }
                break;
            case TOUCH:
                break;
            case MOVE:
                Log.i("touch", "MOVE " + velocity);
                break;
            case SWIPE:
                isWatingAds = true;

                Log.i("touch", "SWIPE " + velocity);
                WipeSpeed = velocity;

                /*if(startPosition.Y > 800){
                    OpenAds();
                }*/

                if (startPosition.Y > 1000) {
                    CommercialMode = !CommercialMode;
                    if (CommercialMode) {
                        Toast.makeText(this.getApplicationContext(), "chế độ Thương mại!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this.getApplicationContext(), "chế độ Thú cưng", Toast.LENGTH_SHORT).show();
                }

                final CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (!isWatingAds) {
                            this.cancel();
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (isWatingAds) {
                            {
                                if (CommercialMode)
                                    OpenAds();
                            }
                            this.cancel();
                        }
                    }
                }.start();

                MakeRealMotion(velocity);
                MakeHappyFace();
                break;
            case TOUCH_UP:
                isWatingAds = false;
                isWatingBoring = true;

                CountDownTimer countDownTimer1 = new CountDownTimer(10000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (!isWatingBoring)
                            this.cancel();
                    }

                    @Override
                    public void onFinish() {
                        MakeUnhappyFace();
                        Toast.makeText(getApplicationContext(), "Chơi với tui đi!", Toast.LENGTH_SHORT).show();
                        this.cancel();
                    }
                }.start();

                Log.i("touch", "TOUCH_UP ");

                XPos = 0;
                YPos = 0;

                TouchTimeSpace = System.currentTimeMillis();
                CurrentTime = System.currentTimeMillis();
                MakeNormalFace();
                break;
        }
    }

    private void MakeUnhappyFace() {
        MiuMiuFace.SetFace(CatFace.SAD_FACE);

        //MiuMiuFace.Move(getString(R.string.right_eye_bg), 0, (int)RXDir, 0, (int)RYDir,1, 0, false);
    }

    private void MakeNormalFace() {
        MiuMiuFace.SetFace(CatFace.NORMAL_FACE);
    }

    private void MakeHappyFace() {
        MiuMiuFace.SetFace(CatFace.HAPPY_FACE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serial.Open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serial.Close();
    }

    void WakeScreenUp() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    public void OnUsbReconnect() {
        Log.i(TAG, "USB reconected!");
        WakeScreenUp();
    }

    void OpenAds() {
        Intent adsIntent = new Intent(this, AdsActivity.class);
        startActivity(adsIntent);
    }
}
