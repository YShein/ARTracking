package com.zfort.artracking

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import com.zfort.artracking.graphic.ARImage
import com.zfort.artracking.model.Marker
import org.artoolkit.ar6.base.ARToolKit
import org.artoolkit.ar6.base.NativeInterface
import org.artoolkit.ar6.base.rendering.ARRenderer
import java.io.File
import java.io.InputStream
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by shein on 6/15/2017.
 */
class ARTackingRenderer(private val markers: List<Marker>) : ARRenderer() {

    private lateinit var imageRender: ARImage

    override fun configureARScene(): Boolean {
        markers.forEach {
            it.id = ARToolKit.getInstance().addMarker("2d;${it.imagePath};${it.maskImageHeight}")
            if (it.id > 0) {
                it.textureId = loadTexture(it.maskImagePath)
            }
        }
        NativeInterface.arwSetTrackerOptionInt(NativeInterface.ARW_TRACKER_OPTION_2D_MAX_IMAGES, markers.filter { it.id > 0 }.size)
        return true
    }

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(unused, config)
        imageRender = ARImage(shaderProgram)
    }

    override fun draw() {
        super.draw()

        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glFrontFace(GLES20.GL_CCW)

        val projectionMatrix = ARToolKit.getInstance().projectionMatrix
        markers.filter { ARToolKit.getInstance().queryMarkerVisible(it.id) }.forEach {
            val modelViewMatrix = ARToolKit.getInstance().queryMarkerTransformation(it.id)
            imageRender.draw(it, projectionMatrix, modelViewMatrix)
        }
    }

    private fun loadTexture(file: String) = loadTexture(File(file).inputStream())

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