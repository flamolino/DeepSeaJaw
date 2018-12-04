package a12.rag.deepseajaw.deepseajaw;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private LinearLayout lay_title_screen_bg = null;
    private GLSurfaceView superficie_title = null;
    private TitleScreen animacao_title = null;
    private GLSurfaceView superficie_game = null;
    private GameScreen animacao_game = null;
    private boolean started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lay_title_screen_bg = findViewById(R.id.lay_title_screen_bg);

        this.superficie_title = new GLSurfaceView( this );
        this.animacao_title = new TitleScreen ( this );
        this.superficie_title.setRenderer ( this.animacao_title );
        this.lay_title_screen_bg.addView(this.superficie_title);
        this.started = false;

        Utilities.emitirSom(R.raw.music_fundo, this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(!started) {
                        this.lay_title_screen_bg.removeAllViews();
                        this.animacao_title = null;
                        this.superficie_title = null;

                        this.superficie_game = new GLSurfaceView(this);
                        this.animacao_game = new GameScreen(this);
                        this.superficie_game.setRenderer(this.animacao_game);
                        this.lay_title_screen_bg.addView(this.superficie_game);

                        this.started = true;
                }

                break;

            default:
                break;
        }

        if(started){
            this.animacao_game.touch(event);
        }

        return super.onTouchEvent(event);
    }
}
