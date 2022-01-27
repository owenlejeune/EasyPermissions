package com.owenlejeune.easypermissions

import androidx.lifecycle.*

internal class LifecycleAwareInfo (
    private val info: PermissionInfo,
    lifecycleOwner: LifecycleOwner
) : PermissionInfo(info), DefaultLifecycleObserver {

    var lifecycleOwner: LifecycleOwner? = null

    init {
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        info.permissionManager.unbind()
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }

}
