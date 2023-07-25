package com.mz.myapplication;

import android.opengl.GLES20;

public class MyGLRenderer {
    public static int loadShader(int type, String shaderCode) {
        // 创建着色器
        int shader = GLES20.glCreateShader(type);
        // 添加着色器源码并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

