package com.mz.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cylinder {
    private final int mProgram;
    private FloatBuffer vertexBuffer;
    private final int vertexCount = 360 * 2 + 2;
    private final int vertexStride = 3 * 4; // 每个顶点四个字节
    private float[] vertices;

    public Cylinder() {
        // 初始化顶点数据
        initVertices();

        // 加载顶点着色器和片段着色器
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // 创建OpenGL ES程序并连接着色器
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器中的位置句柄
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用顶点属性数组
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 准备顶点坐标数据
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // 获取变换矩阵句柄
        int mvpMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 应用投影和视图变换
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // 绘制圆柱体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        // 禁用顶点属性数组
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private void initVertices() {
        vertices = new float[vertexCount * 3];

        float angle;
        float x;
        float y;

        for (int i = 0; i < vertexCount; i += 2) {
            angle = (float) (i / 2 * Math.PI / 180);
            x = (float) Math.cos(angle);
            y = (float) Math.sin(angle);

            vertices[i * 3] = x;
            vertices[i * 3 + 1] = y;
            vertices[i * 3 + 2] = -1.0f;

            vertices[(i + 1) * 3] = x;
            vertices[(i + 1) * 3 + 1] = y;
            vertices[(i + 1) * 3 + 2] = 1.0f;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);" +
                    "}";
}

