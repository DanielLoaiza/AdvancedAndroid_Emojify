package com.example.android.emojify

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector

class Emojifier {
    companion object {

        private const val SMILING_TRESHOLD = 0.15
        private const val RIGHT_EYE_OPEN_TRESHOLD = 0.5
        private const val LEFT_EYE_OPEN_TRESHHOLD = 0.5

        fun detectFacesAndOverlayEmoji(context: Context, bitMap: Bitmap): Bitmap {
            val faceDetector = FaceDetector.Builder(context)
                    .setTrackingEnabled(false)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .build()

            val frame = Frame.Builder()
                    .setBitmap(bitMap)
                    .build()

            val faces = faceDetector.detect(frame)
            Log.e("number of faces", faces.size().toString())

            if (faces.size() == 0) {
                Toast.makeText(context, "No faces detected", Toast.LENGTH_SHORT)
                        .show()
            }

            var picture = bitMap
            for (i in 0 until faces.size()) {
                val face = faces.valueAt(i)
                val emoji = whichEmoji(face)
                var emojiBitmap: Bitmap?
                emojiBitmap = when (emoji) {
                    Emoji.SMILE -> BitmapFactory.decodeResource(context.resources, R.drawable.smile)
                    Emoji.LEFT_WINK -> BitmapFactory.decodeResource(context.resources, R.drawable.leftwink)
                    Emoji.LEFT_WINK_FROWN -> BitmapFactory.decodeResource(context.resources, R.drawable.leftwinkfrown)
                    Emoji.RIGHT_WINK -> BitmapFactory.decodeResource(context.resources, R.drawable.rightwink)
                    Emoji.RIGHT_WINK_FROWN -> BitmapFactory.decodeResource(context.resources, R.drawable.rightwinkfrown)
                    Emoji.CLOSED_EYE_FROWN -> BitmapFactory.decodeResource(context.resources, R.drawable.closed_frown)
                    Emoji.CLOSED_EYE_SMILE -> BitmapFactory.decodeResource(context.resources, R.drawable.closed_smile)
                    Emoji.FROWN -> BitmapFactory.decodeResource(context.resources, R.drawable.frown)
                }
                picture = BitmapUtils.addBitmapToFace(picture, emojiBitmap, face)
            }

            faceDetector.release()
            return picture
        }

        private fun whichEmoji(face: Face): Emoji {
            val smiling = face.isSmilingProbability > SMILING_TRESHOLD
            val leftEyeClosed = face.isLeftEyeOpenProbability < LEFT_EYE_OPEN_TRESHHOLD
            val rightEyeClosed = face.isRightEyeOpenProbability < RIGHT_EYE_OPEN_TRESHOLD

            return if (smiling) {
                when {
                    leftEyeClosed && !rightEyeClosed -> Emoji.LEFT_WINK
                    rightEyeClosed && !leftEyeClosed -> Emoji.RIGHT_WINK
                    leftEyeClosed -> Emoji.CLOSED_EYE_SMILE
                    else -> Emoji.SMILE
                }
            } else {
                when {
                    leftEyeClosed && !rightEyeClosed -> Emoji.LEFT_WINK_FROWN
                    rightEyeClosed && !leftEyeClosed -> Emoji.RIGHT_WINK_FROWN
                    leftEyeClosed -> Emoji.CLOSED_EYE_FROWN
                    else -> Emoji.FROWN
                }
            }
        }
    }
}