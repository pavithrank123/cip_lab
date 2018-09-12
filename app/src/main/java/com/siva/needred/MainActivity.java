package com.siva.needred;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;
    private DatabaseReference mUserReference;
    private FloatingActionButton floatingActionButton;

    private TabLayout mTabLayout;
    View background_view;
    public static Boolean isAdmin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background_view=findViewById(R.id.background_view);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);


        //FIRE BASE
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser == null){

            sendToStart();

        } else {

            Log.e("Value","Current value is ");
            Log.e("Email",mAuth.getCurrentUser().getEmail());
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
            mUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot childDataSnapshot :dataSnapshot.getChildren()){
                        //                       Log.e("The child 1 is ",childDataSnapshot.getKey());
                        //                       Log.e("The child 1 is ",childDataSnapshot.child("admin").getValue().toString());
                        if(mAuth.getCurrentUser().getEmail().equalsIgnoreCase(childDataSnapshot.child("admin").getValue().toString()))
                        {
                            isAdmin=true;
                            getSupportActionBar().setTitle("A D M I N");


                        }
                        else
                        {

                        }
                    }
                    //Tabs
                    mViewPager = (ViewPager) findViewById(R.id.main_tabpager);
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
                    mTabLayout.setupWithViewPager(mViewPager);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //mUserRef.child("online").setValue("true");

        }



        if(isAdmin==true)
        {
            getSupportActionBar().setTitle("A D M I N");
        }
        else
        {

            getSupportActionBar().setTitle("H E A L T H   C A R E");
        }

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }




    }
    public static boolean getAdminStatus()
    {
        return isAdmin;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser == null){

            sendToStart();

        } else {

            Log.e("Value","Current value is ");
            Log.e("Email",mAuth.getCurrentUser().getEmail());
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
            mUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot childDataSnapshot :dataSnapshot.getChildren()){
 //                       Log.e("The child 1 is ",childDataSnapshot.getKey());
 //                       Log.e("The child 1 is ",childDataSnapshot.child("admin").getValue().toString());
                        if(mAuth.getCurrentUser().getEmail().equalsIgnoreCase(childDataSnapshot.child("admin").getValue().toString())==true)
                        {
                            isAdmin=true;
                        }
                        else
                        {

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

           // mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId() == R.id.main_logout_btn){

            //mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }
        return true;
    }
}




