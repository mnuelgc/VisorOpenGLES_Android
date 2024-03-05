package com.japg.mastermoviles.opengl10;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Transform {
    private Vector<Float> mPosition;
    private float[] mAnglesEuler;
    private float[] modelMatrix;

    private List<Vector<Float>> transformStack;

    Transform() {
        mPosition = new Vector<Float>(3);
        mAnglesEuler = new float[3];
        modelMatrix = new float[16];
        setIdentityM(modelMatrix, 0);

        transformStack = new ArrayList<Vector<Float>>();


        mPosition.add(0f);
        mPosition.add(0f);
        mPosition.add(0f);

        mAnglesEuler[0] = 0;
        mAnglesEuler[1] = 0;
        mAnglesEuler[2] = 0;
    }

    public void Translate(int offset, float positionX, float positionY, float positionZ) {
        mPosition.setElementAt(mPosition.elementAt(0) + positionX, 0 );
        mPosition.setElementAt(mPosition.elementAt(1) + positionY, 1 );
        mPosition.setElementAt(mPosition.elementAt(2) + positionZ, 2 );

        System.out.println(mPosition.get(0));
        System.out.println(mPosition.get(1));
        System.out.println(mPosition.get(2));

        Vector<Float> translation= new Vector<Float>(5);
        translation.add(0f);
        translation.add((float)offset);
        translation.add(positionX);
        translation.add(positionY);
        translation.add(positionZ);

        transformStack.add(translation);

        translateM(modelMatrix, offset, positionX, positionY, positionZ);
    }

    public float[] GetModelMatrix() { return modelMatrix;}

    public void SetModelMatrix(float[] modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void Rotate(float a, float x, float y, float z) {
        mAnglesEuler[0] += x * a;
        mAnglesEuler[1] += y * a;
        mAnglesEuler[2] += z * a;

        Vector<Float> rotation= new Vector<Float>(6);
        rotation.add(1f);
        rotation.add((float)0);
        rotation.add(x);
        rotation.add(y);
        rotation.add(z);
        rotation.add(a);

        transformStack.add(rotation);

        rotateM(modelMatrix, 0, a, x, y, z);
    }

    public void PrepareDraw() {
        if (mPosition.size() == 3) {
            setIdentityM(modelMatrix, 0);
            translateM(modelMatrix, 0, mPosition.get(0), mPosition.get(1), mPosition.get(2));
        }
    }
}
