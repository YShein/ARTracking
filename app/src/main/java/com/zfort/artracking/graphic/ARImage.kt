package com.zfort.artracking.graphic

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import org.artoolkit.ar6.base.rendering.ARDrawable
import org.artoolkit.ar6.base.rendering.ShaderProgram
import org.artoolkit.ar6.base.rendering.util.RenderUtils
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.FloatBuffer

/**
 * Created by shein on 6/15/2017.
 */

class ARImage : ARDrawable{

    private var mVertexBuffer: FloatBuffer? = null
    private var shaderProgram: ShaderProgram? = null

    private fun setArrays() {
        val x = 0f
        val y = 0f
        val z = 0f
        val hs = size / 2.0f

        /*
        In the marker coordinate system z points from the marker up. x goes to the right and y to the top
         */
        val vertices = floatArrayOf(x - hs, y - hs, 0f,
                x + hs, y - hs, 0f,
                x + hs, y + hs, 0f,
                x - hs, y + hs, 0f)

        mVertexBuffer = RenderUtils.buildFloatBuffer(vertices)
    }

    override fun draw(projectionMatrix: FloatArray, modelViewMatrix: FloatArray) {
        shaderProgram?.setProjectionMatrix(projectionMatrix)
        shaderProgram?.setModelViewMatrix(modelViewMatrix)



        this.setArrays()
        shaderProgram?.render(mVertexBuffer, null, null)


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
