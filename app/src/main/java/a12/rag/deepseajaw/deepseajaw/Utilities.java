package a12.rag.deepseajaw.deepseajaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utilities {

    /*
Embaralha conteúdo de uma String
*/
    public static String shuffleString(String s) {
        List<String> letters = Arrays.asList(s.split(""));
        Collections.shuffle(letters);
        StringBuilder t = new StringBuilder(s.length());
        for (String k : letters) {
            t.append(k);
        }
        return t.toString();
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    public static float[] getFloatOpenGLColorFromColorsXML(int id, Context context){

        int color_base = context.getResources().getColor(id);
        int red = Color.red(color_base);
        int green = Color.green(color_base);
        int blue = Color.blue(color_base);
        int alpha = Color.alpha(color_base);

        return new float[]{
                (red / 255f),
                (green / 255f),
                (blue / 255f),
                (alpha / 255f)
        };
    }

    /*
    Método responsável por reproduzir um som que esteja dentro da pasta res/raw.
 */
    public static void emitirSom(int som, Context context) {

        MediaPlayer mp = MediaPlayer.create(context, som );
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener () {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        mp.start();

    }

    /*
    Método que gera uma cor
    */
    public static int randomizaCor() {

        Random r = new Random();

        int red, green, blue;

        int sw = r.nextInt (3);
        if (sw == 0){
            red = 124;
        } else if(sw == 1){
            red = 174;
        } else {
            red = 244;
        }
        sw = r.nextInt (3);
        if (sw == 0){
            green = 124;
        } else if(sw == 1){
            green = 174;
        } else {
            green = 244;
        }

        sw = r.nextInt (3);
        if (sw == 0){
            blue = 124;
        } else if(sw == 1){
            blue = 174;
        } else {
            blue = 244;
        }

        while(red > 190 && green > 190 && blue > 190){

            sw = r.nextInt (3);
            if (sw == 0){
                if(r.nextBoolean ()){
                    red = 124;
                } else {
                    red = 174;
                }
            } else if(sw == 1){
                if(r.nextBoolean ()){
                    green = 124;
                } else {
                    green = 174;
                }
            } else {
                if(r.nextBoolean ()){
                    blue = 124;
                } else {
                    blue = 174;
                }
            }

        }

        return Color.argb(255, red, green, blue);

    }

    /*
    Método que retorna um imagem da pasta assets
 */
    public static Drawable getImageInAssets(String pasta, String item, Context context)  {

        InputStream imageStream = null;
        Drawable drawable = null;

        try {
            imageStream  = context.getAssets().open( "atividade-completa-imagem/" +item+".png");
            drawable= Drawable.createFromStream(imageStream, null);
        }
        catch(IOException ex) {
            return null;
        }

        if(imageStream !=null){
            try {
                imageStream.close();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

        return drawable;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Bitmap loadBitmapFromView(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("ERROBITM", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }



    public static void removeViewComFadeOut(final ViewGroup layout, final View viewClose, int duracao)
    {
        Animation fadeOut = new AlphaAnimation (1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator ());
        fadeOut.setDuration(duracao);

        fadeOut.setAnimationListener(new Animation.AnimationListener ()
        {
            public void onAnimationEnd(Animation animation)
            {
                layout.removeView ( viewClose );
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        layout.startAnimation(fadeOut);
    }

    public static void atualizaNota(TextView lbl_try, TextView lbl_nota, TextView lbl_hit) {

        int hit, tentativa, nota;

        hit = Integer.valueOf ( lbl_hit.getText () +"" );
        tentativa = Integer.valueOf ( lbl_try.getText () + "" );
        nota = (int) Math.floor ((((hit * 100) / tentativa) / 10) );
        lbl_nota.setText ( String.valueOf ( nota ) );

        if(nota <= 3){
            lbl_nota.setBackgroundColor ( Color.argb ( 255, 255, 45, 45 ) );
        } else if (nota >= 4 && nota <= 6){
            lbl_nota.setBackgroundColor ( Color.argb ( 255, 255, 255, 45 ) );
        } else {
            lbl_nota.setBackgroundColor ( Color.argb ( 255, 75, 215, 75 ) );
        }

    }

    public static String trataStringParaDrawable(String str){
        String str_tradada = str.toLowerCase ();

        str_tradada.replace ( '-', '_' );

        str_tradada = Normalizer.normalize(str_tradada, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        return str_tradada;
    }

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader (context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public static FloatBuffer generateOpenGLBuffer(float[] vetor) {

        ByteBuffer prBuffer = ByteBuffer.allocateDirect(vetor.length * 4);
        prBuffer.order( ByteOrder.nativeOrder());
        FloatBuffer prFloat = prBuffer.asFloatBuffer();
        prFloat.clear();
        prFloat.put(vetor);
        prFloat.flip();
        return prFloat;

    }


}
