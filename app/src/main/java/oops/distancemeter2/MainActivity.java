package oops.distancemeter2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements SurfaceHolder.Callback, SensorEventListener {
    private double DISTANCE;
    private double HEIGHT;
    Sensor accSensor;
//    private AdView adView;
    Camera camera;
    SurfaceView cameraView;
    private Button cancelButton;
    private Button cancelButton2;
    private double captureDISTANCE;
    private ImageView centerimage;
    Dialog dialog;
    private Dialog dialog3;
    private TextView dialogTitle;
    private boolean distanceLock;
    private int distanceUnit;
    private int distanceUnitTemp;
    private TextView distanceprint;
    private TextView distancevalue;
    private ImageView dshot;
    private boolean errorCheck;
    private int errorType;
    private CheckBox ftCheckBox;
    private boolean heightLock;
    private float heightValueTemp;
    private LinearLayout heightback;
    private TextView heightprint;
    private TextView heightvalue;
    private ImageView hshot;
    private EditText inputDistanceEditText;
    private float lensHeightValue;
    private TextView lensheight;
    private float[] mAccelerometerValues;
    Camera.AutoFocusCallback mAutoFocus = new Camera.AutoFocusCallback() {
        /* class oops.distancemeter2.MainActivity.AnonymousClass1 */

        public void onAutoFocus(boolean success, Camera camera) {
        }
    };
    private CheckBox mCheckBox;
    private float[] mMagneticValues;
    private float mPitch;
    SensorManager mSensorManager;
    Sensor magnetSensor;
    private Button okButton;
    Camera.Parameters parameters;
//    private AdView quitAdView;
    private Button quitButton;
    private int screenHeight;
    private int screenWidth;
    SurfaceHolder surfaceHolder;
    private TextView unit;
    private CheckBox ydCheckBox;

    /* access modifiers changed from: package-private */
    public void comeOnAdmob() {
//        this.adView = (AdView) findViewById(R.id.adView);
//        this.adView.loadAd(new AdRequest.Builder().build());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.screenWidth = metrics.widthPixels;
        this.screenHeight = metrics.heightPixels;
        Window win = getWindow();
        win.setContentView(R.layout.main);
        win.addContentView((LinearLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mainover, (ViewGroup) null), new LinearLayout.LayoutParams(-1, -1));
        getWindow().addFlags(WindowManager.LayoutParams.ALPHA_CHANGED);
        this.cameraView = (SurfaceView) findViewById(R.id.CameraView);
        this.surfaceHolder = this.cameraView.getHolder();
        this.surfaceHolder.setType(3);
        this.surfaceHolder.addCallback(this);
        this.cameraView.setFocusable(true);
        this.cameraView.setFocusableInTouchMode(true);
        this.cameraView.setClickable(true);
        this.mSensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        this.accSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        ((ImageView) findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass2 */

            public void onClick(View v) {
                MainActivity.this.showSetting();
            }
        });
        ((ImageView) findViewById(R.id.focus)).setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass3 */

            public void onClick(View v) {
                MainActivity.this.camera.autoFocus(MainActivity.this.mAutoFocus);
            }
        });
        ((ImageView) findViewById(R.id.howto)).setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass4 */

            public void onClick(View v) {
                MainActivity.this.showHowto();
            }
        });
        this.dshot = (ImageView) findViewById(R.id.dshot);
        this.dshot.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass5 */

            public void onClick(View v) {
                if (MainActivity.this.distanceLock) {
                    MainActivity.this.distanceLock = false;
                    MainActivity.this.heightLock = false;
                    MainActivity.this.dshot.setImageResource(R.drawable.doff);
                    MainActivity.this.hshot.setVisibility(View.INVISIBLE);
                    MainActivity.this.heightback.setVisibility(View.INVISIBLE);
                    return;
                }
                MainActivity.this.captureDISTANCE = MainActivity.this.DISTANCE;
                MainActivity.this.distanceLock = true;
                MainActivity.this.heightLock = false;
                MainActivity.this.dshot.setImageResource(R.drawable.don);
                MainActivity.this.hshot.setVisibility(View.VISIBLE);
                MainActivity.this.hshot.setImageResource(R.drawable.hoff);
                MainActivity.this.heightback.setVisibility(View.VISIBLE);
            }
        });
        this.hshot = (ImageView) findViewById(R.id.hshot);
        this.hshot.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass6 */

            public void onClick(View v) {
                if (MainActivity.this.heightLock) {
                    MainActivity.this.heightLock = false;
                    MainActivity.this.hshot.setImageResource(R.drawable.hoff);
                    return;
                }
                MainActivity.this.heightLock = true;
                MainActivity.this.hshot.setImageResource(R.drawable.hon);
            }
        });
        this.hshot.setVisibility(View.INVISIBLE);
        this.centerimage = (ImageView) findViewById(R.id.centerimage);
        int temp1 = (int) (((double) this.screenHeight) * 0.25d);
        int distanceValueSize = (int) (((double) ((int) (((double) temp1) * 0.6666666666666666d))) * 0.4d);
        int distancePrintSize = (int) (((double) ((int) (((double) temp1) * 0.3333333333333333d))) * 0.5d);
        int lensheightSize = (int) (((double) this.screenHeight) * 0.125d * 0.3d);
        this.lensheight = (TextView) findViewById(R.id.lensheight);
        this.lensheight.setTextSize(0, (float) lensheightSize);
        this.distancevalue = (TextView) findViewById(R.id.distance);
        this.distancevalue.setTextSize(0, (float) distanceValueSize);
        this.distanceprint = (TextView) findViewById(R.id.distanceprint);
        this.distanceprint.setTextSize(0, (float) distancePrintSize);
        this.heightvalue = (TextView) findViewById(R.id.height);
        this.heightvalue.setTextSize(0, (float) distanceValueSize);
        this.heightprint = (TextView) findViewById(R.id.heightprint);
        this.heightprint.setTextSize(0, (float) distancePrintSize);
        this.heightback = (LinearLayout) findViewById(R.id.heightback);
        this.heightback.setBackgroundResource(R.drawable.back);
        this.heightback.setVisibility(View.INVISIBLE);
        SharedPreferences pref = getSharedPreferences("SaveSate", 0);
        this.distanceUnit = pref.getInt("distanceunit", 0);
        this.lensHeightValue = pref.getFloat("lensheightvalue", 1.8f);
        if (this.distanceUnit == 0) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "m");
        } else if (this.distanceUnit == 1) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "ft");
        } else if (this.distanceUnit == 2) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "yd");
        }
        comeOnAdmob();
        //this.dialog3 = new Dialog(this, 16973839);
        this.dialog3 = new Dialog(this);
        this.dialog3.requestWindowFeature(1025);
        this.dialog3.setCancelable(true);
        this.dialog3.setContentView(R.layout.quitdialog);
//        this.quitAdView = (AdView) this.dialog3.findViewById(R.id.adView3);
//        this.quitAdView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("B75ED071DDD4F95A7AD538878F12DAF6").build());
        this.cancelButton = (Button) this.dialog3.findViewById(R.id.cancelbutton);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass7 */

            public void onClick(View v) {
                MainActivity.this.dialog3.dismiss();
            }
        });
        this.quitButton = (Button) this.dialog3.findViewById(R.id.quitbutton);
        this.quitButton.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass8 */

            public void onClick(View v) {
//                if (MainActivity.this.quitAdView != null) {
//                    MainActivity.this.quitAdView.destroy();
//                }
                MainActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void showQuitAdDialog() {
        this.dialog3.show();
    }

    /* access modifiers changed from: package-private */
    public void changeGUIAndSaveSetting() {
        this.distanceLock = false;
        this.heightLock = false;
        this.dshot.setImageResource(R.drawable.doff);
        this.hshot.setVisibility(View.INVISIBLE);
        this.heightback.setVisibility(View.INVISIBLE);
        SharedPreferences.Editor edit = getSharedPreferences("SaveSate", 0).edit();
        edit.putInt("distanceunit", this.distanceUnit);
        edit.putFloat("lensheightvalue", this.lensHeightValue);
        edit.commit();
        if (this.distanceUnit == 0) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "m");
            this.unit.setText("(m)");
        } else if (this.distanceUnit == 1) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "ft");
            this.unit.setText("(ft)");
        } else if (this.distanceUnit == 2) {
            this.lensheight.setText(String.valueOf(getInputStringFormat(this.lensHeightValue)) + "yd");
            this.unit.setText("(yd)");
        }
    }

    /* access modifiers changed from: package-private */
    public void showSetting() {
        //this.dialog = new Dialog(this, 16973839);
        this.dialog = new Dialog(this);
        this.dialog.requestWindowFeature(1025);
        this.dialog.getWindow().addFlags(1024);
        this.dialog.setCancelable(true);
        this.dialog.setContentView(R.layout.dialog);
        int textSize = (int) (((double) ((int) (((double) this.screenHeight) * 0.16666666666666666d))) * 0.4d);
        this.dialogTitle = (TextView) this.dialog.findViewById(R.id.dialogtitle);
        this.dialogTitle.setTextSize(0, (float) textSize);
        this.unit = (TextView) this.dialog.findViewById(R.id.unit);
        this.mCheckBox = (CheckBox) this.dialog.findViewById(R.id.me);
        this.ftCheckBox = (CheckBox) this.dialog.findViewById(R.id.ft);
        this.ydCheckBox = (CheckBox) this.dialog.findViewById(R.id.yd);
        this.okButton = (Button) this.dialog.findViewById(R.id.ok);
        this.cancelButton2 = (Button) this.dialog.findViewById(R.id.cancel);
        this.inputDistanceEditText = (EditText) this.dialog.findViewById(R.id.inputdistanceedit);
        this.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass9 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.this.ftCheckBox.setChecked(false);
                    MainActivity.this.ydCheckBox.setChecked(false);
                    MainActivity.this.distanceUnitTemp = 0;
                    MainActivity.this.unit.setText("(m)");
                }
            }
        });
        this.ftCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass10 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.this.mCheckBox.setChecked(false);
                    MainActivity.this.ydCheckBox.setChecked(false);
                    MainActivity.this.distanceUnitTemp = 1;
                    MainActivity.this.unit.setText("(ft)");
                }
            }
        });
        this.ydCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass11 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.this.mCheckBox.setChecked(false);
                    MainActivity.this.ftCheckBox.setChecked(false);
                    MainActivity.this.distanceUnitTemp = 2;
                    MainActivity.this.unit.setText("(yd)");
                }
            }
        });
        this.okButton.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass12 */

            public void onClick(View v) {
                MainActivity.this.lensHeightValue = MainActivity.this.heightValueTemp;
                MainActivity.this.distanceUnit = MainActivity.this.distanceUnitTemp;
                MainActivity.this.changeGUIAndSaveSetting();
                MainActivity.this.dialog.dismiss();
            }
        });
        this.cancelButton2.setOnClickListener(new View.OnClickListener() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass13 */

            public void onClick(View v) {
                MainActivity.this.dialog.dismiss();
            }
        });
        this.inputDistanceEditText.addTextChangedListener(new TextWatcher() {
            /* class oops.distancemeter2.MainActivity.AnonymousClass14 */

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp.equals("")) {
                    return;
                }
                if (temp.charAt(0) == '.') {
                    Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.dotalert), Toast.LENGTH_LONG);
                    toast.setGravity(17, 0, 0);
                    toast.show();
                    MainActivity.this.inputDistanceEditText.setText("");
                    return;
                }
                MainActivity.this.heightValueTemp = Float.parseFloat(temp);
            }
        });
        this.distanceUnitTemp = this.distanceUnit;
        this.inputDistanceEditText.setText(getInputStringFormat(this.lensHeightValue));
        if (this.distanceUnit == 0) {
            this.mCheckBox.setChecked(true);
            this.unit.setText("(m)");
        } else if (this.distanceUnit == 1) {
            this.ftCheckBox.setChecked(true);
            this.unit.setText("(ft)");
        } else if (this.distanceUnit == 2) {
            this.ydCheckBox.setChecked(true);
            this.unit.setText("(yd)");
        }
        this.dialog.show();
    }

    /* access modifiers changed from: package-private */
    public String getInputStringFormat(float input) {
        return new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US)).format((double) input);
    }

    /* access modifiers changed from: package-private */
    public String getInputStringFormat2(double input) {
        return new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.US)).format(input);
    }

    /* access modifiers changed from: package-private */
    public void showHowto() {
        startActivity(new Intent(this, help.class));
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters2) {
        List<Camera.Size> sizeList = parameters2.getSupportedPreviewSizes();
        Camera.Size bestSize = sizeList.get(0);
        for (int i = 1; i < sizeList.size(); i++) {
            if (sizeList.get(i).height * sizeList.get(i).width > bestSize.width * bestSize.height) {
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (!this.errorCheck) {
            Camera.Size myBestSize = getBestPreviewSize(w, h, this.parameters);
            if (myBestSize != null) {
                int WIDTH = myBestSize.width;
                int HEIGHT2 = myBestSize.height;
                this.parameters.setPreviewSize(WIDTH, HEIGHT2);
                this.cameraView.setLayoutParams(new LinearLayout.LayoutParams(this.screenWidth, (int) (((double) this.screenWidth) * (((double) WIDTH) / ((double) HEIGHT2)))));
            }
            this.camera.setParameters(this.parameters);
            this.camera.startPreview();
        } else if (this.errorType == 1) {
            this.centerimage.setImageResource(R.drawable.openerror);
            this.distancevalue.setVisibility(View.INVISIBLE);
            this.distanceprint.setVisibility(View.INVISIBLE);
            this.dshot.setVisibility(View.INVISIBLE);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera = Camera.open();
        } catch (Exception e) {
            this.errorCheck = true;
            this.errorType = 1;
        }
        if (!this.errorCheck) {
            try {
                this.camera.setPreviewDisplay(holder);
                this.parameters = this.camera.getParameters();
                if (getResources().getConfiguration().orientation != 2) {
                    this.camera.setDisplayOrientation(90);
                }
            } catch (IOException e2) {
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (!this.errorCheck) {
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case 1:
                this.mAccelerometerValues = (float[]) event.values.clone();
                break;
            case 2:
                this.mMagneticValues = (float[]) event.values.clone();
                break;
        }
        if (!(this.mMagneticValues == null || this.mAccelerometerValues == null)) {
            float[] R = new float[16];
            SensorManager.getRotationMatrix(R, null, this.mAccelerometerValues, this.mMagneticValues);
            float[] orientation = new float[3];
            SensorManager.getOrientation(R, orientation);
            this.mPitch = orientation[1];
        }
        double ANGLE = 90.0d - Math.abs(Math.toDegrees((double) this.mPitch));
        if (!this.distanceLock) {
            this.DISTANCE = ((double) this.lensHeightValue) / Math.tan(Math.toRadians(ANGLE));
            if (this.distanceUnit == 0) {
                this.distancevalue.setText(String.valueOf(getInputStringFormat2(this.DISTANCE)) + "m");
            } else if (this.distanceUnit == 1) {
                this.distancevalue.setText(String.valueOf(getInputStringFormat2(this.DISTANCE)) + "ft");
            } else if (this.distanceUnit == 2) {
                this.distancevalue.setText(String.valueOf(getInputStringFormat2(this.DISTANCE)) + "yd");
            }
        }
        if (!this.heightLock) {
            this.HEIGHT = (Math.tan(Math.toRadians(ANGLE)) * this.captureDISTANCE) + ((double) this.lensHeightValue);
            if (this.distanceUnit == 0) {
                this.heightvalue.setText(String.valueOf(getInputStringFormat2(this.HEIGHT)) + "m");
            } else if (this.distanceUnit == 1) {
                this.heightvalue.setText(String.valueOf(getInputStringFormat2(this.HEIGHT)) + "ft");
            } else if (this.distanceUnit == 2) {
                this.heightvalue.setText(String.valueOf(getInputStringFormat2(this.HEIGHT)) + "yd");
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onStop() {
        super.onStop();
    }

    public void onStart() {
        super.onStart();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onDestroy() {
//        if (this.adView != null) {
//            this.adView.destroy();
//        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
//        if (this.adView != null) {
//            this.adView.resume();
//        }
        this.mSensorManager.registerListener(this, this.accSensor, 0);
        this.mSensorManager.registerListener(this, this.magnetSensor, 0);
    }

    public void onPause() {
//        if (this.adView != null) {
//            this.adView.pause();
//        }
        super.onPause();
        this.mSensorManager.unregisterListener(this, this.accSensor);
        this.mSensorManager.unregisterListener(this, this.magnetSensor);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.quittitle)).setMessage(getString(R.string.quitmessage)).setPositiveButton(getString(R.string.quitdialog), new DialogInterface.OnClickListener() {
                /* class oops.distancemeter2.MainActivity.AnonymousClass15 */

                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            }).setNegativeButton(getString(R.string.canceldialog), (DialogInterface.OnClickListener) null).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                try {
                    Intent marketLaunch = new Intent("android.intent.action.VIEW");
                    marketLaunch.setData(Uri.parse("market://details?id=oops.distancemeter2"));
                    startActivity(marketLaunch);
                    return true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nolink), Toast.LENGTH_LONG).show();
                    return true;
                }
            case 2:
                try {
                    Intent marketLaunch2 = new Intent("android.intent.action.VIEW");
                    marketLaunch2.setData(Uri.parse("market://search?q=pub:KHTSXR"));
                    startActivity(marketLaunch2);
                    return true;
                } catch (Exception e2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nolink), Toast.LENGTH_LONG).show();
                    return true;
                }
            default:
                return false;
        }
    }
}
