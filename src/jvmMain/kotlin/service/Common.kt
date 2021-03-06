package service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * @author Markitanov Vadim
 * @since 24.04.2022
 */
fun generateContactCredentials(): Boolean {
    return true
}

fun requestDefaultUserId(): Long? {
    return 1L
//    return null
}

private fun needPrintDraw(): Boolean = false

fun printDraw() {
    if (needPrintDraw()) {
        val funName = Thread.currentThread().stackTrace[2].methodName
        println("${funName.uppercase()} DRAW.")
    }
}

@Composable
fun ImageChooser(imageLoaded: (ByteArray) -> Unit) {
    val imageFullName = remember { mutableStateOf<String?>(null) }
    if (imageFullName.value != null) {
        println("Image loaded: ${imageFullName.value}")
//        showImageChooser.value = false
        val file = imageFullName.value?.let { File(it) }
        if (file != null) {
            if (file.exists()) {
//                val image = mutableStateOf<ImageBitmap?>(null)
                try {
                    imageLoaded.invoke(file.readBytes())
//                    val imageBytes = file.readBytes()
//                    UsersRepo.loadImage(imageBytes)
//                    image.value = Image.makeFromEncoded(imageBytes).toComposeImageBitmap()
                } catch (e: Exception) {
                    println("Error load image: ${e.localizedMessage}")
                }

//                if (image.value != null) {
//                    Image(
//                        bitmap = image.value!!,
//                        contentDescription = "Test"
//                    )
//                }
            }
        }
    }

    AwtWindow(
        true,
        create = {
            val parent: Frame? = null
            object : FileDialog(parent, "Choose a image", LOAD) {
                override fun setVisible(b: Boolean) {
                    super.setVisible(b)
                    if (b) {
                        println("Call close request.")
                        imageFullName.value = directory + file
                    }
                }
            }
        },
        dispose = FileDialog::dispose
    )
}