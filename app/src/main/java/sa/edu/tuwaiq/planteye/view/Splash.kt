package sa.edu.tuwaiq.planteye.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.ActivitySplashBinding

private const val TAG = "Splash"

class Splash : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.planteye).into(binding.splashLogo)

        // Splash screen count timer
        object : CountDownTimer(2900, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.start()

    }
}