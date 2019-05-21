package com.example.fairride

import android.content.Context
import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
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

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Setup Firebase */
        Firebase.setAndroidContext(this)
        var userName = FirebaseAuth.getInstance().currentUser!!.displayName
        Toast.makeText(this, userName, Toast.LENGTH_LONG).show()


        driverButton.setOnClickListener{
            val intent = Intent(this, DriverSettingsActivity::class.java)
            startActivity(intent)
        }

        passengerButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
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


}
