package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.userexperior.UserExperior

class MainActivity : AppCompatActivity() {

    lateinit var discoLayout: FrameLayout
    lateinit var sensorManager: SensorManager
    lateinit var accelSensor: Sensor
    lateinit var sensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserExperior.startRecording(applicationContext, "67a51de0-8274-40e6-81ed-a27a70e4d3f8");
        
        discoLayout = findViewById(R.id.discoLayout) // Allot ID to variable
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            Log.d("Sensor", "${sensor.name} -> ${sensor.vendor}")
        }

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {

                p0?.values?.let { axis ->
                    val colorRGB = accelToColor(axis[0], axis[1], axis[2])
                    discoLayout.setBackgroundColor(colorRGB)
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                Log.d("SENSOR", " Accuracy Changed")
            }

        }

    }


    private fun accelToColor(aX: Float, aY: Float, aZ: Float): Int {

        // Range of Axis lies from -10 to +10
        // Range of Color Code lies from 0 - 255
        // Division of Range will give result between -1 to 1

        val R = (((aX + 12) / 24) * 255).toInt()

        val G = (((aY + 12) / 24) * 255).toInt()

        val B = (((aZ + 12) / 24) * 255).toInt()

        return Color.rgb(R, G, B)

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, accelSensor, 1000)
    }

    override fun onStop() {
        sensorManager.unregisterListener(sensorEventListener)

        super.onStop()
    }
}



