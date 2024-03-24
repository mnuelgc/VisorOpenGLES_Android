package com.japg.mastermoviles.opengl10;

import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glVertexAttribPointer;




import android.content.Context;
import android.os.Debug;

import com.japg.mastermoviles.opengl10.util.Resource3DSReader;
import com.japg.mastermoviles.opengl10.util.TextureHelper;

import java.nio.Buffer;
import java.util.concurrent.TimeUnit;

public class Mesh{

    private static final int BYTES_PER_FLOAT = 4;

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int UV_COMPONENT_COUNT = 2;
    // C?lculo del tama?o de los datos (3+3+2 = 8 floats)
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT + UV_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    Resource3DSReader model;

    float offsetPosX, offsetPosY, offsetPosZ;
    float offsetAngleX, offsetAngleY, offsetAngleZ;

    int texture;
    int mTextureId;

    public Transform transform;

    Context cont;

    public Mesh(Context context, Integer resourceId)
    {
        model = new Resource3DSReader();
        model.read3DSFromResource(context, resourceId != null ? resourceId : 0);
        transform = new Transform("Mesh transform");

        mTextureId = Integer.MIN_VALUE;

        offsetPosX = 0;
        offsetPosY = 0;
        offsetPosZ = 0;
        offsetAngleX = 0;
        offsetAngleY = 0;
        offsetAngleZ = 0;


    }


    public Mesh(Context context, Integer resourceId, Transform parent, int textureId)
    {
        model = new Resource3DSReader();
        model.read3DSFromResource(context, resourceId != null ? resourceId : 0);
        transform = new Transform( "Mesh Transform");


        transform.SetModelMatrix(parent.GetModelMatrix());

        mTextureId = textureId;

        offsetPosX = 0;
        offsetPosY = 0;
        offsetPosZ = 0;
        offsetAngleX = 0;
        offsetAngleY = 0;
        offsetAngleZ = 0;

        System.out.println("Model matrix cube "+ transform.GetModelMatrix());
    }
    public Mesh(Context context, Integer resourceId,
                float oPX, float oPY, float oPZ,
                float oAX, float oAY, float oAZ)
    {
        model = new Resource3DSReader();
        model.read3DSFromResource(context, resourceId != null ? resourceId : 0);
        transform = new Transform("Mesh Transform");

        mTextureId = Integer.MIN_VALUE;

        offsetPosX = oPX;
        offsetPosY = oPY;
        offsetPosZ = oPZ;
        offsetAngleX = oAX;
        offsetAngleY = oAY;
        offsetAngleZ = oAZ;

        transform.Translate(0, offsetPosX, offsetPosY, offsetPosZ);
    }

    public Mesh(Context context, Integer resourceId, Transform parent,  int textureId,
                float oPX, float oPY, float oPZ,
                float oAX, float oAY, float oAZ)
    {
        model = new Resource3DSReader();
        model.read3DSFromResource(context, resourceId != null ? resourceId : 0);
        transform = new Transform("Mesh Transform");

        mTextureId = textureId;

        offsetPosX = oPX;
        offsetPosY = oPY;
        offsetPosZ = oPZ;
        offsetAngleX = oAX;
        offsetAngleY = oAY;
        offsetAngleZ = oAZ;

        transform.SetModelMatrix(parent.GetModelMatrix());
        transform.Translate(0, offsetPosX, offsetPosY, offsetPosZ);

        System.out.println("Model matrix mono "+ transform.GetModelMatrix());

    }
    public void CreateTexture(Context context)
    {
        this.texture = (mTextureId == Integer.MIN_VALUE ? Integer.MIN_VALUE :  TextureHelper.loadTexture(context, mTextureId));
    }

    public void Render(int aPositionLocation, int aNormalLocation, int aUVLocation, int uTextureLocation , int text)
    {

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, text);
        glUniform1f(uTextureLocation, 0);

        for (int i=0; i<model.numMeshes; i++) {
            // Asociando vértices con su attribute

            final Buffer position = model.dataBuffer[i].position(0);
            glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                    false, STRIDE, model.dataBuffer[i]);

            // Asociamos el vector de normales
            model.dataBuffer[i].position(POSITION_COMPONENT_COUNT);
            glVertexAttribPointer(aNormalLocation, NORMAL_COMPONENT_COUNT, GL_FLOAT,
                    false, STRIDE, model.dataBuffer[i]);

            // Asociamos el vector de UVs
            model.dataBuffer[i].position(POSITION_COMPONENT_COUNT+NORMAL_COMPONENT_COUNT);
            glVertexAttribPointer(aUVLocation, NORMAL_COMPONENT_COUNT, GL_FLOAT,
                    false, STRIDE, model.dataBuffer[i]);
            glDrawArrays(GL_TRIANGLES, 0, model.numVertices[i]);
        }
    }


    /*public void Rotate(float x, float y, float z) {
    }*/
}
