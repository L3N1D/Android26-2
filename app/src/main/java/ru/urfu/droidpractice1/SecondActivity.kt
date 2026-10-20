package ru.urfu.droidpractice1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import coil.load
import ru.urfu.droidpractice1.databinding.ActivitySecondBinding

class SecondActivity : ComponentActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate, savedInstanceState=${savedInstanceState != null}")
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.articleImage.load(getString(R.string.second_image_url)) {
            crossfade(true)
            placeholder(R.drawable.image_placeholder)
            error(R.drawable.image_placeholder)
        }

        // При пересоздании Switch сам восстановит своё состояние, поэтому из Intent читаем только при первом запуске
        if (savedInstanceState == null) {
            binding.readSwitch.isChecked = intent.getBooleanExtra(EXTRA_IS_READ, false)
        }
        publishReadState(binding.readSwitch.isChecked)
        binding.readSwitch.setOnCheckedChangeListener { _, isChecked -> publishReadState(isChecked) }
    }

    /** Результат выставляется сразу, поэтому до первой активити он дойдёт при любом способе выхода назад. */
    private fun publishReadState(isRead: Boolean) {
        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_IS_READ, isRead))
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy, isFinishing=$isFinishing")
    }

    /** Открывает вторую статью с текущим флагом «Прочитано» и возвращает новое значение флага. */
    class OpenArticle : ActivityResultContract<Boolean, Boolean?>() {
        override fun createIntent(context: Context, input: Boolean): Intent =
            Intent(context, SecondActivity::class.java).putExtra(EXTRA_IS_READ, input)

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? =
            if (resultCode == Activity.RESULT_OK && intent?.hasExtra(EXTRA_IS_READ) == true) {
                intent.getBooleanExtra(EXTRA_IS_READ, false)
            } else {
                null
            }
    }

    companion object {
        const val EXTRA_IS_READ = "ru.urfu.droidpractice1.extra.IS_READ"
        private const val TAG = "Lifecycle/Second"
    }
}
