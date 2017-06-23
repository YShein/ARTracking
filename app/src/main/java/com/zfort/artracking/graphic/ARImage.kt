package com.zfort.artracking.graphic

import android.opengl.GLES20
import com.zfort.artracking.model.Marker
import org.artoolkit.ar6.base.rendering.ShaderProgram
import org.artoolkit.ar6.base.rendering.util.RenderUtils
import java.nio.FloatBuffer

/**
 * Created by shein on 6/15/2017.
 * Not necessary implement ARDrawable
 */

class ARImage(private val shaderProgram: ShaderProgram) {

    fun draw(marker: Marker,
             projectionMatrix: FloatArray,
             modelViewMatrix: FloatArray) {
        shaderProgram.setProjectionMatrix(projectionMatrix)
        shaderProgram.setModelViewMatrix(modelViewMatrix)
        shaderProgram.render(calcVertexBuffer(marker.maskImageWidth, marker.maskImageHeight), null, null)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, marker.textureId)
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

}
