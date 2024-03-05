package com.japg.mastermoviles.opengl10;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import android.content.Context;
import android.opengl.Matrix;

import com.japg.mastermoviles.opengl10.Transform;
import com.japg.mastermoviles.opengl10.util.Resource3DSReader;
import com.japg.mastermoviles.opengl10.util.TextureHelper;

import java.util.ArrayList;
import java.util.List;


public class GameObject
{

	GameObject parent;
	List<GameObject> childs;
	Mesh mesh;

	Integer texture;
	int textureCreated;

	Transform transform;
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
			transform = new Transform();
			transform.Translate(0, xInicial,yInicial,zInicial);
		}
		else {
			transform = new Transform();
			transformLocal = new Transform();

			transformLocal.Translate(0, xInicial,yInicial,zInicial);

			multiplyMM(transform.GetModelMatrix(), 0, parent.transform.GetModelMatrix(), 0, transformLocal.GetModelMatrix(), 0);
		}

		if (meshesId != null)
		{
			childs = new ArrayList<GameObject>();
			for(int i = 0; i < meshesId.size(); i++)
			{
				GameObject child;
				if (i == 1) {
					child = new GameObject(
							context,
							meshesId.get(i),
							mTextures.get(0),
							this,
							0, 1.5f, 0);
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
	GameObject(Context context,
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
			multiplyMM(child.transform.GetModelMatrix(), 0, transform.GetModelMatrix(), 0, child.transformLocal.GetModelMatrix(), 0);
		}
	}

	public void  Render(int aPositionLocation, int aNormalLocation, int aUVLocation, int uTextureLocation)
	{
		//transform.PrepareDraw();

		if (childs!= null) {
			for (GameObject child : childs) {
				child.Render(aPositionLocation, aNormalLocation, aUVLocation, uTextureLocation);
			}
		}
		if (mesh != null)
		{
		//	transform.PrepareDraw();
			mesh.Render(aPositionLocation, aNormalLocation, aUVLocation, uTextureLocation, textureCreated);
		}
	}

	public float[] getModelMatrix() {return  transform.GetModelMatrix();}


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


