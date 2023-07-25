package com.example.designmode.opengl;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
    // 编译顶点着色器
    public static int compileVertexShader(String shaderCode){
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }
    // 编译片元着色器
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int compileShader(int type, String shaderCode) {
        // 创建一个新的着色器对象
        final int shaderObjectId = GLES20.glCreateShader(type);
        // 检查是否创建成功
        if (shaderObjectId == 0) {
            Log.e("Test","create shader error");
            return 0;
        }
        // 上传着色器源码
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        // 编译着色器源码
        GLES20.glCompileShader(shaderObjectId);

        // 获取编译状态
        final int[] compileStatus = new int[2];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus,0);
        // 取出日志信息
        Log.e("Test","compile: "+GLES20.glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {
            // 编译失败时删除该着色器对象
            GLES20.glDeleteShader(shaderObjectId);
            return 0;
        }
        // 返回着色器id
        return shaderObjectId;
    }


    public static int linkProgram(int vertexShaderId,int fragmentShaderId) {
        // 创建一个 OpenGL 程序对象
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            Log.e("Test","create programe error");
            return 0;
        }

        // 把顶点着色器和片段着色器附加到程序对象上
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);

        // 链接程序
        GLES20.glLinkProgram(programObjectId);

        // 验证链接的状态
        final int[] linkStatus = new int[2];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus,0);
        // 打印链接信息
        Log.e("Test","link programe: "+GLES20.glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            Log.e("Test","lint programe error");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[2];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus,0);
        Log.e("Test","validate programe: "+GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}