package a12.rag.deepseajaw.deepseajaw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLText {


    private float color_red;
    private float color_green;
    private float color_blue;
    private float color_alpha;
    private float bgcolor_red;
    private float bgcolor_green;
    private float bgcolor_blue;
    private float bgcolor_alpha;
    private float translate_x;
    private float translate_y;
    private float translate_z;
    private float scale_x;
    private float scale_y;
    private float scale_z;
    private GL10 openGL = null;
    private String text = null;
    private float[] pixel = null;
    private int[][] letter = null;
    private FloatBuffer buffer = null;

    public GLText(GL10 openGL) {

        this.openGL = openGL;
        this.openGL.glEnableClientState ( GL10.GL_VERTEX_ARRAY );

        this.color_red = 0;
        this.color_green = 0;
        this.color_blue = 0;
        this.color_alpha = 1;

        this.bgcolor_red = 1;
        this.bgcolor_green = 1;
        this.bgcolor_blue = 1;
        this.bgcolor_alpha = 0;

        this.translate_x = 0;
        this.translate_y = 0;
        this.translate_z = 0;

        this.scale_x = 1;
        this.scale_y = 1;
        this.scale_z = 1;


    }

    public void setTextColor(float red, float green, float blue, float alpha) {

        this.color_red = red;
        this.color_green = green;
        this.color_blue = blue;
        this.color_alpha = alpha;

    }

    public void setTextBackgroundColor(float red, float green, float blue, float alpha){

        this.bgcolor_red = red;
        this.bgcolor_green = green;
        this.bgcolor_blue = blue;
        this.bgcolor_alpha = alpha;

    }

    public void setTextPosition(float x, float y, float z){

        this.translate_x = x;
        this.translate_y = y;
        this.translate_z = z;

    }

    public void setTextSize(float size){

        this.scale_x = size;
        this.scale_y = size;
        this.scale_z = size;

    }

    public void setText(String text){
        this.text = text;
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

    public void drawText(){

        this.openGL.glEnable ( GL10.GL_BLEND );
        this.openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        this.openGL.glLoadIdentity ();
        this.pixel = new float[] {
                0, 0,
                0, 1,
                1, 0,
                1, 1
        };
        this.buffer = generateBuffer(this.pixel);
        this.openGL.glVertexPointer(2, GL10.GL_FLOAT, 0, this.buffer);

        int acumulo_pos = 0;
        for(int k = 0; k < this.text.length (); k++) {

            this.letter = null;
            this.letter = getArrayLetter ( (this.text.charAt ( k )+"").toLowerCase ().charAt ( 0 ) );
            acumulo_pos += (this.letter[0].length * this.scale_x) + 2;

            for (int i = this.letter[0].length-1; i >= 0; i--) {
                for (int j = this.letter.length-1; j >= 0; j--) {

                    this.openGL.glLoadIdentity ();
                    if(this.text.charAt ( k ) == 'g' || this.text.charAt ( k ) == 'p' || this.text.charAt ( k ) == 'q' || this.text.charAt ( k ) == 'y'){
                        this.openGL.glTranslatef ( this.translate_x + ((i-1) * this.scale_x) + (acumulo_pos), (this.translate_y - (j * scale_y)) - (2 * this.scale_y), this.translate_z );
                    }else {
                        this.openGL.glTranslatef ( this.translate_x + ((i-1) * this.scale_x) + (acumulo_pos), this.translate_y - (j * scale_y), this.translate_z );
                    }
                    this.openGL.glScalef ( this.scale_x, this.scale_y, this.scale_z );
                    if (this.letter[j][i] == 1) {
                        this.openGL.glColor4f ( this.color_red, this.color_green, this.color_blue, this.color_alpha );
                    } else {
                        this.openGL.glColor4f ( this.bgcolor_red, this.bgcolor_green, this.bgcolor_blue, this.bgcolor_alpha );
                    }
                    this.openGL.glDrawArrays ( GL10.GL_TRIANGLE_STRIP, 0, 4 );

                }
            }
        }

        this.openGL.glDisable ( GL10.GL_BLEND );

    }

    private int[][] getArrayLetter(char letter){

        int[][] arr_letter = null;

        switch (letter){

            case 'a':

                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 1},
                        {0, 1, 1, 0, 1},
                };
                break;

            case 'b':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 1, 1, 0},
                        {1, 1, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 1, 0, 0, 1},
                        {1, 0, 1, 1, 0},
                };
                break;

            case 'c':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case 'd':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {0, 1, 1, 0, 1},
                        {1, 0, 0, 1, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 1},
                        {0, 1, 1, 0, 1},
                };
                break;

            case 'e':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case 'f':
                arr_letter = new int[][]{
                        {0, 0, 1},
                        {0, 1, 0},
                        {1, 1, 1},
                        {0, 1, 0},
                        {0, 1, 0},
                        {0, 1, 0},
                        {0, 1, 0},
                        {0, 1, 0},
                };
                break;

            case 'g':
                arr_letter = new int[][]{
                        {0, 1, 1, 0, 1},
                        {1, 0, 0, 1, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 1},
                        {0, 1, 1, 0, 1},
                        {0, 0, 0, 0, 1},
                        {1, 1, 1, 1, 0},
                };
                break;
            case 'h':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 1, 1, 0},
                        {1, 1, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                };
                break;
            case 'i':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case 'j':
                arr_letter = new int[][]{
                        {0, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case 'k':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 1, 0},
                        {1, 0, 1, 0, 0},
                        {1, 1, 0, 0, 0},
                        {1, 0, 1, 0, 0},
                        {1, 0, 1, 0, 0},
                        {1, 0, 0, 1, 0},
                };
                break;
            case 'l':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case 'm':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 1, 1, 1, 1, 0},
                        {1, 1, 0, 1, 0, 0, 1},
                        {1, 0, 0, 1, 0, 0, 1},
                        {1, 0, 0, 1, 0, 0, 1},
                        {1, 0, 0, 1, 0, 0, 1},
                        {1, 0, 0, 1, 0, 0, 1},
                };
                break;
            case 'n':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                };
                break;
            case 'o':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;
            case 'p':
                arr_letter = new int[][]{
                        {1, 0, 1, 1, 0},
                        {1, 1, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 1, 0, 0, 1},
                        {1, 0, 1, 1, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case 'q':
                arr_letter = new int[][]{
                        {0, 1, 1, 0, 1},
                        {1, 0, 0, 1, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 1},
                        {0, 1, 1, 0, 1},
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                };
                break;
            case 'r':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 1, 0, 0},
                        {1, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case 's':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 0, 0},
                        {0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;
            case 't':
                arr_letter = new int[][]{
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {1, 1, 1, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 1, 0, 0},
                };
                break;
            case 'u':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 1},
                        {0, 1, 1, 0, 1},
                };
                break;
            case 'v':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                };
                break;
            case 'w':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 0, 0, 0, 1},
                        {1, 0, 0, 1, 0, 1, 0, 0, 1},
                        {0, 1, 0, 1, 0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 0, 1, 0, 1, 0},
                        {0, 0, 1, 0, 0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0, 0, 1, 0, 0},
                };
                break;
            case 'x':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1},
                        {0, 1, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 1, 0},
                        {1, 0, 0, 0, 1},
                };
                break;
            case 'y':
                arr_letter = new int[][]{
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0},
                };
                break;
            case 'z':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 1, 1, 1, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0},
                        {1, 1, 1, 1, 1},
                };
                break;

            case '0':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case '1':
                arr_letter = new int[][]{
                        {0, 0, 1, 0, 0},
                        {0, 1, 1, 0, 0},
                        {1, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                };
                break;

            case '2':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0},
                        {1, 1, 1, 1, 1},
                };
                break;

            case '3':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case '4':
                arr_letter = new int[][]{
                        {0, 0, 0, 1, 0},
                        {0, 0, 1, 1, 0},
                        {0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 0},
                        {1, 0, 0, 1, 0},
                        {1, 1, 1, 1, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 1, 0},
                };
                break;

            case '5':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 1},
                        {0, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 1, 1, 1, 0},
                        {0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case '6':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0},
                        {1, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case '7':
                arr_letter = new int[][]{
                        {1, 1, 1, 1, 1},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 1, 0},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0},
                };
                break;

            case '8':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case '9':
                arr_letter = new int[][]{
                        {0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 1},
                        {0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 1},
                        {0, 1, 1, 1, 0},
                };
                break;

            case ':':
                arr_letter = new int[][]{
                        {0},
                        {0},
                        {1},
                        {0},
                        {0},
                        {0},
                        {0},
                        {1},
                };
                break;

            case '.':
                arr_letter = new int[][]{
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {1},
                };
                break;
/*
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
*/





            default:
                arr_letter = new int[][]{
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                        {0},
                };
                break;


        }

        return arr_letter;

    }

}
