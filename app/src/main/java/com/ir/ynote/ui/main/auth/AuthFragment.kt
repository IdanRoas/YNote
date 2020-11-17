package com.ir.ynote.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.ir.ynote.R
import com.ir.ynote.dto.User
import kotlinx.android.synthetic.main.auth_fragment.*



class AuthFragment : Fragment() {

    private var googleSignInClient: GoogleSignInClient? = null


    companion object {
        fun newInstance() = AuthFragment()
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initGoogleSignInClient()


        return inflater.inflate(R.layout.auth_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        google_sign_in_button.setOnClickListener{
            startActivityForResult(googleSignInClient?.signInIntent, RC_SIGN_IN)
        }
        }


    private fun initGoogleSignInClient() {
        var googleSignInOptions =  GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

         googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(ApiException::class.java)
                if (task.result != null) {
                    var googleTokenId = googleSignInAccount?.idToken
                    var googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
                    signInWithGoogleAuthCredential(googleAuthCredential);
                }
            } catch (e: ApiException) {
                    e.printStackTrace()           }
        }
    }

    private fun createNewUser( authenticatedUser: User) {
        viewModel.createUser(authenticatedUser);
        viewModel.createdUserLiveData?.observe(this, Observer<User> {user->
            if (user.isCreated!!) {
                Log.i("auth","user exist")
                goToMainScreen(user)
            }

        });
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.signInWithGoogle(googleAuthCredential)
        viewModel.authenticatedUser?.observe(this, Observer<User> {authenticatedUser->
            if (authenticatedUser.isNew!!) {
                createNewUser(authenticatedUser)
            } else {
                goToMainScreen(authenticatedUser)
            }
        })
    }

    private fun goToMainScreen(user:User) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, MainFragment.newInstance(user))?.commitNow()

    }


}



