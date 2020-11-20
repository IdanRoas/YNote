package com.ir.ynote.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.ir.ynote.R
import com.ir.ynote.dto.User
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), FirebaseAuth.AuthStateListener {

    private lateinit var user: User

    companion object {
        fun newInstance(user: User) = MainFragment().apply {
            this.user=user
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       replaceFragment(HomeFragment.newInstance())
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_btn -> {
                    replaceFragment(HomeFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.chat_btn -> {
                    replaceFragment(ChatFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.notification_btn -> {
                    replaceFragment(NotificationFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                else-> {
                    replaceFragment(HomeFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
            false
        bottom_menu.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)
    }



    private fun replaceFragment(fragment:Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_container, fragment)?.commit()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        TODO("Not yet implemented")
    }

}