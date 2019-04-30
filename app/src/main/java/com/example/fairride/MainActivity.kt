package com.example.fairride

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.client.Firebase

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Setup Firebase */
        Firebase.setAndroidContext(this);
        println("dupa");

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
