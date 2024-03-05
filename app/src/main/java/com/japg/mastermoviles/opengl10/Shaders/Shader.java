package com.japg.mastermoviles.opengl10.Shaders;

import static android.opengl.GLES20.glUseProgram;

import android.content.Context;
import android.util.Pair;

import com.japg.mastermoviles.opengl10.GameObject;
import com.japg.mastermoviles.opengl10.R;
import com.japg.mastermoviles.opengl10.util.LoggerConfig;
import com.japg.mastermoviles.opengl10.util.ShaderHelper;
import com.japg.mastermoviles.opengl10.util.TextResourceReader;

import java.util.List;
import java.util.Map;

public class Shader {

    protected int programID;
    protected Map<String, Integer> uniforms;
    protected Map<String, Integer>   attributes;

    public Shader(Context context, int idResourceVertexShaderFile, int idResourceFragmentShaderFile)
    {
        String vertexShaderSource, fragmentShaderSource;
        int vertexShader, fragmentShader;

        vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, idResourceVertexShaderFile);
        fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, idResourceFragmentShaderFile);

        vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        programID = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        System.out.println("PROGRAM "+ programID);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(programID);
        }
    }
    public static void UseShader(Shader shader)
    {
        System.out.println("Using Shader "+ shader.getProgramID());

        glUseProgram(shader.getProgramID());
    }
    public int getProgramID() {
        return programID;
    }

    public Map<String, Integer>getUniforms() {
        return uniforms;
    }

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public void PrepareUniforms()
    {
        try{
            throw new Exception();
        }catch (Exception e)
        {
            System.err.println("Must Override method PrepareUniforms()");
        }
    }

    public void PrepareAttributes()
    {
        try{
            throw new Exception();
        }catch (Exception e)
        {
            System.err.println("Must Override method PrepareAttributes()");
        }
    }

    public void RenderGameObject(GameObject gameObject, float[] projectionMatrix)
    {
        try{
            throw new Exception();
        }catch (Exception e)
        {
            System.err.println("Must Override method RenderGameObject(GameObject gameObject, float[] projectionMatrix)");
        }
    }
}
