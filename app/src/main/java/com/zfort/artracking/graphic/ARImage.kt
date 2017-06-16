package com.zfort.artracking.graphic

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import org.artoolkit.ar6.base.rendering.ARDrawable
import org.artoolkit.ar6.base.rendering.ShaderProgram
import org.artoolkit.ar6.base.rendering.util.RenderUtils
import java.io.InputStream
import java.nio.FloatBuffer

/**
 * Created by shein on 6/15/2017.
 */

class ARImage : ARDrawable {

    private var mVertexBuffer: FloatBuffer? = null
    private var shaderProgram: ShaderProgram? = null

    private fun draw(imagePath: String,
                     width: Float,
                     height: Float,
                     projectionMatrix: FloatArray,
                     modelViewMatrix: FloatArray) {
        draw(projectionMatrix, modelViewMatrix)

        


        shaderProgram?.render(mVertexBuffer, null, null)

    }

    private fun calcVertexBuffer(width: Float, height: Float): FloatBuffer {
        val halfWidth = width / 2.0f
        val halfHeight = height / 2.0f

        val vertices = floatArrayOf(-halfWidth, -halfHeight, 0f,
                halfWidth, -halfHeight, 0f,
                halfWidth, halfHeight, 0f,
                -halfWidth, halfHeight, 0f)

        return RenderUtils.buildFloatBuffer(vertices)
    }

    override fun draw(projectionMatrix: FloatArray, modelViewMatrix: FloatArray) {
        shaderProgram?.setProjectionMatrix(projectionMatrix)
        shaderProgram?.setModelViewMatrix(modelViewMatrix)


    }

    override fun setShaderProgram(program: ShaderProgram?) {
        shaderProgram = program
    }

    private fun loadTexture(inputStream: InputStream): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        val bitmap = BitmapFactory.decodeStream(inputStream)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()

        return texture[0]
    }
}
