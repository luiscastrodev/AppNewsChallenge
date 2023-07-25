package br.com.example.criticaltechworks.challenge.ui

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import br.com.example.criticaltechworks.challenge.R
import br.com.example.criticaltechworks.challenge.util.BiometricHelper
import com.google.android.material.snackbar.Snackbar

class SpalshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        val biometricHelper = BiometricHelper.isBiometricAvailable(this)

        if (biometricHelper) {
            biometricAuthentication()
        } else {
            startApp()
            showMessage("No biometric")
        }
    }

    private fun startApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun biometricAuthentication() {
        val executor = ContextCompat.getMainExecutor(this)

        val bio =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startApp()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showMessage("Failed")
                    startApp()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showMessage("Canceled")
                    startApp()
                }
            })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Scanner")
            .setSubtitle("Finger Print ")
            .setDescription("Your Finger Print")
            .setNegativeButtonText("Cancel")
            .build()

        bio.authenticate(info)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}