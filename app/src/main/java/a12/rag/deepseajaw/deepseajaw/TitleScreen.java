package a12.rag.deepseajaw.deepseajaw;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TitleScreen implements GLSurfaceView.Renderer {

    private Context context = null;
    private GLImage background = null;
    private GLImage coral1 = null;
    private GLImage coral2 = null;
    private GLImage coral3 = null;
    private GLImage coral4 = null;
    private GLImage coral5 = null;
    private GLImage title = null;
    private GLImage lockjaw = null;
    private int maxWidth, maxHeight;
    private float speed_jaw, frame_jaw;


    public TitleScreen(Context context){
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glClearColor(0,0,0,1);
        this.frame_jaw = 1;
        this.speed_jaw = 0.18f;

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
        int bg_h = (height);
        this.background.setSize(bg_w, bg_h);

        this.title = new GLImage(gl, this.context);
        this.title.setImage(R.drawable.title);
        this.title.setAlign(GLImage.ALIGN_CENTER);
        this.title.setAngle(180);
        this.title.setSize(bg_w, bg_h);

        float prop = 7;
        float met = 2;

        this.coral1 = new GLImage(gl, this.context);
        this.coral1.setImage(R.drawable.coralaranja);
        this.coral1.setPosition(width/met, (width/prop)/met, 1 );
        this.coral1.setAngle(180);
        this.coral1.setSize(width/prop);


        this.lockjaw = new GLImage(gl, this.context);
        this.lockjaw.setImage(R.drawable.lockjaw_sprite_chart);
        this.lockjaw.setFrames(4,3,8);
        this.lockjaw.setAlign(GLImage.ALIGN_CENTER);
        this.lockjaw.setAngle(180);
        this.lockjaw.setSize(width/prop/2f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        this.background.drawImage();
        this.title.drawImage();



        if(this.frame_jaw > 8){
            this.frame_jaw = 1;
        }

        this.frame_jaw += this.speed_jaw;

        this.lockjaw.drawFrame(this.frame_jaw, GLImage.DIRECTION_CLOCKWISE);

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

}
