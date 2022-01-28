# Easy Permissions
The easiest way to manage and request permissions in your Android application.

### Setup
```gradle
dependencies {
	implementation 'com.github.owenlejeune:EasyPermissions:TAG'
}
```

### Requesting a Permission
Simply add the permission to your application manifest

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

and then include the following code in your Activity or Fragment

```kotlin
class MainActivity: AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		//...
		// create the permission manager
		val permissionManager = EasyPermissions(this)
		// create a permissions requester
		val cameraRequester = permissionManager.forPermission(Permissions.Camera)
			.onDenied {
				// permission denied
			}
			.onPermanentlyDenied {
				// permission permanently denied
			}
			.onGranted {
				// permission granted
			}
			.onRationale {
				// rationale should be shown
			}
			.create(this)
		// request the permission!
		cameraRequester.request()
	}
}	
```

### Permissions
Easy Permissions includes a number of pre-definied permissions in the `Permissions` object, or you can create your own using the `Permission` object.

```kotlin
val calenderPermission = Permission(Manifest.permission.READ_CALENDAR)
```