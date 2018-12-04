package a12.rag.deepseajaw.deepseajaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLUtils;
import android.view.Display;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class GLImage {

    public static int DIRECTION_CLOCKWISE = -1;
    public static int DIRECTION_COUNTER_CLOCKWISER = 1;
    public static int ALIGN_TOP_CENTER = 1;
    public static int ALIGN_BOTTOM_CENTER = 2;
    public static int ALIGN_LEFT_CENTER = 3;
    public static int ALIGN_RIGHT_CENTER = 4;
    public static int ALIGN_TOP_LEFT = 5;
    public static int ALIGN_BOTTOM_LEFT = 6;
    public static int ALIGN_TOP_RIGHT = 7;
    public static int ALIGN_BOTTOM_RIGHT = 8;
    public static int ALIGN_CENTER = 9;
    private float translate_x;
    private float translate_y;
    private float translate_z;
    private float scale_x;
    private float scale_y;
    private float scale_z;
    private float rotate_angle;
    private GL10 openGL = null;
    private Context context = null;
    private float[] coord_img = null, coord_square = null;
    private FloatBuffer tex_coords = null, square_buffer = null;
    private int cod_texture;
    private int cod_image;
    private ArrayList<GLImage> lst_push_matrix = null;
    private int rotation_speed, rotation_direction;
    private boolean after_draw, isLoadImage;
    private int maxWidth, maxHeight, minWidth, minHeight;
    private int idTextura;
    private ArrayList<float[]> lst_frames = null;
    private ArrayList<float[]> lst_frames_espelho = null;
    private boolean grab;
    private int relaction;
    private int[] start_position;
    private boolean visible = true;
    private float frame_pos;

    public GLImage(GL10 openGL, Context context){

        this.openGL = openGL;
        this.context = context;

        this.translate_x = 0;
        this.translate_y = 0;
        this.translate_z = 0;

        this.scale_x = 1;
        this.scale_y = 1;
        this.scale_z = 1;

        this.rotate_angle = 0;

        this.cod_image = 0;
        this.cod_texture = 0;

        this.lst_push_matrix = new ArrayList<> (  );

        this.rotation_speed = 0;
        this.rotation_direction = 1;

        this.after_draw = true;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.maxWidth = size.x;
        this.maxHeight = (size.y - this.getStatusBarHeight ());

        this.minWidth = (int) (this.getScale_x () * (this.getScale_x () /2));
        this.minHeight = (int) (this.getScale_y () * (this.getScale_y () /2));

        this.idTextura = 0;

        this.isLoadImage = false;

        this.lst_frames = new ArrayList<> (  );
        this.lst_frames_espelho = new ArrayList<> (  );

        this.relaction = -1;

        this.setFrame_pos(1);

    }

    public int getRelaction(){
        return this.relaction;
    }

    public void setRelaction(int relaction){
        this.relaction = relaction;
    }

    public GLImage(GL10 openGL, Context context, int weight){

        this.openGL = openGL;
        this.context = context;

        this.translate_x = 0;
        this.translate_y = 0;
        this.translate_z = 0;

        this.scale_x = 1;
        this.scale_y = 1;
        this.scale_z = 1;

        this.rotate_angle = 0;

        this.cod_image = 0;
        this.cod_texture = 0;

        this.lst_push_matrix = new ArrayList<> (  );

        this.rotation_speed = 0;
        this.rotation_direction = 1;

        this.after_draw = true;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.maxWidth = size.x;
        this.maxHeight = (size.y - this.getStatusBarHeight ()) / weight;

        this.minWidth = (int) (this.getScale_x () * (this.getScale_x () /2));
        this.minHeight = (int) (this.getScale_y () * (this.getScale_y () /2));

        this.idTextura = 0;

        this.isLoadImage = false;

        this.lst_frames = new ArrayList<> (  );
        this.lst_frames_espelho = new ArrayList<> (  );

    }

    public void setOpenGL(GL10 openGL){
        this.openGL = openGL;
    }

    public void setImage(int r_image, int centerPoint){

        this.cod_image = r_image;
        this.isLoadImage = true;
        this.setGrab(false);
        setCenterPoint(centerPoint);
        this.setAfterDraw();

    }

    public void setImage(int r_image){

        this.cod_image = r_image;
        this.isLoadImage = true;
        this.setGrab(false);
        setCenterPoint(1);
        this.setAfterDraw();

    }

    public void setGrab(boolean grab){
        this.grab = grab;
    }

    public boolean isGrab() {
        return this.grab;
    }

    public void setPosition(float x, float y, float z){

        this.translate_x = x;
        this.translate_y = y;
        this.translate_z = z;

    }

    public void setSize(float size){

        this.scale_x = size;
        this.scale_y = size;
        this.scale_z = size;
        this.minWidth = (int) (this.getScale_x () * (this.getScale_x () /2));
        this.minHeight = (int) (this.getScale_y () * (this.getScale_y () /2));

    }

    public void setSize(float x, float y){

        this.scale_x = x;
        this.scale_y = y;
        this.scale_z = 1;
        this.minWidth = (int) (this.getScale_x () * (this.getScale_x () /2));
        this.minHeight = (int) (this.getScale_y () * (this.getScale_y () /2));

    }

    public void setAngle(float angle ){

        this.rotate_angle = angle;

    }

    public void setFrames(int cut_x, int cut_y, int max_frames){

            this.lst_frames.clear ();
            this.lst_frames_espelho.clear ();

            float x = 1.0f / cut_x;
            float y = 1.0f / cut_y;

            float coord_x = 0;
            float coord_y = 0;

            int cont = 1;

            for(float i = 0; i < 1f; i += y){

                for(float j = 0; j < 1f; j+= x){

                    coord_x = j;
                    coord_y = i;

                    float[] frame = {

                            coord_x,coord_y,
                            coord_x,coord_y+y,
                            coord_x+x,coord_y,
                            coord_x+x,coord_y+y

                    };
                    this.lst_frames.add ( frame );

                    float[] frame_espelho = {

                            coord_x+x,coord_y,
                            coord_x+x,coord_y+y,
                            coord_x,coord_y,
                            coord_x,coord_y+y

                    };
                    this.lst_frames_espelho.add ( frame_espelho );

                    cont++;
                    if(cont > max_frames){
                        break;
                    }

                }
                if(cont >= max_frames){
                    break;
                }

            }

    }

    public void setAlign(int align){

        switch (align){

            case 1:
                this.setPosition ( this.maxWidth/2, this.maxHeight - this.minHeight, 1 );
                break;
            case 2:
                this.setPosition ( this.maxWidth/2, this.minWidth, 1 );
                break;
            case 3:
                this.setPosition ( this.minWidth, this.maxHeight/2, 1 );
                break;
            case 4:
                this.setPosition ( this.maxWidth - minWidth, this.maxHeight/2, 1 );
                break;
            case 5:
                this.setPosition ( minWidth, this.maxHeight - this.minHeight, 1 );
                break;
            case 6:
                this.setPosition (  minWidth, this.minHeight, 1 );
                break;
            case 7:
                this.setPosition ( this.maxWidth - minWidth, this.maxHeight - minHeight, 1 );
                break;
            case 8:
                this.setPosition ( this.maxWidth - minWidth, this.minHeight, 1 );
                break;
            case 9:
                this.setPosition ( this.maxWidth/2, this.maxHeight/2, 1 );
                break;

        }

    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = this.context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setRotationSpeed(int speed){
        this.rotation_speed = speed;
    }

    public void setRotationDirection(int direction){
        this.rotation_direction = direction;
    }



    public void addPushMatrixObjects( GLImage image ){
        this.lst_push_matrix.add ( image );
    }

    private FloatBuffer generateBuffer(float[] vetor) {

        ByteBuffer prBuffer = ByteBuffer.allocateDirect(vetor.length * 4);
        prBuffer.order( ByteOrder.nativeOrder());
        FloatBuffer prFloat = prBuffer.asFloatBuffer();
        prFloat.clear();
        prFloat.put(vetor);
        prFloat.flip();
        return prFloat;

    }

    private int loadTexture(GL10 opengl, int codTextura, Context context) {

        //CARREGA A IMAGEM NA MEMORIA RAAAMMMM
        Bitmap imagem = BitmapFactory.decodeResource(context.getResources(), codTextura);

        //DEFINE UM ARRAY PARA ARMAZ. DOS IDS DE TEXTURA (APENAS 1 POSICAO)
        int[] idTextura = new int[1];

        //GERA AS AREAS NA GPU E CRIA UM ID PARA CADA UMA
        opengl.glGenTextures(1, idTextura, 0);

        //DIZER PARA A MAQUINA QUAL DAS AREAS CRIADAS NA VRAM EU QUERO TRABALHAR
        opengl.glBindTexture(GL10.GL_TEXTURE_2D, idTextura[0]);

        //COPIA A IMAGEM DA RAM PARA A VRAM
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, imagem,0);

        //CONFIGURA OS ALGORITMOS QUE SERAO UTILIZADOS PARA RECALCULAR A IMAGEM EM CASO DE ESCLA PARA MAIS OU MENOS (MIN E MAG)
        opengl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        //APONTA A VRAM OPENGL PARA O NADA (CODIGO ZERO)
        opengl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        //LIBERA A MEMORIA RAM
        imagem.recycle();

        return idTextura[0];
    }


    public void drawImage(){

        if(this.isVisible()) {
            if (this.isLoadImage) {

                this.openGL.glLoadIdentity();
                this.openGL.glBindTexture(GL10.GL_TEXTURE_2D, this.cod_texture);

                this.openGL.glTranslatef(this.getTranslate_x(), this.getTranslate_y(), this.translate_z);


                if (this.rotation_speed == 0) {
                    this.openGL.glRotatef(this.rotate_angle, 0, 0, 1);
                } else {

                    this.rotate_angle += this.rotation_speed;

                    this.openGL.glRotatef(this.rotate_angle * this.rotation_direction, 0, 0, 1);
                }

                this.openGL.glScalef(this.getScale_x(), this.getScale_y(), this.scale_z);
                this.openGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

                if (this.lst_push_matrix.size() > 0) {

                    for (int i = 0; i < this.lst_push_matrix.size(); i++) {
                        this.openGL.glPushMatrix();
                        this.lst_push_matrix.get(i).drawImageNoLoadIdentity();
                        this.openGL.glPopMatrix();
                    }


                }
            }
        }

    }

    public void drawImageNoLoadIdentity(){
        if(this.isVisible()) {
            if (this.isLoadImage) {

                this.openGL.glBindTexture(GL10.GL_TEXTURE_2D, this.cod_texture);

                this.openGL.glTranslatef(this.getTranslate_x(), this.getTranslate_y(), this.translate_z);


                if (this.rotation_speed == 0) {
                    this.openGL.glRotatef(this.rotate_angle, 0, 0, 1);
                } else {

                    this.rotate_angle += this.rotation_speed;

                    this.openGL.glRotatef(this.rotate_angle * this.rotation_direction, 0, 0, 1);
                }

                this.openGL.glScalef(this.getScale_x(), this.getScale_y(), this.scale_z);
                this.openGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

                if (this.lst_push_matrix.size() > 0) {


                    for (int i = 0; i < this.lst_push_matrix.size(); i++) {
                        this.openGL.glPushMatrix();
                        this.lst_push_matrix.get(i).drawImageNoLoadIdentity();
                        this.openGL.glPopMatrix();
                    }


                }
            }
        }

    }

    private void setAfterDraw(){

        this.openGL.glEnable ( GL10.GL_BLEND );
        this.openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        this.square_buffer = generateBuffer(this.coord_square);
        this.openGL.glVertexPointer(2, GL10.GL_FLOAT, 0, this.square_buffer);

        this.coord_img = new float[] {
                1,0,
                1,1,
                0,0,
                0,1
        };



        //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
        this.openGL.glEnable(GL10.GL_TEXTURE_2D);

        //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
        this.openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //CRIANDO O FLOAT BUFFER A PARTIR DO VETOR JAVA
        this.tex_coords = generateBuffer(this.coord_img);

        //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
        this.openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.tex_coords);

        this.cod_texture = loadTexture (this.openGL, this.cod_image, this.context);

        //ASSINAR A TEXTURA QUE A OPENGL VAI UTILIZAR NO DESENHO DA PRIMITIVA
        this.openGL.glBindTexture(GL10.GL_TEXTURE_2D, this.cod_texture);

    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCenterPoint(int point){

        switch (point){
            case 1://center
                this.coord_square = new float[] {
                        -this.getScale_x () /2, -this.getScale_y () /2,
                        -this.getScale_x () /2, this.getScale_y () /2,
                        this.getScale_x () /2, -this.getScale_y () /2,
                        this.getScale_x () /2, this.getScale_y () /2
                };
                break;

            case 2://leftCenter
                this.coord_square = new float[] {
                        0, -this.getScale_y () /2,
                        0, this.getScale_y () /2,
                        this.getScale_x (), -this.getScale_y () /2,
                        this.getScale_x (), this.getScale_y () / 2
                };
                break;

            default:
                break;
        }

    }



    public void drawFrame(float index_anim, int direction) {

        if(this.isLoadImage) {

            //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
            this.openGL.glEnable(GL10.GL_TEXTURE_2D);

            //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
            this.openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            if(index_anim < 1){
                index_anim = 0;
            }

            if(direction == -1) {
                this.tex_coords = generateBuffer ( this.lst_frames_espelho.get ( (int) (Math.floor ( index_anim ) - 1) ) );
            } else {
                this.tex_coords = generateBuffer ( this.lst_frames.get ( (int) (Math.floor ( index_anim ) - 1) ) );
            }

            //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
            this.openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.tex_coords);

            this.openGL.glLoadIdentity ();
            this.openGL.glBindTexture(GL10.GL_TEXTURE_2D, this.cod_texture);

            this.openGL.glTranslatef ( this.getTranslate_x (), this.getTranslate_y (), this.translate_z );


            if (this.rotation_speed == 0) {
                this.openGL.glRotatef ( this.rotate_angle, 0, 0, 1 );
            } else {

                this.rotate_angle += this.rotation_speed;

                this.openGL.glRotatef ( this.rotate_angle * this.rotation_direction, 0, 0, 1 );
            }

            this.openGL.glScalef ( this.getScale_x (), this.getScale_y (), this.scale_z );
            this.openGL.glDrawArrays ( GL10.GL_TRIANGLE_STRIP, 0, 4 );

            if (this.lst_push_matrix.size () > 0) {

                for (int i = 0; i < this.lst_push_matrix.size (); i++) {
                    this.openGL.glPushMatrix ();
                    this.lst_push_matrix.get ( i ).drawFrameNoLoadIdentity (index_anim, direction);
                    this.openGL.glPopMatrix ();
                }
            }

            //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
            this.openGL.glEnable(GL10.GL_TEXTURE_2D);

            //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
            this.openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


            this.tex_coords = generateBuffer ( this.coord_img );

            //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
            this.openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.tex_coords);

        }

    }

    private void drawFrameNoLoadIdentity(float index_anim, int direction) {

        if(this.isLoadImage) {

            //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
            this.openGL.glEnable(GL10.GL_TEXTURE_2D);

            //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
            this.openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            if(index_anim < 1){
                index_anim = 0;
            }

            if(direction == -1) {
                this.tex_coords = generateBuffer ( this.lst_frames_espelho.get ( (int) (Math.floor ( index_anim ) - 1) ) );
            } else {
                this.tex_coords = generateBuffer ( this.lst_frames.get ( (int) (Math.floor ( index_anim ) - 1) ) );
            }

            //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
            this.openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.tex_coords);

            this.openGL.glBindTexture(GL10.GL_TEXTURE_2D, this.cod_texture);

            this.openGL.glTranslatef ( this.getTranslate_x (), this.getTranslate_y (), this.translate_z );

            if (this.rotation_speed == 0) {
                this.openGL.glRotatef ( this.rotate_angle, 0, 0, 1 );
            } else {

                this.rotate_angle += this.rotation_speed;

                this.openGL.glRotatef ( this.rotate_angle * this.rotation_direction, 0, 0, 1 );
            }

            this.openGL.glScalef ( this.getScale_x (), this.getScale_y (), this.scale_z );
            this.openGL.glDrawArrays ( GL10.GL_TRIANGLE_STRIP, 0, 4 );

            if (this.lst_push_matrix.size () > 0) {



                for (int i = 0; i < this.lst_push_matrix.size (); i++) {
                    this.openGL.glPushMatrix ();
                    this.lst_push_matrix.get ( i ).drawFrameNoLoadIdentity (index_anim, direction);
                    this.openGL.glPopMatrix ();
                }



            }

            //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
            this.openGL.glEnable(GL10.GL_TEXTURE_2D);

            //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
            this.openGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


            this.tex_coords = generateBuffer ( this.coord_img );

            //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
            this.openGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.tex_coords);

        }

    }

    public float getTranslate_x() {
        return translate_x;
    }

    public float getTranslate_y() {
        return translate_y;
    }

    public float getScale_x() {
        return scale_x;
    }

    public float getScale_y() {
        return scale_y;
    }

    public int getScale_x_int() {
        return (int) scale_x;
    }

    public int getScale_y_int() {
        return (int) scale_y;
    }

    public int[] getStart_position() {
        return start_position;
    }

    public void setStart_position(int[] start_position) {
        this.start_position = start_position;
    }

    public float getFrame_pos() {
        return frame_pos;
    }

    public void setFrame_pos(float frame_pos) {
        this.frame_pos = frame_pos;
    }
}













