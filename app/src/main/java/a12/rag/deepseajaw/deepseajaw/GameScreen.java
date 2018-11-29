package a12.rag.deepseajaw.deepseajaw;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameScreen implements GLSurfaceView.Renderer, SensorEventListener {

    private Sensor accelerometer = null;
    private SensorManager sensorManager = null;
    private Context context = null;
    private GLImage background = null;
    private GLImage lockjaw = null;
    private GLImage lockjaw_face = null;
    private GLImage heart = null;
    private GLImage txt_hungry = null;
    private GLImage bar_hungry = null;
    private GLImage coral = null;
    private GLImage bait = null;
    private ArrayList<GLImage> lst_hearts = null;
    private ArrayList<GLImage> lst_corais = null;
    private ArrayList<GLImage> lst_baits = null;
    private int maxWidth, maxHeight;
    private float speed_jaw, frame_jaw;
    private int qtd_hearts;
    private float per_hungry;
    private float sensor_x, sensor_y;
    private float sensor_speed;

    public GameScreen(Context context){

        this.context = context;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.frame_jaw = 1;
        this.speed_jaw = 0.18f;
        this.qtd_hearts = 3;
        this.per_hungry = 100;
        this.sensor_speed = 20;
        this.lst_hearts = new ArrayList<>();
        this.lst_corais = new ArrayList<>();
        this.lst_baits = new ArrayList<>();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        configuraTela(gl, width, height);
        this.maxHeight = height;
        this.maxWidth = width;

        this.background = new GLImage(gl, this.context);
        this.background.setImage(R.drawable.deepsea);
        this.background.setAlign(GLImage.ALIGN_CENTER);
        this.background.setAngle(180);
        int bg_w = (width);
        int bg_h = (height+(height/10));
        this.background.setSize(bg_w, bg_h);

        float prop = 7;
        float met = 2;

        this.lockjaw = new GLImage(gl, this.context);
        this.lockjaw.setImage(R.drawable.lockjaw_sprite_chart);
        this.lockjaw.setFrames(4,2,8);
        this.lockjaw.setPosition(width/2 - (width/4), height/2, 1);
        this.lockjaw.setAngle(180);
        this.lockjaw.setSize(width/prop/1.5f);

        this.lockjaw_face = new GLImage(gl, this.context);
        this.lockjaw_face.setImage(R.drawable.lockjawface);
        this.lockjaw_face.setFrames(4,2,8);
        this.lockjaw_face.setPosition((width/prop/2)+4, (height-(width/prop/2))-4, 1);
        this.lockjaw_face.setAngle(180);
        this.lockjaw_face.setSize(width/prop);

        this.txt_hungry = new GLImage(gl, this.context);
        this.txt_hungry.setImage(R.drawable.txt_hungry);
        this.txt_hungry.setFrames(4,2,8);
        this.txt_hungry.setPosition((((width/prop/2)+4)*2.5f)+8, (height-(width/prop/2))-4, 1);
        this.txt_hungry.setAngle(180);
        this.txt_hungry.setSize(width/prop/1.5f);

        this.bar_hungry = new GLImage(gl, this.context);
        this.bar_hungry.setImage(R.drawable.hungry_bar_red);
        this.bar_hungry.setFrames(4,2,8);
        this.bar_hungry.setPosition((((width/prop/2)+4)*2.5f)+8, (height-(width/prop/2))-4, 1);
        this.bar_hungry.setAngle(180);
        this.bar_hungry.setSize(width/prop/1.5f);

        for(int i = 1; i <= this.qtd_hearts; i++){

            float h = ((height-(width/prop/2))) - width/prop/2;
            h -=  (width/prop/4) * i;
            this.heart = new GLImage(gl, this.context);
            this.heart.setImage(R.drawable.coracao);
            this.heart.setFrames(4,2,8);
            this.heart.setPosition((width/prop/2)+4, h, 1);
            this.heart.setAngle(180);
            this.heart.setSize(width/prop/4);

            this.lst_hearts.add(this.heart);
        }

        addCoral(gl, width, prop);
        addBait(gl, width, prop, height);

    }

    private void addBait(GL10 gl, int width, float prop, int height) {
        this.bait = new GLImage(gl, this.context);
        this.bait.setImage(getBait());
        this.bait.setFrames(4,2,8);
        this.bait.setPosition(width, height-(width/prop/3), 1);
        this.bait.setAngle(180);
        this.bait.setSize(width/prop);
        this.lst_baits.add(this.bait);
    }

    private void addCoral(GL10 gl, int width, float prop) {
        this.coral = new GLImage(gl, this.context);
        this.coral.setImage(getCoral());
        this.coral.setFrames(4,2,8);
        this.coral.setPosition(width, width/prop/3, 1);
        this.coral.setAngle(180);
        this.coral.setSize(width/prop);
        this.lst_corais.add(this.coral);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        Random r = new Random();
        if(r.nextInt(150) == 1){
            if(r.nextInt(2) == 1){
                addBait(gl, maxWidth, 7, maxHeight);
            } else {
                addCoral(gl, maxWidth, 7);
            }
        }

        this.background.drawImage();

            for (int i = 0; i < this.lst_hearts.size(); i++) {
                this.lst_hearts.get(i).drawImage();
            }

            if (this.frame_jaw > 8) {
                this.frame_jaw = 1;
            }
            this.frame_jaw += this.speed_jaw;
            this.lockjaw.drawFrame(this.frame_jaw, GLImage.DIRECTION_CLOCKWISE);

        for(int i = 0; i < this.lst_baits.size();i++){

            this.lst_baits.get(i).setPosition(this.lst_baits.get(i).getTranslate_x() - speed_jaw * 10, this.lst_baits.get(i).getTranslate_y(), 1);
            this.lst_baits.get(i).drawImage();
            if(this.lst_baits.get(i).getTranslate_x() < 0){
                this.lst_baits.remove(i);
            }
        }

        for(int i = 0; i < this.lst_corais.size();i++){
            this.lst_corais.get(i).setPosition(this.lst_corais.get(i).getTranslate_x() - speed_jaw * 10, this.lst_corais.get(i).getTranslate_y(), 1);
            this.lst_corais.get(i).drawImage();
            if(this.lst_corais.get(i).getTranslate_x() < 0){
                this.lst_corais.remove(i);
            }
        }



        this.lockjaw_face.drawImage();
        this.txt_hungry.drawImage();
        if(per_hungry >= 0) {
            float hungry_low = 0.1f;
            this.per_hungry -= hungry_low;
            this.bar_hungry.setSize(this.per_hungry / 100 * (this.maxWidth / 7 / 1.5f), this.maxWidth / 7 / 1.5f);
            this.bar_hungry.setPosition(this.bar_hungry.getTranslate_x() - (hungry_low/1.8f), this.bar_hungry.getTranslate_y(), 1);
        }
        this.bar_hungry.drawImage();
    }

    private void configuraTela(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, width, 0, height, -1, 1);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnableClientState ( GL10.GL_VERTEX_ARRAY );

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float y = event.values[1] * this.sensor_speed;
            Log.i("ASDF", event.values[1]+"");
            if(this.lockjaw != null) {

                if(event.values[1] > 0.2f){
                    if(this.lockjaw.getTranslate_y() < this.maxHeight ) {
                        this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() + y, 1);
                    }
                } else if(event.values[1] < 0.16f){
                    y += (this.sensor_speed);
                    if(this.lockjaw.getTranslate_y() > maxWidth/7/2f ) {
                        this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() - y, 1);
                    }
                }


            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private int getCoral(){

        Random r = new Random();

        switch (r.nextInt(4)){
            case 0:
                return R.drawable.coralaranja;
            case 1:
                return R.drawable.coralazul;
            case 2:
                return R.drawable.coralrosa;

            case 3:
                return R.drawable.coralverde;

            default:
                return  R.drawable.coralverde;

        }

    }

    private int getBait() {

        Random r = new Random();

        switch (r.nextInt(5)){
            case 0:
                return R.drawable.bait1;

            case 1:
                return R.drawable.bait2;

            case 2:
                return R.drawable.bait3;

            case 3:
                return R.drawable.bait4;

            case 4:
                return  R.drawable.bait5;

                default:
                    return R.drawable.bait1;

        }

    }

}
