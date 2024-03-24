package com.japg.mastermoviles.opengl10.Shaders;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.content.Context;

import com.japg.mastermoviles.opengl10.EngineBasics.GameObject;

import java.util.HashMap;
import java.util.Map;

import glm_.mat4x4.Mat4;

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
        attributes.put(A_NORMAL, glGetAttribLocation(programID, A_NORMAL));
        attributes.put(A_UV, glGetAttribLocation(programID, A_UV));

        for (Map.Entry<String, Integer> entry : attributes.entrySet()) {
            glEnableVertexAttribArray(entry.getValue());
        }


    }

    @Override
    public void RenderGameObject(GameObject gameObject, Mat4 projectionMatrix)
    {

//        System.out.println(projectionMatrix.toString());

        for(GameObject child : gameObject.getChilds()) {
            Mat4 mvp = projectionMatrix.times(child.getModelMatrix());


            // Env?a la matriz de proyecci?n multiplicada por modelMatrix al shader
            glUniformMatrix4fv(uniforms.get(U_MVPMATRIX), 1, false, mvp.toFloatArray(), 0);
            // Env?a la matriz modelMatrix al shader
            glUniformMatrix4fv(uniforms.get(U_MVMATRIX), 1, false, child.getModelMatrix().toFloatArray(), 0);
            // Actualizamos el color (Marr?n)
            glUniform4f(uniforms.get(U_COLOR), 1f, 1f, 1f, 1.0f);

            child.Render(attributes.get(A_POSITION), attributes.get(A_NORMAL), attributes.get(A_UV), uniforms.get(U_TEXTURE));
        }
    }

}
