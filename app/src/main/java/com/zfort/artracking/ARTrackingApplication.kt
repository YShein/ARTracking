
package com.zfort.artracking

import android.app.Application
import android.content.Context

import org.artoolkit.ar6.base.assets.AssetHelper

class ARTrackingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        this.initializeInstance()
    }

    // Here we do one-off initialisation which should apply to all activities
    // in the application.
    private fun initializeInstance() {

        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the modules build.gradle file.
        val assetHelper = AssetHelper(assets)
        assetHelper.cacheAssetFolder(this, "Data")
        assetHelper.cacheAssetFolder(this, "cparam_cache")
    }
}
