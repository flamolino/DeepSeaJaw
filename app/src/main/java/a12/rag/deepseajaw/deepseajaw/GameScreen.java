package a12.rag.deepseajaw.deepseajaw;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameScreen implements GLSurfaceView.Renderer {

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
    private GLImage fish = null;
    private GLImage enemy = null;
    private GLImage heart_anim = null;
    private ArrayList<GLImage> lst_hearts = null;
    private ArrayList<GLImage> lst_corais = null;
    private ArrayList<GLImage> lst_baits = null;
    private ArrayList<GLImage> lst_feed_fish = null;
    private ArrayList<GLImage> lst_feed_enemy = null;
    private ArrayList<GLImage> lst_feed_heart = null;
    private int maxWidth, maxHeight;
    private float speed_jaw, frame_jaw, speed_inc;
    private int qtd_hearts;
    private float per_hungry;
    private float speed_up;
    private int sprite_action;
    private static int SWIN = 1;
    private static int BITE = 2;
    private int points;
    private float speed_up_inc;

    public GameScreen(Context context){

        this.context = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.frame_jaw = 1;
        this.speed_jaw = 0.18f;
        this.speed_inc = 0.000015f;
        this.qtd_hearts = 3;
        this.per_hungry = 100;
        this.speed_up = 1f;
        this.speed_up_inc = 0.1f;
        this.lst_hearts = new ArrayList<>();
        this.lst_corais = new ArrayList<>();
        this.lst_baits = new ArrayList<>();
        this.lst_feed_enemy = new ArrayList<>();
        this.lst_feed_fish = new ArrayList<>();
        this.lst_feed_heart = new ArrayList<>();
        this.sprite_action = BITE;
        this.points = 0;

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
        this.lockjaw.setFrames(4,3,12);
        this.lockjaw.setPosition(width/2 - (width/4), height/2, 1);
        this.lockjaw.setAngle(180);
        this.lockjaw.setSize(width/prop/1f);

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
        addFish(gl, width, 15, height);

    }

    private void addBait(GL10 gl, int width, float prop, int height) {
        this.bait = new GLImage(gl, this.context);
        this.bait.setImage(getBait());
        this.bait.setPosition(width, height-(width/prop/3), 1);
        this.bait.setAngle(180);
        this.bait.setSize(width/prop);
        this.lst_baits.add(this.bait);
    }

    private void addCoral(GL10 gl, int width, float prop) {
        this.coral = new GLImage(gl, this.context);
        this.coral.setImage(getCoral());
        this.coral.setPosition(width, width/prop/3, 1);
        this.coral.setAngle(180);
        this.coral.setSize(width/prop);
        this.lst_corais.add(this.coral);
    }

    private void addHeartTop(GL10 gl, int width, float prop, int height) {
        this.heart = new GLImage(gl, this.context);
        this.heart.setImage(R.drawable.heart_sprite_chart);
        this.heart.setFrames(6,2,12);
        this.heart.setPosition(width, (height-(width/prop/3)) -(width/prop), 1);
        this.heart.setAngle(180);
        this.heart.setSize(width/prop);
        this.lst_feed_heart.add(this.heart);
    }

    private void addHeartBot(GL10 gl, int width, float prop, int height) {
        this.heart = new GLImage(gl, this.context);
        this.heart.setImage(R.drawable.heart_sprite_chart);
        this.heart.setFrames(6,2,12);
        this.heart.setPosition(width, width/prop/3 + (width/prop), 1);
        this.heart.setAngle(180);
        this.heart.setSize(width/prop);
        this.lst_feed_heart.add(this.heart);
    }

    private void addFish(GL10 gl, int width, float prop, int height){
        this.fish = new GLImage(gl, this.context);
        this.fish.setImage(getFish());
        this.fish.setFrames(4,2,8);

        Random r = new Random();
        int dir = 1;
        if(r.nextBoolean()){
            dir = -1;
        }

        r = new Random();
        float h = ((height/2) / 100) * (r.nextInt(78)+1);

        if(dir == 1){
            h = (height/2) + h;
        } else{
            h = (height/2) - h;
        }

        this.fish.setPosition(width, h, 1);
        this.fish.setAngle(180);
        this.fish.setSize(width/prop);
        this.lst_feed_fish.add(this.fish);
    }

    private void addEnemy(GL10 gl, int width, float prop, int height){
        this.enemy = new GLImage(gl, this.context);
        this.enemy.setImage(getEnemy());
        this.enemy.setFrames(4,2,8);

        Random r = new Random();
        int dir = 1;
        if(r.nextBoolean()){
            dir = -1;
        }

        r = new Random();
        float h = ((height/2) / 100) * (r.nextInt(78)+1);

        if(dir == 1){
            h = (height/2) + h;
        } else{
            h = (height/2) - h;
        }

        this.enemy.setPosition(width, h, 1);
        this.enemy.setAngle(180);
        this.enemy.setSize(width/prop);
        this.lst_feed_enemy.add(this.enemy);
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        this.speed_jaw+=this.speed_inc;

        Random r = new Random();
        if(r.nextInt(100) == 1){
            int rand = r.nextInt(100)+1;
            if(rand > 0 && rand <= 15){
                addBait(gl, maxWidth, 7, maxHeight);
            } else if( rand > 15 && rand <= 30) {
                addCoral(gl, maxWidth, 7);
            } else if( rand > 30 && rand <= 45) {
                addEnemy(gl, maxWidth, 13, maxHeight);
            } else if( rand > 45 && rand <= 90) {
                addFish(gl, maxWidth, 15, maxHeight);
            } else if( rand > 90 && rand <= 100) {
                if(r.nextBoolean()){
                    addHeartBot(gl, maxWidth, 24, maxHeight);
                } else {
                    addHeartTop(gl, maxWidth, 24, maxHeight);
                }
            }
        }

        this.background.drawImage();



        for(int i = 0; i < this.lst_feed_enemy.size(); i++){

            if(this.lst_feed_enemy.size() == 0 || i == this.lst_feed_enemy.size() ){
                break;
            }

            this.lst_feed_enemy.get(i).setPosition(this.lst_feed_enemy.get(i).getTranslate_x() - speed_jaw * 10, this.lst_feed_enemy.get(i).getTranslate_y(), 1);
            if (this.lst_feed_enemy.get(i).getFrame_pos() > 8) {
                this.lst_feed_enemy.get(i).setFrame_pos(1);
            }
            this.lst_feed_enemy.get(i).setFrame_pos(this.lst_feed_enemy.get(i).getFrame_pos()+(this.speed_jaw/1.5f));
            this.lst_feed_enemy.get(i).drawFrame(this.lst_feed_enemy.get(i).getFrame_pos(), GLImage.DIRECTION_CLOCKWISE);
            if(this.lst_feed_enemy.get(i).getTranslate_x() < 0){
                this.lst_feed_enemy.remove(i);
                break;
            }

            if(this.lockjaw.getTranslate_x()+(this.lockjaw.getScale_x()/1.5f) >= this.lst_feed_enemy.get(i).getTranslate_x()
                    && this.lockjaw.getTranslate_x()+(this.lockjaw.getScale_x()/1.5f) < this.lst_feed_enemy.get(i).getTranslate_x()+this.lst_feed_enemy.get(i).getScale_x()/2){


                if(this.lockjaw.getTranslate_y() >= this.lst_feed_enemy.get(i).getTranslate_y()- (this.lst_feed_enemy.get(i).getScale_y()) &&
                        this.lockjaw.getTranslate_y() < this.lst_feed_enemy.get(i).getTranslate_y()+ (this.lst_feed_enemy.get(i).getScale_y()/2.5f)
                        ){
                    this.sprite_action = BITE;
                    this.lst_feed_enemy.remove(i);
                    this.qtd_hearts --;

                    if(qtd_hearts == 0) {

                    } else {
                        this.lst_hearts.remove(this.qtd_hearts);
                    }
                    break;
                }


            }

        }

        for(int i = 0; i < this.lst_feed_fish.size(); i++){

            if(this.lst_feed_fish.size() == 0 || i == this.lst_feed_fish.size()){
                break;
            }

                this.lst_feed_fish.get(i).setPosition(this.lst_feed_fish.get(i).getTranslate_x() - speed_jaw * 10, this.lst_feed_fish.get(i).getTranslate_y(), 1);
                if (this.lst_feed_fish.get(i).getFrame_pos() > 8) {
                    this.lst_feed_fish.get(i).setFrame_pos(1);
                }
                this.lst_feed_fish.get(i).setFrame_pos(this.lst_feed_fish.get(i).getFrame_pos() + (this.speed_jaw/1.5f));
                this.lst_feed_fish.get(i).drawFrame(this.lst_feed_fish.get(i).getFrame_pos(), GLImage.DIRECTION_CLOCKWISE);
                if (this.lst_feed_fish.get(i).getTranslate_x() < 0) {
                    this.lst_feed_fish.remove(i);
                    this.points++;
                    if(this.per_hungry < 75) {
                        this.per_hungry += 25;
                    } else {
                        this.per_hungry = 100;
                    }
                    break;
                }


            if(this.lockjaw.getTranslate_x()+(this.lockjaw.getScale_x()/1.5f) >= this.lst_feed_fish.get(i).getTranslate_x()
                    && this.lockjaw.getTranslate_x()+(this.lockjaw.getScale_x()/1.5f) < this.lst_feed_fish.get(i).getTranslate_x()+this.lst_feed_fish.get(i).getScale_x()/2){


                if(this.lockjaw.getTranslate_y() >= this.lst_feed_fish.get(i).getTranslate_y()- (this.lst_feed_fish.get(i).getScale_y()) &&
                        this.lockjaw.getTranslate_y() < this.lst_feed_fish.get(i).getTranslate_y()+ (this.lst_feed_fish.get(i).getScale_y()/2.5f)
                        ){
                    this.sprite_action = BITE;
                    this.lst_feed_fish.remove(i);
                    break;
                }


            }

        }

        for(int i = 0; i < this.lst_feed_heart.size(); i++){

            if(this.lst_feed_heart.size() == 0 || i == this.lst_feed_heart.size()){
                break;
            }

                this.lst_feed_heart.get(i).setPosition(this.lst_feed_heart.get(i).getTranslate_x() - speed_jaw * 10, this.lst_feed_heart.get(i).getTranslate_y(), 1);
                if (this.lst_feed_heart.get(i).getFrame_pos() > 10) {
                    this.lst_feed_heart.get(i).setFrame_pos(1);
                }
                this.lst_feed_heart.get(i).setFrame_pos(this.lst_feed_heart.get(i).getFrame_pos() + (this.speed_jaw/1.5f));
                this.lst_feed_heart.get(i).drawFrame(this.lst_feed_heart.get(i).getFrame_pos(), GLImage.DIRECTION_CLOCKWISE);
                if (this.lst_feed_heart.get(i).getTranslate_x() < 0) {
                    this.lst_feed_heart.remove(i);
                    if(this.qtd_hearts<3) {
                        this.qtd_hearts++;
                        this.lst_hearts.add(this.heart);
                    }
                    break;
                }

        }

        if(this.sprite_action == SWIN) {
            if (this.frame_jaw > 8) {
                this.frame_jaw = 1;
            }
            this.frame_jaw += (this.speed_jaw/1.5f);
            this.lockjaw.drawFrame(this.frame_jaw, GLImage.DIRECTION_CLOCKWISE);
        } else if(this.sprite_action == BITE) {
            if (this.frame_jaw > 12) {
                this.frame_jaw = 1;
                this.sprite_action = SWIN;
            }
            this.frame_jaw += (this.speed_jaw/1.5f);
            this.lockjaw.drawFrame(this.frame_jaw, GLImage.DIRECTION_CLOCKWISE);
        }

        for(int i = 0; i < this.lst_baits.size();i++){

            if(this.lst_baits.size() == 0 || i == this.lst_baits.size()){
                break;
            }

            this.lst_baits.get(i).setPosition(this.lst_baits.get(i).getTranslate_x() - speed_jaw * 10, this.lst_baits.get(i).getTranslate_y(), 1);
            this.lst_baits.get(i).drawImage();
            if(this.lst_baits.get(i).getTranslate_x() < 0){
                this.lst_baits.remove(i);
                break;
            }
        }

        for(int i = 0; i < this.lst_corais.size();i++){

            if(this.lst_corais.size() == 0 || i == this.lst_corais.size()){
                break;
            }

            this.lst_corais.get(i).setPosition(this.lst_corais.get(i).getTranslate_x() - speed_jaw * 10, this.lst_corais.get(i).getTranslate_y(), 1);
            this.lst_corais.get(i).drawImage();
            if(this.lst_corais.get(i).getTranslate_x() < 0){
                this.lst_corais.remove(i);
                break;
            }
        }


        for (int i = 0; i < this.lst_hearts.size(); i++) {
            this.lst_hearts.get(i).drawImage();
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

    private int getFish() {

        Random r = new Random();
        int rand = r.nextInt(100) + 1;

        if(rand > 0 && rand <= 40){
            return R.drawable.green_enemie_sprite_chart;
        } else if(rand > 40 && rand <= 70){
            return R.drawable.yellow_enemie_sprite_chart;
        } else if(rand > 70 && rand <= 90){
            return R.drawable.blue_big_enemie_sprite_chart;
        } else {
            return R.drawable.red_big_enemie_sprite_chart;
        }

    }

    private int getEnemy() {

        Random r = new Random();
        int rand = r.nextInt(100) + 1;

        if(rand > 0 && rand <= 75){
            return R.drawable.green_thorn_enemie_sprite_chart;
        } else {
            return R.drawable.poison_thorn_enemie_sprite_chart;
        }

    }


    public void touch(MotionEvent event) {

        float y = maxHeight - event.getY();

        int action = event.getAction();
        Log.i("ASDF", "Clicou: "+y+" | "+maxHeight);

        if(action == MotionEvent.ACTION_DOWN) {

            Log.i("ASDF", "DOWN");
            this.speed_up += this.speed_up_inc;
            if (this.lockjaw != null) {
                if (y < maxWidth / 2) {
                    this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() - this.speed_up, 1);
                } else {
                    this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() + this.speed_up, 1);
                }
            }
        }

        if(action == MotionEvent.ACTION_MOVE) {
            Log.i("ASDF", "MOVE");
            if(this.speed_up < 25) {

                    this.speed_up += this.speed_up_inc;

            }

            if (this.lockjaw != null) {
                if(this.lockjaw.getTranslate_y() > 0 && this.lockjaw.getTranslate_y() < maxHeight) {
                    if (y > maxWidth / 4) {
                        this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() + this.speed_up, 1);
                    } else {
                        this.lockjaw.setPosition(this.lockjaw.getTranslate_x(), this.lockjaw.getTranslate_y() - this.speed_up, 1);
                    }
                }
            }
        }


        if(action == MotionEvent.ACTION_UP){
                Log.i("ASDF", "UP");
                this.speed_up = 0.2f;
        }

    }
}
