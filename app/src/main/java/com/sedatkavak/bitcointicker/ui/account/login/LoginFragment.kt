package com.sedatkavak.bitcointicker.ui.account.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sedatkavak.bitcointicker.R
import com.sedatkavak.bitcointicker.databinding.FragmentLoginBinding
import com.sedatkavak.bitcointicker.ui.MainActivity
import com.sedatkavak.bitcointicker.ui.home.HomeFragment

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private var createAccountButtonClickListener: OnCreateAccountButtonClickListener? = null
    private var menuButtonClickListener: OnMenuButtonClickListener? = null

    interface OnCreateAccountButtonClickListener {
        fun onCreateAccountButtonClicked()
    }

    interface OnMenuButtonClickListener {
        fun onMenuButtonClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCreateAccountButtonClickListener) {
            createAccountButtonClickListener = context
        } else {
            throw ClassCastException("$context must implement OnCreateAccountButtonClickListener")
        }

        if (context is OnMenuButtonClickListener) {
            menuButtonClickListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        mAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            loginUser()
        }
        binding.createAccountButton.setOnClickListener {
            createAccountButtonClickListener?.onCreateAccountButtonClicked()
        }
        return view
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkEmailVerification()
                } else {
                    Toast.makeText(activity, "Giriş Yapılamadı!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun checkEmailVerification() {
        val user = mAuth.currentUser
        if (user != null && user.isEmailVerified) {
            Toast.makeText(activity, "Giriş Başarılı!", Toast.LENGTH_LONG).show()
            navigateToMainActivity()
        } else {
            Toast.makeText(
                activity,
                "E-posta adresiniz henüz doğrulanmamış!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
