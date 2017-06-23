package com.zfort.artracking

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.zfort.artracking.databinding.ActivityMainBinding
import org.artoolkit.ar6.base.ARActivity


class CameraActivity : ARActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun supplyRenderer() = ARTackingRenderer(emptyList())

    override fun supplyFrameLayout() = binding.flMain ?: error("Cant find FrameLayout")
}
