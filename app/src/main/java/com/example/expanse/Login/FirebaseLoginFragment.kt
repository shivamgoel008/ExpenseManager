package com.example.expanse.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expanse.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_firebase_login.*


class FirebaseLoginFragment : Fragment() {

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (this.auth.currentUser != null) {
            FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment()
        }


//        configuring google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        UpdateUI(currentUser)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_login, container, false)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
//                    findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())

                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {
                println("signInActivity ${exception.toString()}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }


    private fun UpdateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notRegistered.setOnClickListener {
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToFirebaseRegisterFragment4())
        }

//        for google signin
        sign_in_button.setOnClickListener {
            progressBar.visibility=View.VISIBLE
            signIn()
        }
        login.setOnClickListener {
            firebaseEmailLogin.error = null
            firebasePasswordLogin.error = null

            val email = firebaseEmailLogin.editText?.text.toString()
            val pass = firebasePasswordLogin.editText?.text.toString()

            if (validateInput(email, pass)) {
                progressBar.visibility = View.VISIBLE

                // authenticating user
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    progressBar.visibility = View.INVISIBLE

                    if (task.isSuccessful) {
                        findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
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
            firebaseEmailLogin.error = "Please enter an email address"
            valid = false
        } else if (pass.length < 8) {
            firebasePasswordLogin.error = "Password show 8 character or more"
            valid = false
        }

        return valid
    }


}