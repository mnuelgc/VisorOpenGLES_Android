package com.japg.mastermoviles.opengl10.EngineBasics.Camera;

import glm_.func.common.func_vector1_common;
import glm_.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

public class Camera {
    final Vec3 up = new Vec3(0f,1f,0f);

    float fov;
    float aspect, near, far;
    Vec3 cameraPos, cameraTarget, cameraDirection, cameraRight, cameraUp;
    Mat4 view;
    public Camera()
    {
        cameraPos = new Vec3(0.0f,0.0f,0.0f);
        cameraTarget = new Vec3(0.0f,0.0f,-7.0f);
        cameraDirection = cameraPos.minus(cameraTarget);
        cameraRight = up.cross(cameraDirection).normalize();
        cameraUp = cameraDirection.cross(cameraRight);
        fov = 45;
        near = 0.01f;
        far = 1000f;

        view= glm.INSTANCE.lookAt(cameraPos, cameraTarget, cameraUp);

        System.out.println("Pos Cam: " + cameraPos.toString());
        System.out.println("View: " + view.toString());

    }

    public Mat4 GetProjectView() {return view;}


    public void setPerspective(float aspect)
    {
        this.aspect = aspect;
        System.out.println("Prev View: " + view.toString());
        glm.INSTANCE.perspective(fov,aspect,near,far,view);
        System.out.println("View: " + view.toString());

    }

    public void updatePerspective()
    {
        System.out.println("FOV: " + fov);
        glm.INSTANCE.perspective(fov,aspect,near,far,view);
        System.out.println("View: " + view.toString());
    }


    public void moveForward()
    {
        cameraPos.plusAssign(cameraDirection);
        view = glm.INSTANCE.lookAt(cameraPos, cameraPos.plus(cameraTarget), cameraUp);
        fov -= .1f;
        System.out.println("FOV Rest: " + fov);
        fov = (fov <= 44) ? 44 : fov;
        updatePerspective();
        System.out.println("Pos Cam: " + cameraPos.toString());
        System.out.println("View: " + view.toString());
    }
    public void moveBackwards()
    {
        cameraPos.plusAssign(cameraDirection);
        view = glm.INSTANCE.lookAt(cameraPos, cameraPos.plus(cameraTarget), cameraUp);
        fov += .1f;
        System.out.println("FOV Sum: " + fov);
        fov = (fov >= 46) ? 46 : fov;
        updatePerspective();
        System.out.println("Pos Cam: " + cameraPos.toString());
        System.out.println("View: " + view.toString());
    }
}
