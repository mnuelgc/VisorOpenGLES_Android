package com.japg.mastermoviles.opengl10.Shaders;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;

import android.content.Context;
import android.util.DebugUtils;
import android.util.Pair;

import com.japg.mastermoviles.opengl10.GameObject;
import com.japg.mastermoviles.opengl10.Mesh;

import java.util.HashMap;
import java.util.Map;

public class BasicShaderProgram extends Shader{

    //**UNIFORMS NAMES*//
    private static final String U_MVPMATRIX 		= "u_MVPMatrix";
    private static final String U_MVMATRIX 			= "u_MVMatrix";
    private static final String U_COLOR 			= "u_Color";
    private static final String U_TEXTURE 			= "u_TextureUnit";

    //** ATTRIBUTES NAMES*//

    private static final String A_POSITION = "a_Position";
    private static final String A_NORMAL   = "a_Normal";
    private static final String A_UV       = "a_UV";

    public BasicShaderProgram(Context context, int idResourceVertexShaderFile, int idResourceFragmentShaderFile) {
        super(context, idResourceVertexShaderFile, idResourceFragmentShaderFile);
    }

    @Override
    public void PrepareUniforms(){
        System.out.println("PROGRAM "+ programID);

        uniforms = new HashMap<String, Integer>();

        uniforms.put(U_MVPMATRIX, glGetUniformLocation(programID, U_MVPMATRIX));
        uniforms.put(U_MVMATRIX, glGetUniformLocation(programID, U_MVMATRIX));
        uniforms.put(U_COLOR, glGetUniformLocation(programID, U_COLOR));
        uniforms.put(U_TEXTURE, glGetUniformLocation(programID, U_TEXTURE));
    }

    @Override
    public void PrepareAttributes(){

        attributes = new HashMap<String, Integer>();

        attributes.put(A_POSITION, glGetAttribLocation(programID, A_POSITION));
       // glEnableVertexAttribArray(attributes.get(A_POSITION));

        attributes.put(A_NORMAL, glGetAttribLocation(programID, A_NORMAL));

     //   glEnableVertexAttribArray(attributes.get(A_NORMAL));

        attributes.put(A_UV, glGetAttribLocation(programID, A_UV));
       // glEnableVertexAttribArray(attributes.get(A_UV));


        for (Map.Entry<String, Integer> entry : attributes.entrySet()) {
            glEnableVertexAttribArray(entry.getValue());
        }


    }

    @Override
    public void RenderGameObject(GameObject gameObject, float[] projectionMatrix)
    {
        float [] MVP = new float[16];

        for(GameObject child : gameObject.getChilds()) {
            /*   for (Mesh mesh :  gameObject.getMeshes()) {*/
            //  multiplyMM(MVP, 0, projectionMatrix, 0, mesh.transform.GetModelMatrix(), 0);
            multiplyMM(MVP, 0, projectionMatrix, 0, child.getModelMatrix(), 0);

            // Env?a la matriz de proyecci?n multiplicada por modelMatrix al shader
            glUniformMatrix4fv(uniforms.get(U_MVPMATRIX), 1, false, MVP, 0);
            // Env?a la matriz modelMatrix al shader
            // glUniformMatrix4fv(uniforms.get(U_MVMATRIX), 1, false, mesh.transform.GetModelMatrix(), 0);
            glUniformMatrix4fv(uniforms.get(U_MVMATRIX), 1, false, child.getModelMatrix(), 0);
            // Actualizamos el color (Marr?n)
            //glUniform4f(uColorLocation, 0.78f, 0.49f, 0.12f, 1.0f);
            glUniform4f(uniforms.get(U_COLOR), 1.0f, 1.0f, 1.0f, 1.0f);

            /*  }*/
            //  System.out.println(uniforms.toString());
            //  System.out.println(attributes.toString());
            child.Render(attributes.get(A_POSITION), attributes.get(A_NORMAL), attributes.get(A_UV), uniforms.get(U_TEXTURE));
        }
    }

}
