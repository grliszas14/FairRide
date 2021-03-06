package com.example.fairride

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 1000

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Setup Firebase */
        Firebase.setAndroidContext(this)
        var userName = FirebaseAuth.getInstance().currentUser!!.displayName
        //Toast.makeText(this, userName, Toast.LENGTH_LONG).show()
        getPermissionForLocation()

        driverButton.setOnClickListener{
            val intent = Intent(this, DriverSettingsActivity::class.java)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        passengerButton.setOnClickListener {
            val intent = Intent(this, PassengerActivity::class.java)
            startActivity(intent)
        }

        sign_out_button.setOnClickListener {
            signOut()
        }


        /* Read & write to Firebase Database */
        // Firebase myFirebaseRef = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com/");

        // write
        //myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");

        // read
        // myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
        //  @Override
        //  public void onDataChange(DataSnapshot snapshot) {
        //    System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
        //  }
        //  @Override public void onCancelled(FirebaseError error) { }
        //});

    }
    private fun signOut() {

        //startActivity(SignInActivity.getLaunchIntent(this).putExtra("ifSignOut", true))
        //FirebaseAuth.getInstance().signOut()
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            Toast.makeText(this, "wylogowano", Toast.LENGTH_LONG).show()
            startActivity(SignInActivity.getLaunchIntent(this))
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    fun getPermissionForLocation () {
        if(ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }

}
