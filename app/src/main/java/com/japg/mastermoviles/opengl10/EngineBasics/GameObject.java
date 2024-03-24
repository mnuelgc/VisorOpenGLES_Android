package com.japg.mastermoviles.opengl10.EngineBasics;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;

import android.content.Context;

import com.japg.mastermoviles.opengl10.util.TextureHelper;

import java.util.ArrayList;
import java.util.List;

import glm_.mat4x4.Mat4;


public class GameObject
{

	GameObject parent;
	List<GameObject> childs;
	Mesh mesh;

	Integer texture;
	int textureCreated;

	public Transform transform;
	Transform transformLocal;

	GameObject(Context context,
				List<Integer> meshesId,
				Integer mMesh,
				List<Integer> mTextures,
				Integer textureId,
				GameObject mParent,
				float xInicial, float yInicial, float zInicial) {

		parent = mParent;

		if (parent == null)
		{
			transform = new Transform("parentTransform");
			transform.Translate(0, xInicial,yInicial,zInicial);
		}
		else {
			transform = new Transform("Child transform");
			transformLocal = new Transform("Child localTransform");

			transformLocal.Translate(0, xInicial,yInicial,zInicial);

			transform.SetModelMatrix(parent.getModelMatrix().times(transformLocal.GetModelMatrix()));
		}

		if (meshesId != null)
		{
			childs = new ArrayList<GameObject>();
			for(int i = 0; i < meshesId.size(); i++)
			{
				GameObject child;
				if (i == 1 ) {
					child = new GameObject(
							context,
							meshesId.get(i),
							mTextures.get(0),
							this,
							0, 1.5f, 0);
				}
				else if( i == 2){
				child = new GameObject(
						context,
						meshesId.get(i),
						mTextures.get(0),
						this,
						0, 2.0f, 0);
				}
				else {
					child = new GameObject(
							context,
							meshesId.get(i),
							mTextures.get(0),
							this,
							0, 0, 0);

				}
				childs.add(child);
			}
		}


		if (mMesh != null) mesh = new Mesh(context, mMesh);
		else mesh = null;

		texture = textureId;

	}


	/* Parent GameObject */
	public GameObject(Context context,
			   List<Integer> meshesId,
			   List<Integer> mTextures,
			   float xInicial, float yInicial, float zInicial)
	{
		this(context,
				meshesId, null,
				mTextures,null,
				null,
				xInicial, yInicial, zInicial);
	}

	/* Child GameObject */

	GameObject(Context context,
			   Integer mMesh,
			   Integer textureId,
			   GameObject mParent,
			   float xInicial, float yInicial, float zInicial) {
		this(context,
				null, mMesh,
				null, textureId,
				mParent,
				xInicial,yInicial,zInicial);
	}

	public void Translate(int offset, float x, float y, float z)
	{
		transform.Translate(offset, x, y, z);
	}

	public void Rotate(float a, float x, float y, float z)
	{
		transform.Rotate(a,x,y,z);
		if (childs != null) {
			ApplyRotationToChilds();
		}
	}

	public void ApplyRotationToChilds() {
		for (GameObject child: childs)
		{
			child.transform.SetModelMatrix(transform.GetModelMatrix().times(child.transformLocal.GetModelMatrix()));
		}
	}

	public void  Render(int aPositionLocation, int aNormalLocation, int aUVLocation, int uTextureLocation)
	{
		if (childs!= null) {
			for (GameObject child : childs) {
				child.Render(aPositionLocation, aNormalLocation, aUVLocation, uTextureLocation);
			}
		}
		if (mesh != null)
		{
			mesh.Render(aPositionLocation, aNormalLocation, aUVLocation, uTextureLocation, textureCreated);
		}
	}

	public Mat4 getModelMatrix() {return  transform.GetModelMatrix();}


	public void CreateTexture(Context context)
	{
		this.textureCreated = (texture == null ? 0 : TextureHelper.loadTexture(context, texture));

		if (childs != null)
		{
			for(GameObject child : childs) {
				child.CreateTexture(context);
			}
		}
	}

	public List<GameObject> getChilds() {
		return childs;
	}

	public void PrepareTransformToDraw() {transform.PrepareDraw();}
}


