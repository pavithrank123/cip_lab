package com.siva.needred;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private View mMainView;
    private RecyclerView mHelpList;
    private DatabaseReference mUsersDatabase,someRef;
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    public FullscreenActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        //init
        mHelpList = (RecyclerView)findViewById(R.id.hospital_recyclerView);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mHelpList.setLayoutManager(linearVertical);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mHelpList.getContext(),
                linearVertical.getOrientation()
        );

        mHelpList.addItemDecoration(mDividerItemDecoration);

    }

    @Override
    public void onStart() {
        super.onStart();

        Intent myIntent = getIntent();
        String hosp_id = myIntent.getStringExtra("hosp_id");
        final String url = myIntent.getStringExtra("url");
        Log.e("hosp",hosp_id);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("emergencies").child(hosp_id);
        //final PaymentOrder order = new PaymentOrder();


        final FirebaseRecyclerAdapter<Help, NeedHelpFragment.HelpViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Help, NeedHelpFragment.HelpViewHolder>(

                Help.class,
                R.layout.help_single_layout,
                NeedHelpFragment.HelpViewHolder.class,
                mUsersDatabase) {
            @Override

            protected void populateViewHolder(final NeedHelpFragment.HelpViewHolder helpViewHolder, final Help help, int i) {
                helpViewHolder.setDate(help.getDate());
                mHelpList.setVisibility(View.GONE);
                final String list_user_id = getRef(i).getKey();
                Log.e("Checking Hospital ==",getRef(i).child("hospital").toString());
                // if(getRef(i).child("hospital").toString().equalsIgnoreCase(mCurrent_user_id.toString())) {

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("blood_group").getValue().toString();
                        final String blood = dataSnapshot.child("name").getValue().toString();
                        final String phone = dataSnapshot.child("mobile").getValue().toString();
                        final String address = dataSnapshot.child("amount").getValue().toString();
                        String hospital = dataSnapshot.child("hospital").getValue().toString();
                        //if (hospital.equalsIgnoreCase(mCurrent_user_id.toString())) {
                            helpViewHolder.setName(userName);
                            helpViewHolder.setBlood(blood);
                            helpViewHolder.setAddress(address+"Rs");
                            helpViewHolder.setPhone(phone);
                            mHelpList.setVisibility(View.VISIBLE);
                            helpViewHolder.mView.setVisibility(View.VISIBLE);
                            helpViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CharSequence options[] = new CharSequence[]{"Donate", "Call", "Directions","Screenshot"};

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);

                                    builder.setTitle("Select Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            //Click Event for each item.
                                            if (i == 0) {
                                                   Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                           Uri.parse("https://imjo.in/Ua2ERm"));
                                                    startActivity(intent);
                                            }

                                            if (i == 1) {

                                                String uri = phone;


                                                if (ActivityCompat.checkSelfPermission(FullscreenActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + uri));
                                                    // callIntent.setData(Uri.parse("tel:"+uri));
                                                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    FullscreenActivity.this.startActivity(callIntent);

                                                }


                                            }
                                            if (i == 2) {
                                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                            Uri.parse(url));
                                                    startActivity(intent);
                                            }
                                            if(i == 3){
                                                Intent profileIntent = new Intent(FullscreenActivity.this,ScreenShot.class);
                                                profileIntent.putExtra("name",userName );
                                                profileIntent.putExtra("blood", blood);
//                                                    profileIntent.putExtra("hospital", "saas");
                                                profileIntent.putExtra("mobile", phone);
                                                profileIntent.putExtra("req", address+"Rs");
                                                startActivity(profileIntent);

                                            }

                                        }
                                    });
                                    builder.show();
                                }
                            });
        //                } else {
                            //helpViewHolder.mView.setOnCheckChangedListener(null);

      //                  }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                //       }
            }
        };
        mHelpList.setAdapter(friendsRecyclerViewAdapter);
    }

    // viewholder class..



    public static class HelpViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public HelpViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBlood(String blood){
            TextView userStatusView = (TextView) mView.findViewById(R.id.help_blood);
            userStatusView.setText(blood.toUpperCase());
        }
        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.help_name);
            userNameView.setText(name.toUpperCase());
        }
        public void setPhone(String phone){

            TextView userNameView = (TextView) mView.findViewById(R.id.help_mobile);
            userNameView.setText(phone);
        }
        public void setAddress(String address) {
            TextView userNameView = (TextView) mView.findViewById(R.id.help_place);
            address.toUpperCase();
            userNameView.setText(address.toUpperCase());
        }
        public void setDate(String date){


        }

    }

}
