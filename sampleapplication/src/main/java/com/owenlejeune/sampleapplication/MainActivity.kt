package com.owenlejeune.sampleapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.owenlejeune.easypermissions.EasyPermissions
import com.owenlejeune.easypermissions.Permission
import com.owenlejeune.easypermissions.PermissionRequester
import com.owenlejeune.easypermissions.Permissions
import com.owenlejeune.sampleapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val requesters = HashMap<Permission, PermissionRequester>()

    private lateinit var binding: ActivityMainBinding

    private enum class State {
        GRANTED,
        DENIED,
        PERM_DENIED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val permissionManager = EasyPermissions(this)

        val cameraRequester = createCameraRequester(permissionManager)
        requesters[Permissions.Camera] = cameraRequester
        binding.cameraButton.setOnClickListener {
            cameraRequester.request()
        }

        val storageRequester = createStorageRequester(permissionManager)
        requesters[Permissions.ReadExternalStorage] = storageRequester
        binding.extStoButton.setOnClickListener {
            storageRequester.request()
        }

        val smsRequester = createSmsRequester(permissionManager)
        requesters[Permissions.SendSms] = smsRequester
        binding.smsButton.setOnClickListener {
            smsRequester.request()
        }

        val audioRequest = createAudioRequester(permissionManager)
        requesters[Permissions.RecordAudio] = audioRequest
        binding.audioButton.setOnClickListener {
            audioRequest.request()
        }

        binding.resetButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reset permissions")
                .setMessage("To reset permissions, go this applications Permissions settings and revoke permissions.")
                .setPositiveButton("Take me there") { _, _ ->
                    openAppSettings()
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
    }

    private fun createCameraRequester(manager: EasyPermissions): PermissionRequester {
        return createRequester(manager, Permissions.Camera, "Camera")
    }

    private fun createStorageRequester(manager: EasyPermissions): PermissionRequester {
        return createRequester(manager, Permissions.ReadExternalStorage, "Read External Storage")
    }

    private fun createSmsRequester(manager: EasyPermissions): PermissionRequester {
        return createRequester(manager, Permissions.SendSms, "Send SMS")
    }

    private fun createAudioRequester(manager: EasyPermissions): PermissionRequester {
        return createRequester(manager, Permissions.RecordAudio, "Record Audio")
    }

    private fun createRequester(manager: EasyPermissions, permission: Permission, name: String): PermissionRequester {
        return manager.forPermission(permission)
            .onDenied {
                Toast.makeText(this, "$name permission denied", Toast.LENGTH_SHORT).show()
                setButtonColor(permission, State.DENIED)
            }
            .onPermanentlyDenied {
                Toast.makeText(this, "$name permission permanently denied", Toast.LENGTH_SHORT).show()
                setButtonColor(permission, State.PERM_DENIED)
                openAppSettings()
            }
            .onGranted {
                Toast.makeText(this, "$name permission granted", Toast.LENGTH_SHORT).show()
                setButtonColor(permission, State.GRANTED)
            }
            .onRationale {
                AlertDialog.Builder(this)
                    .setTitle(name)
                    .setMessage("This is a sample rationale for $name permission")
                    .setPositiveButton("OK") { _, _ -> request(permission)}
                    .setNegativeButton("NO") { _, _ -> }
                    .show()
            }
            .create(this)
    }

    private fun request(permission: Permission) {
        requesters[permission]?.request()
    }

    private fun setButtonColor(permission: Permission, state: State) {
        val color = when(state) {
            State.GRANTED -> R.color.granted
            State.DENIED -> R.color.denied
            State.PERM_DENIED -> R.color.denied_perm
        }
        val button = when (permission) {
            Permissions.Camera -> binding.cameraButton
            Permissions.ReadExternalStorage -> binding.extStoButton
            Permissions.SendSms -> binding.smsButton
            Permissions.RecordAudio -> binding.audioButton
            else -> null
        }

        val bgColor = ContextCompat.getColor(this, color)
        button?.setBackgroundColor(bgColor)
    }

    private fun openAppSettings() {
        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(i)
    }
}