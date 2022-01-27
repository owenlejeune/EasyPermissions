package com.owenlejeune.easypermissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * A collection of [Permission] implementations for easy access. This is NOT an exhaustive list,
 * permissions not listed here can be created using the [Permission] class.
 */
object Permissions {
    object Camera : Permission(Manifest.permission.CAMERA)

    object WriteExternalStorage : Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    object ReadExternalStorage : Permission(Manifest.permission.READ_EXTERNAL_STORAGE)

    object FineLocation : Permission(Manifest.permission.ACCESS_FINE_LOCATION)
    object CoarseLocation : Permission(Manifest.permission.ACCESS_COARSE_LOCATION)
    @RequiresApi(Build.VERSION_CODES.Q)
    object BackgroundLocation : Permission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    object Bluetooth : Permission(Manifest.permission.BLUETOOTH)
    @RequiresApi(Build.VERSION_CODES.S)
    object BluetoothConnect : Permission(Manifest.permission.BLUETOOTH_CONNECT)
    @RequiresApi(Build.VERSION_CODES.S)
    object BluetoothScan : Permission(Manifest.permission.BLUETOOTH_SCAN)

    object ReceiveSms : Permission(Manifest.permission.RECEIVE_SMS)
    object ReceiveMms : Permission(Manifest.permission.RECEIVE_MMS)
    object SendSms : Permission(Manifest.permission.SEND_SMS)

    object RecordAudio : Permission(Manifest.permission.RECORD_AUDIO)

    @RequiresApi(Build.VERSION_CODES.P)
    object UseBiometric : Permission(Manifest.permission.USE_BIOMETRIC)

    object WriteContacts : Permission(Manifest.permission.WRITE_CONTACTS)
    object ReadContacts : Permission(Manifest.permission.READ_CONTACTS)
}