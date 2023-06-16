package com.sedatkavak.bitcointicker.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sedatkavak.bitcointicker.R
import com.sedatkavak.bitcointicker.databinding.FragmentRegisterBinding
import com.sedatkavak.bitcointicker.ui.account.login.LoginFragment

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        mAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification()
                    } else {
                        Toast.makeText(
                            activity,
                            "Kayıt olurken bir hata oluştu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.loginButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        return view
    }

    private fun sendEmailVerification() {
        val user = mAuth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Doğrulama e-postası gönderildi!", Toast.LENGTH_LONG)
                        .show()
                    navigateToLoginFragment()
                } else {
                    val errorMessage = task.exception?.message
                    Toast.makeText(
                        activity,
                        "Doğrulama e-postası gönderilemedi!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun navigateToLoginFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val loginFragment = LoginFragment()

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, loginFragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
