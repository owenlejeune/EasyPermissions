package com.owenlejeune.easypermissions

/**
 * Utility class for requesting a given permission represented by a [PermissionInfo] instance
 */
class PermissionRequester internal constructor(private val info: PermissionInfo) {

    /**
     * Request the permission bound to this requester
     */
    fun request() {
        info.permissionManager.requestPermission(info)
    }

}