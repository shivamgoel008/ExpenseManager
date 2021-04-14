package com.example.expanse.Login

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expanse.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_firebase_register.*


class FirebaseRegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        UpdateUI(currentUser)
    }

    private fun UpdateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            findNavController().navigate(FirebaseRegisterFragmentDirections.actionFirebaseRegisterFragmentToLoginFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AlreadyRegistered.setOnClickListener {
            findNavController().navigate(FirebaseRegisterFragmentDirections.actionFirebaseRegisterFragmentToFirebaseLoginFragment2())
        }

        register.setOnClickListener {
            firebaseEmailRegister.error = null
            firebasePasswordRegister.error = null

            val email = firebaseEmailRegister.editText?.text.toString()
            val pass = firebasePasswordRegister.editText?.text.toString()

            if (validateInput(email, pass)) {
                progressBar2.visibility = View.VISIBLE

                // authenticating user
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    progressBar2.visibility = View.INVISIBLE

                    if (task.isSuccessful) {
                        findNavController().navigate(FirebaseRegisterFragmentDirections.actionFirebaseRegisterFragmentToLoginFragment())
                    } else {
                        val toast = Toast.makeText(
                            requireActivity(),
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        )
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                }

            }
        }
    }

    private fun validateInput(email: String, pass: String): Boolean {
        var valid = true
        if (email.isBlank()) {
            firebaseEmailRegister.error = "Please enter an email address"
            valid = false
        } else if (pass.length < 8) {
            firebasePasswordRegister.error = "Password show 8 character or more"
            valid = false
        }

        return valid
    }

}