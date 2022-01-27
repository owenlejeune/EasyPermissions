package com.owenlejeune.easypermissions

import androidx.lifecycle.LifecycleOwner

/**
 * This class wraps a [Permission] with methods to attach callbacks for various permission request results
 */
open class PermissionInfo(val type: Permission, val permissionManager: EasyPermissions) {

    constructor(info: PermissionInfo): this(info.type, info.permissionManager) {
        granted = info.granted
        denied = info.denied
        rationale = info.rationale
        permanentlyDenied = info.permanentlyDenied
    }

    var granted: PermissionAction? = null
        private set
    var denied: PermissionAction? = null
        private set
    var permanentlyDenied: PermissionAction? = null
        private set
    var rationale: PermissionAction? = null
        private set

    /**
     * Use this to pass all callbacks at once
     */
    fun withCallback(
        onGranted: PermissionAction? = null,
        onDenied: PermissionAction? = null,
        onPermanentlyDenied: PermissionAction? = null,
        onRationale: PermissionAction? = null
    ): PermissionInfo {
        granted = onGranted
        denied = onDenied
        rationale = onRationale
        permanentlyDenied = onPermanentlyDenied
        return this
    }

    /**
     * The [PermissionAction] to be performed when the permission is granted
     *
     * @return this [PermissionInfo] instance
     */
    fun onGranted(action: PermissionAction): PermissionInfo {
        return this.apply {
            granted = action
        }
    }

    /**
     * The [PermissionAction] to be performed when the permission is denied
     *
     * @return this [PermissionInfo] instance
     */
    fun onDenied(action: PermissionAction): PermissionInfo {
        return this.apply {
            denied = action
            if (permanentlyDenied == null) {
                permanentlyDenied = denied
            }
        }
    }

    /**
     * The [PermissionAction] to be performed when the permission is permanently denied
     *
     * @return this [PermissionInfo] instance
     */
    fun onPermanentlyDenied(action: PermissionAction): PermissionInfo {
        return this.apply {
            permanentlyDenied = action
        }
    }

    /**
     * The [PermissionAction] to be performed when a rationale needs to be shown
     *
     * @return this [PermissionInfo] instance
     */
    fun onRationale(action: PermissionAction): PermissionInfo {
        return this.apply {
            rationale = action
        }
    }

    /**
     * Removes all callbacks
     */
    fun clearCallbacks() {
        granted = null
        denied = null
        rationale = null
        permanentlyDenied = null
    }

    /**
     * Create a [PermissionRequester] for the permission represented by this instance
     *
     * @param lifecycleOwner The lifecycle owner observing changes to this permission
     *
     * @return the created [PermissionRequester]
     */
    fun create(lifecycleOwner: LifecycleOwner? = null): PermissionRequester {
        val info = if (lifecycleOwner == null) {
            this
        } else {
            LifecycleAwareInfo(this, lifecycleOwner)
        }

        return PermissionRequester(info).also { info.permissionManager.subscribe(info) }
    }

}