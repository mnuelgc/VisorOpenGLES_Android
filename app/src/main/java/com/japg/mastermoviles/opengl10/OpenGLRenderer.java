package com.japg.mastermoviles.opengl10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.japg.mastermoviles.opengl10.EngineBasics.Camera.Camera;
import com.japg.mastermoviles.opengl10.EngineBasics.GameObject;
import com.japg.mastermoviles.opengl10.Shaders.BasicShaderProgram;
import com.japg.mastermoviles.opengl10.Shaders.Shader;
import com.japg.mastermoviles.opengl10.util.LoggerConfig;
import com.japg.mastermoviles.opengl10.util.Resource3DSReader;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_MAX_TEXTURE_IMAGE_UNITS;
import static android.opengl.GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glViewport;

public class OpenGLRenderer implements Renderer {
	private static final String TAG = "OpenGLRenderer";
	private static final int BYTES_PER_FLOAT = 4;
	
	private final Context context;

	// Rotación alrededor de los ejes
	private float rX = 0f;
	private float orgRX = 0f;
	private float rY = 0f;
	private float orgRY = 0f;


	private float preRX = 0f;
	private float preRY = 0f;

	private BasicShaderProgram probeShader;

	private GameObject monkey;
	private Camera camera;
	
	public OpenGLRenderer(Context context) {
		this.context = context;

		List<Integer> monkeyIds = new ArrayList<Integer>();
		monkeyIds.add(R.raw.cubo);
		monkeyIds.add(R.raw.mono);

		List<Integer> monkeyTextIds = new ArrayList<Integer>();
		monkeyTextIds.add(R.drawable.mono_tex);
		monkey = new GameObject(context,
								monkeyIds,
								monkeyTextIds,
								0, 0, -7);
		camera = new Camera();


	//	for (int i = 0; i < camera.get)
	}
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		int[]	maxVertexTextureImageUnits = new int[1];
		int[]	maxTextureImageUnits       = new int[1];
			
		// Comprobamos si soporta texturas en el vertex shader
		glGetIntegerv(GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, maxVertexTextureImageUnits, 0);
		if (LoggerConfig.ON) {
			Log.w(TAG, "Max. Vertex Texture Image Units: "+maxVertexTextureImageUnits[0]);
		}
		// Comprobamos si soporta texturas (en el fragment shader)
		glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, maxTextureImageUnits, 0);
		if (LoggerConfig.ON) {
			Log.w(TAG, "Max. Texture Image Units: "+maxTextureImageUnits[0]);
		}
		// Cargamos la textura desde los recursos

		monkey.CreateTexture(context);

		// Leemos los shaders
		if (maxVertexTextureImageUnits[0]>0) probeShader = new BasicShaderProgram(context, R.raw.specular_vertex_shader, R.raw.specular_fragment_shader);
		else probeShader = new BasicShaderProgram(context, R.raw.specular_vertex_shader2, R.raw.specular_vertex_shader2);

		// Activamos el programa OpenGL
		//glUseProgram(program);
		Shader.UseShader(probeShader);
		// Capturamos los uniforms
		probeShader.PrepareUniforms();
		// Capturamos los attributes
		probeShader.PrepareAttributes();
	}
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		UpdatePerspective(width, height);
	}

	private void UpdatePerspective(int width, int height)
	{
		glViewport(0, 0, width, height);
		final float aspectRatio = width > height ?
				(float) width / (float) height :
				(float) height / (float) width;
		if (width > height) {
			// Landscape
			//orthoM(projectionMatrix, 0, -aspectRatio*TAM, aspectRatio*TAM, -TAM, TAM, -100.0f, 100.0f);
			camera.setPerspective(aspectRatio);
			//frustum(projectionMatrix, 0, -aspectRatio*TAM, aspectRatio*TAM, -TAM, TAM, 1f, 1000.0f);
		} else {
			// Portrait or square
			//orthoM(projectionMatrix, 0, -TAM, TAM, -aspectRatio*TAM, aspectRatio*TAM, -100.0f, 100.0f);
			camera.setPerspective(1f/aspectRatio);
			//frustum(projectionMatrix, 0, -TAM, TAM, -aspectRatio*TAM, aspectRatio*TAM, 1f, 1000.0f);
		}
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glLineWidth(2.0f);

		monkey.PrepareTransformToDraw();

		monkey.Rotate(  rX , 1, 0, 0);
		monkey.Rotate(  rY,0, 1, 0);
		monkey.getChilds().get(1).transform.Rotate(System.nanoTime() /10000000f, 0.0f, 1.0f, 0.0f);


		probeShader.RenderGameObject(monkey, camera.GetProjectView());
	}
	
	public void handleTouchPress(float normalizedX, float normalizedY) {
		if (LoggerConfig.ON) {
			Log.w(TAG, "Touch Press ["+normalizedX+", "+normalizedY+"]");
			//rX = -normalizedY*180f;
			//rY = normalizedX*180f;

			//preRY += rY;
			//preRX += rX;

			//rY = 0;
			//rX =0;
		}
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		if (LoggerConfig.ON) {
			Log.w(TAG, "Touch Drag ["+normalizedX+", "+normalizedY+"]");
		}
		rX = -normalizedY*180f;
		rY = normalizedX*180f;
		//monkey.Rotate( -System.nanoTime() /1000000000000f,0, 1, 0);

		//rX -=orgRX;
		//rY -=orgRY;

		System.out.println("RY: " + rY );
		System.out.println("RX: " + rX );

	}

	public void handleZoomIn(int width, int height)
	{
		camera.moveForward();
		//UpdatePerspective(width, height);
	}
	public void handleZoomOut(int width, int height)
	{
		camera.moveBackwards();
		//UpdatePerspective(width, height);
	}
}