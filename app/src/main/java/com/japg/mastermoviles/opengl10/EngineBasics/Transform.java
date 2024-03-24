package com.japg.mastermoviles.opengl10.EngineBasics;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

public class Transform {

    String mName;
    Vec3 mPosition;
    Vec3 mAnglesEuler;
    Mat4 mModelMatrix;


    //private List<Vector<Float>> transformStack;

    Transform(String name) {
        mName = name;
        mPosition = new Vec3(0, 0,0 );
        mAnglesEuler = new Vec3(0,0,0);
        mModelMatrix = new Mat4().identity();

//        transformStack = new ArrayList<Vector<Float>>();
    }

    public void Translate(int offset, float positionX, float positionY, float positionZ) {

        System.out.println( mName +  ": Posx = " + positionX + " Posy = " + positionY + " Posz = "+ positionZ);

        mPosition.plusAssign(new Vec3(positionX, positionY, positionZ));

        System.out.println( mName +  ": x = " +mPosition.get(0) + " y = " + mPosition.get(1) + " z = "+ mPosition.get(2));

        Vector<Float> translation= new Vector<Float>(5);
        translation.add(0f);
        translation.add((float)offset);
        translation.add(positionX);
        translation.add(positionY);
        translation.add(positionZ);

     //   transformStack.add(translation);

        mModelMatrix.translateAssign(mPosition);
 //       translateM(modelMatrix, offset, positionX, positionY, positionZ);
    }

    public Mat4 GetModelMatrix() { return mModelMatrix;}

    public void SetModelMatrix(Mat4 modelMatrix) {
        this.mModelMatrix = modelMatrix;
    }

    public void Rotate(float a, float x, float y, float z) {
     //   mAnglesEuler.plusAssign(new Vec3(x, y, z).times(a));

        System.out.println( mName +  ": a = "+ a +" Axisx = " + x + " Axisy = " + y + " Axisz = "+ z);

        mModelMatrix.rotateAssign(a / 100, new Vec3(x, y, z).normalize());
    }

    public void PrepareDraw() {
        mModelMatrix.identity();
        mModelMatrix.translateAssign(mPosition);
    }
}
