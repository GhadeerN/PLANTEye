package sa.edu.tuwaiq.planteye.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*

private const val TAG = "ImagePicker"
val IMAGE_PICKER = 0
val REQUEST_CODE_CP = 1
object ImagePicker {
    // This function will open an image selector using Matisse library
    fun showImagePicker(context: Context, fragment: Fragment) {
        checkCameraStoragePermission(context)

        Matisse.from(fragment)
            .choose(MimeType.ofImage(), false)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "sa.edu.tuwaiq.planteye"))
            .forResult(11)
    }

    // Since the Plant.id Api only accept base64 string for an image, this function will decode the image file to accomplish that
    fun base64Encoder(file: File): String {
        val bytes = file.readBytes()
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
    }

    // This function is to check the camera and storage permissions
    // if not granted -> ask for the permissions
    private fun checkCameraStoragePermission(context: Context) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "PERMISSION_DENIED")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_CP
            )
        }
    }

    // Saved Broken Image
    @RequiresApi(Build.VERSION_CODES.O)
     fun encoder3(path: String): String {
        val imagefile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imagefile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()

        return Base64.getEncoder().encodeToString(b)
    }
}