package com.xclusive.x_note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.xclusive.x_note.SQlite.DataBase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ImageView colorpicker;
    private TextView currentDateTime;
    private MaterialButton donebtn;
    private Bitmap bitmap;
    private String hexcolor = "#FFFFFFFF";
    @SuppressLint("StaticFieldLeak")
    public static EditText input, Title, subtitle;
    public static Dialog colordialog;
    private FloatingActionButton voicebtn;
    public static ConstraintLayout editlayout;
    public static String color = "#FFFFFFFF", id = "";
    public static DataBase db;
    public static int update;

    private String TITLE, DATE, SUB_TITLE, NOTE, COLOR;


    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
            }
        }

        input = findViewById(R.id.input);
        currentDateTime = findViewById(R.id.currentDateTime);
        Title = findViewById(R.id.Title1);
        subtitle = findViewById(R.id.subtitle);
        voicebtn = findViewById(R.id.voicebtn);
        db = new DataBase(this);
        editlayout = findViewById(R.id.editlayout);

        TITLE = getIntent().getStringExtra("TITLE");
        SUB_TITLE = getIntent().getStringExtra("SUBTITLE");
        NOTE = getIntent().getStringExtra("NOTE");
        DATE = getIntent().getStringExtra("DATE");
        COLOR = getIntent().getStringExtra("COLOR");
        update = getIntent().getIntExtra("TASK",0);
        id = getIntent().getStringExtra("ID");
        if(update == 1){
                Title.setText(TITLE);
                subtitle.setText(SUB_TITLE);
                input.setText(NOTE);
                currentDateTime.setText(DATE);
                editlayout.setBackgroundColor(Color.parseColor(COLOR));
        }else{
            Title.setText("");
            subtitle.setText("");
            input.setText("");
            editlayout.setBackgroundColor(getColor(R.color.white));
            currentDateTime.setText(getTransactionTime());
        }

        //color pallet dialog
        colordialog = new Dialog(this);
        colordialog.setContentView(R.layout.colorpallet_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(colordialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        colordialog.getWindow().setAttributes(lp);
        colordialog.setCanceledOnTouchOutside(true);
        colorpicker = colordialog.findViewById(R.id.colorpicker1);
        donebtn = colordialog.findViewById(R.id.colordonebtn);


//mic btn function for translating speech to text using google translator//
        voicebtn.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            try {
                startActivityForResult(intent, 1);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Your Device doesn't Support this Feature", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        });

        colorpicker.setDrawingCacheEnabled(true);
        colorpicker.buildDrawingCache(true);
        colorpicker.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                bitmap = colorpicker.getDrawingCache();
                int pix = bitmap.getPixel((int) event.getX(), (int) event.getY());
                hexcolor = "#" + Integer.toHexString(pix);
                donebtn.setBackgroundColor(Color.parseColor(hexcolor));
            }
            return true;
        });

        donebtn.setOnClickListener(V -> {
            if (!hexcolor.isEmpty()) {
                editlayout.setBackgroundColor(Color.parseColor(hexcolor));
                colordialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> info = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input.append(" " + info.get(0));
                }
            case 101:
                Bundle bundle = Objects.requireNonNull(data).getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                FirebaseVision firebaseVision = FirebaseVision.getInstance();
                FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
                Task<FirebaseVisionText> textTask = firebaseVisionTextRecognizer.processImage(image);
                textTask.addOnSuccessListener(firebaseVisionText -> {
                    if (textTask.isSuccessful()) {
                        input.append(firebaseVisionText.getText());
                    }
                });
                textTask.addOnFailureListener(e -> {
                });
                break;
        }
    }


    public void save(View view) {
        if (color.isEmpty()) {
            color = "#FFFFFFFF";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            savedata();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void savedata() {
        if (Title.getText().toString().isEmpty() | subtitle.getText().toString().isEmpty() | input.getText().toString().isEmpty()) {
            Toast.makeText(this, "Title and Subtitle are mandatory", Toast.LENGTH_SHORT).show();

        } else {
            if (update == 0) {
                db = new DataBase(this);
                long res = db.insertdata("0", Title.getText().toString(), subtitle.getText().toString(), "null", currentDateTime.getText().toString(), input.getText().toString(), hexcolor, 1);
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            } else {
                update = 0;
                db = new DataBase(this);
                long res = db.update(id, Title.getText().toString(), subtitle.getText().toString(), "null", currentDateTime.getText().toString(), input.getText().toString(), hexcolor);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            }
            finish();
            onBackPressed();
        }

    }

    public void bacbtn(View view) {
        finish();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void bold(View view) {
        Spannable bold = new SpannableStringBuilder(input.getText());
        bold.setSpan(new StyleSpan(Typeface.BOLD),
                input.getSelectionStart(),
                input.getSelectionEnd(),
                0);
        input.setText(bold);
    }

    public void color(View view) {
    }

    public void underline(View view) {
        Spannable bold = new SpannableStringBuilder(input.getText());
        bold.setSpan(new UnderlineSpan(),
                input.getSelectionStart(),
                input.getSelectionEnd(),
                0);
        input.setText(bold);
    }

    public void italic(View view) {
        Spannable bold = new SpannableStringBuilder(input.getText());
        bold.setSpan(new StyleSpan(Typeface.ITALIC),
                input.getSelectionStart(),
                input.getSelectionEnd(),
                0);
        input.setText(bold);
    }

    public void Normal(View view) {
        Spannable n = new SpannableStringBuilder(input.getText());
        n.setSpan(new StyleSpan(0),
                input.getSelectionStart(),
                input.getSelectionEnd(),
                0);
        input.setText(n);
    }

    public void selectcolor(View view) {
        colordialog.show();
    }

    public void opencamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getTransactionTime() {
        Calendar calendar = Calendar.getInstance();
        android.icu.text.SimpleDateFormat day = new android.icu.text.SimpleDateFormat("dd", Locale.getDefault());
        android.icu.text.SimpleDateFormat month = new android.icu.text.SimpleDateFormat("MMM", Locale.getDefault());
        android.icu.text.SimpleDateFormat time = new android.icu.text.SimpleDateFormat("hh:mm a", Locale.getDefault());
        android.icu.text.SimpleDateFormat year = new android.icu.text.SimpleDateFormat("yyyy", Locale.getDefault());
        return time.format(calendar.getTime()) + ", " + day.format(calendar.getTime()) + " " + month.format(calendar.getTime()) + " " + year.format(calendar.getTime());
    }

}