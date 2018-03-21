//Tyler Carrico
//Project developed for practice with android sensors in Kotlin
package lab6.android.wku.edu.accelerometer

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast

class MainActivity : Activity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var color = false
    private var view: View? = null
    private var lastUpdate: Long = 0

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.textView)
        view!!.setBackgroundColor(Color.GREEN)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }

    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = event.timestamp
        if (accelationSquareRoot >= 2)
        //
        {
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
                    .show()
            if (color) {
                view!!.setBackgroundColor(Color.GREEN)
            } else {
                view!!.setBackgroundColor(Color.RED)
            }
            color = !color
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager!!.registerListener(this,
                sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // unregister listener
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}