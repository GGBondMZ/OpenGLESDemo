package com.mz.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;

    private float[] mViewMatrix = new float[16];

    private float[] mProjectionMatrix = new float[16];

    private float[] mMVPMatrix = new float[16];

    private float[] mModelMatrix = new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建GLSurfaceView实例
        mGLSurfaceView = new GLSurfaceView(this);

        // 设置OpenGL ES版本为2.0
        mGLSurfaceView.setEGLContextClientVersion(2);

        // 设置渲染器
        mGLSurfaceView.setRenderer(new MyRenderer());

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    private class MyRenderer implements GLSurfaceView.Renderer {
        private Cylinder mCylinder;
        private float mAngle = 0.0f; // 旋转角度

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 设置背景色为黑色
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            // 初始化圆柱对象
            mCylinder = new Cylinder();
            // 开启深度测试
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // 设置视口大小及位置
            GLES20.glViewport(0, 0, width, height);

            // 计算投影矩阵
            float left = -1.0f;
            float right = 1.0f;
            float bottom = -1.0f;
            float top = 1.0f;
            float near = 2.0f;
            float far = 10.0f;

            Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清除颜色和深度缓冲区
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            // 设置相机位置
            Matrix.setLookAtM(mViewMatrix, 0,
                    0.0f, 0.0f, -5.0f,
                    0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f);

            // 计算模型视图投影矩阵
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            // 对模型矩阵进行旋转变换
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.rotateM(mModelMatrix, 0, mAngle, 1.0f, 1.0f, 1.0f);

            // 将模型矩阵与投影视图矩阵相乘得到最终的模型视图投影矩阵
            Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);

            // 绘制圆柱
            mCylinder.draw(mMVPMatrix);

            // 更新旋转角度
            mAngle += 1.5f;
        }
    }

}
