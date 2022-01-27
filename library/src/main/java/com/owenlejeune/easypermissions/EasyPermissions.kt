package com.owenlejeune.easypermissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

/**
 * Management class for requesting and monitoring permissions and their granted state
 */
class EasyPermissions(private var activity: FragmentActivity?): LifecycleObserver {

    constructor(fragment: Fragment?): this(fragment?.requireActivity())

    private val requestLaunchers = mutableMapOf<Permission, ActivityResultLauncher<String>>()
    private val observers = mutableMapOf<Permission, PermissionInfo>()

    /**
     * Stores whether Rationale has been shown for specific [Permission] of permission.
     * Should be reset after every access granting
     */
    private val shownRationale = mutableMapOf<Permission, Boolean>()

    private val permissionLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            unbind()
        }
    }

    init {
        activity?.lifecycle?.addObserver(permissionLifecycleObserver)
    }

    internal fun unbind() {
        val previousValues = requestLaunchers.toMap()
        previousValues.values.forEach { it.unregister() }
        requestLaunchers.clear()
        observers.clear()
        activity = null
    }

    private fun processPermissionReply(type: Permission, isGranted: Boolean) {
        observers[type]?.let { observer ->
            if (isGranted) {
                observer.granted?.invoke()
            } else {
                // by indicating whether rationale should be shown we can assume that user
                // clicked "Deny&don't ask again"
                activity?.let {
                    val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        type.name
                    )
                    if (shouldShowRationale)
                        observer.denied?.invoke()
                    else
                        observer.permanentlyDenied?.invoke()
                }
            }
            // in any case it should be reset because user made a decision
            shownRationale[type] = false
        }
    }

    /**
     * Check if a given [Permission] is granted
     * 
     * @param [Permission] to check
     * 
     * @return true if the permission is granted, false otherwise
     */
    fun hasPermission(permission: Permission): Boolean {
        return activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                permission.name
            ) == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    internal fun requestPermission(info: PermissionInfo) {
        observers[info.type] = info

        if (hasPermission(info.type)) {
            info.granted?.invoke()
        } else {
            activity?.let { activity ->
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    info.type.name
                ) && !(shownRationale[info.type] ?: false)

                if (shouldShowRationale) {
                    shownRationale[info.type] = true
                    info.rationale?.invoke()
                } else {
                    requestLaunchers[info.type]?.launch(info.type.name)
                }
            }
        }
    }

    /**
     * Generate a [PermissionInfo] instance for a given [Permission]
     * 
     * @param [Permission] to create info for
     * 
     * @return the created [PermissionInfo] instance
     */
    fun forPermission(type: Permission): PermissionInfo {
        return PermissionInfo(type, this)
    }

    internal fun subscribe(info: PermissionInfo) {
        activity?.let {
            val launcher =
                it.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    processPermissionReply(info.type, isGranted)
                }
            requestLaunchers[info.type] = launcher
            observers[info.type] = info
        }
    }

}