package com.zfort.artracking

import android.content.Context
import org.artoolkit.ar6.base.rendering.ARRenderer

/**
 * Created by shein on 6/15/2017.
 */
class ARTackingRenderer(private val context: Context) : ARRenderer() {

    override fun configureARScene() = false
}