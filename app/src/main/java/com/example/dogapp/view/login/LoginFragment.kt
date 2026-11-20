package com.example.dogapp.view.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.dogapp.R
class LoginFragment : Fragment (R.layout.fragment_login){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lottieFingerprint = view.findViewById<LottieAnimationView>(R.id.lottieFingerprint)

        lottieFingerprint.setOnClickListener {
            // example: startBiometricAuthentication()
        }
    }


}