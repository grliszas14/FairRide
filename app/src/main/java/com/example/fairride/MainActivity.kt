package com.example.fairride

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.client.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Setup Firebase */
        Firebase.setAndroidContext(this)
        println("dupa")

        driverButton.setOnClickListener{
            val intent = Intent(this, DriverSettingsActivity::class.java)
            startActivity(intent)
        }

        passengerButton.setOnClickListener {
            val intent = Intent(this, PassengerActivity::class.java)
            startActivity(intent)
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
}
