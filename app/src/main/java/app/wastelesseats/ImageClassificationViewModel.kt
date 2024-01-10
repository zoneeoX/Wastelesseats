package app.wastelesseats

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import app.wastelesseats.ml.ConvertedModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassificationViewModel(application: Application) : AndroidViewModel(application) {
    private val imageSize = 224
    private val classes = arrayOf("fresh apple", "fresh banana", "fresh cucumber", "fresh okra",
        "fresh oranges", "fresh potato", "fresh tomato", "rotten apple", "rotten banana", "rotten cucumber", "rotten okra",
        "rotten oranges", "rotten potato", "rotten tomato")

    var selectedImage by mutableStateOf<Bitmap?>(null)

    private var highestPercentage by mutableStateOf<String?>(null)
    var predictionResult by mutableStateOf<String?>(null)
    private var percentages by mutableStateOf<List<Float>>(emptyList())

    private val contentResolver: ContentResolver by lazy {
        getApplication<Application>().contentResolver
    }

    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun classifyImage() {
        val image = selectedImage?.let { Bitmap.createScaledBitmap(it, imageSize, imageSize, false) }
        val model = ConvertedModel.newInstance(getApplication())
        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        image?.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0
        // Iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value ushr 16 and 0xFF) * (1f / 255f)))
                byteBuffer.putFloat(((value ushr 8 and 0xFF) * (1f / 255f)))
                byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
            }
        }
        inputFeature0.loadBuffer(byteBuffer)
        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        // Update the confidence percentages
        val confidences = outputFeature0.floatArray
        percentages = confidences.map { (it * 100) }
        highestPercentage = (percentages.maxOrNull() ?: 0.0).toString()
        // Find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        predictionResult = classes[maxPos]
        model.close()
    }
}