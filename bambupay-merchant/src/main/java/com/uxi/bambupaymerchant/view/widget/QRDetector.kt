package com.uxi.bambupaymerchant.view.widget

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import java.io.ByteArrayOutputStream

class QRDetector(private val delegate: Detector<Barcode>,
                 private val detectorWidth: Int,
                 private val detectorHeight: Int) : Detector<Barcode>() {

    override fun detect(frame: Frame?): SparseArray<Barcode> {
        val width = frame?.metadata?.width!!
        val height = frame.metadata?.height!!
        val right = (width / 2) + detectorHeight / 2
        val left = width / 2 - detectorHeight / 2
        val bottom = height / 2 + detectorWidth / 2
        val top = height / 2 - detectorWidth / 2

        val yuvImage = YuvImage(frame.grayscaleImageData.array(), ImageFormat.NV21, width, height, null)
        val byteArrayOutputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(left, top, right, bottom), 100, byteArrayOutputStream)
        val jpegArray = byteArrayOutputStream.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)

        val croppedFrame = Frame.Builder()
                .setBitmap(bitmap)
                .setRotation(frame.metadata.rotation)
                .build()

        return delegate.detect(croppedFrame)
    }
}