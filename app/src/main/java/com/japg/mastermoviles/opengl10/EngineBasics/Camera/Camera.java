package com.japg.mastermoviles.opengl10.EngineBasics.Camera;

import glm_.func.common.func_vector1_common;
import glm_.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

public class Camera {
    final Vec3 up = new Vec3(0f,1f,0f);

    Vec3 cameraPos, cameraTarget, cameraDirection, cameraRight, cameraUp;
    Mat4 view;
    public Camera()
    {
        cameraPos = new Vec3(0.0f,0.0f,0.0f);
        cameraTarget = new Vec3(0.0f,0.0f,7.0f);
        cameraDirection = cameraPos.minus(cameraTarget);
        cameraRight = up.cross(cameraDirection).normalize();
        cameraUp = cameraDirection.cross(cameraRight);

        view= glm.INSTANCE.lookAt(
                new Vec3( 0f,0f,0f),
                new Vec3(0f,0f,7f),
                new Vec3(0f,1f,0f));//cameraPos, cameraTarget, cameraUp);

    }

    public Mat4 GetProjectView() {return view;}


    public void setPerspective(float fovY, float aspect, float near, float far)
    {
        glm.INSTANCE.perspective(fovY,aspect,near,far,view);
    }
}
