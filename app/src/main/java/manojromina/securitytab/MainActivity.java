package manojromina.securitytab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {



    NotificationCompat.Builder notification;
    private static final int uniqueID = 1;

    //database reference
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ImageView mImage;
    public static final int RC_SIGN_IN = 1;

  //  private DatabaseReference mDatabaseReferenceUser;
   // private DatabaseReference mDatabaseLike;

    //for firebase authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private boolean mProcessLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent (MainActivity.this, MyService.class);
        startService(i);

        mImage = (ImageView)findViewById(R.id.image);



        //connecting to the firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference();

        // this method is use to get the image
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                System.out.print("Data is changed\n");
               // send_notification();




                /*
             //notification
                //this you have to try to put in background
                Intent intent = new Intent();
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);
                Notification notification = new Notification.Builder(MainActivity.this)
                        .setTicker("TickerTitle")
                        .setContentTitle("Security Alert")
                        .setContentText("Somebody is trying to Enter the lab")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentIntent(pIntent).getNotification();

                notification.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,notification);

          */



                String post_image = (String) dataSnapshot.child("image").getValue();





                Glide.with(MainActivity.this)
                        .load(post_image)
                        .into(mImage);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        //auth state listener is use to check whether the user has signed in or not

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //if the current user is null then
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent loginIntent = new Intent(MainActivity.this ,LoginActivity.class);
                    //if the user is not signed in then he can not go to back or any other activity without sign in
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(loginIntent);

                }

            }
        };




    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.action_logout)
        {
            logout();
        }


        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mFirebaseAuth.signOut();
    }



    @Override
    protected void onStart() {
        super.onStart();


        mFirebaseAuth.addAuthStateListener(mAuthStateListener);


    }


}




