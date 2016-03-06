package pushl.net.recognizefinter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Thumb, Index, Second Joint
    public final String[] wFinger = {"Thumb", "Index", "SecondJoint"};
    public ArrayList<FingerEvent> events = new ArrayList<>();

    public final int THRESHOLD = 1000;
    int currentEventNumber = 0;
    int currentFinger      = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setFingerText(wFinger[0]);
    }

    public void setFingerText(String s){
        ((TextView)findViewById(R.id.fingerMessage)).setText(s);
    }

    public void setCurrent(String s){
        ((TextView)findViewById(R.id.current)).setText(s);
    }

    public void setProgress(float i){
        ((ProgressBar)findViewById(R.id.progressBar)).setProgress((int) (i * 100));
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int type       = event.getAction();
        float pressure = event.getPressure();
        float size     = event.getSize();
        float x        = event.getX();
        float y        = event.getY();
        float orient   = event.getOrientation();
        float major    = event.getTouchMajor();
        float minor    = event.getTouchMinor();

        if(currentFinger < wFinger.length) {
            FingerEvent e = new FingerEvent(wFinger[currentFinger], type, pressure, size, x, y, orient, major, minor);
            events.add(e);

            if(type == MotionEvent.ACTION_DOWN) {
                currentEventNumber++;
            }
            if (type == MotionEvent.ACTION_UP && currentEventNumber > THRESHOLD) {
                currentEventNumber = 0;
                currentFinger++;
                if(currentFinger < wFinger.length){
                    setFingerText(wFinger[currentFinger]);
                }else{
                    shareCSV();
                }
                return true;
            }
            setProgress(1.0f * currentEventNumber / THRESHOLD);
            setCurrent(e.toString());
        }else{
            setProgress(1.0f);
            setCurrent("No more finger");
        }
        return true;
    }

    public File saveToCSV(String filename){
        File root = getApplicationContext().getExternalFilesDir(null);
       // File root   = Environment.getExternalStorageDirectory();
        if(!root.canWrite()) return null;
        File dir = new File(root.getAbsolutePath() + "/FingerData");
        dir.mkdirs();
        File file = new File(dir, filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write((FingerEvent.genCSVHeader() + "\n").getBytes());
            for(FingerEvent e : events){
                out.write((e.toCSVColumn() + "\n").getBytes());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    public void shareCSV(){
        File f = saveToCSV("fing.csv");
        Uri  u =   Uri.fromFile(f);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Finger Datas");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u);
        sendIntent.setType("text/csv");
        startActivity(sendIntent);
    }
}
