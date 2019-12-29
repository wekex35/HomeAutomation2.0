package com.digiads.akshhomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

    /**
     * Demonstrate Firebase Authentication using a Facebook access token.
     */


    public class FacebookLogin extends AppCompatActivity {

        private static final String TAG = "FacebookLogin";

        private TextView mStatusTextView;
        private TextView mDetailTextView;

        // [START declare_auth]
        private FirebaseAuth mAuth;
        // [END declare_auth]

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_facebook);

            // Views
            mStatusTextView = findViewById(R.id.status);
            mDetailTextView = findViewById(R.id.detail);
           //findViewById(R.id.buttonFacebookSignout).setOnClickListener(this);

            // [START initialize_auth]
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]

            // [START initialize_fblogin]
            // Initialize Facebook Login button
          /*  mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }
            });*/
            // [END initialize_fblogin]
        }

     /*   // [START on_start_check_user]
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
        // [END on_start_check_user]

        // [START on_activity_result]
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        // [END on_activity_result]

        // [START auth_with_facebook]
        private void handleFacebookAccessToken(AccessToken token) {
            Log.d(TAG, "handleFacebookAccessToken:" + token);
            // [START_EXCLUDE silent]
            showProgressDialog();
            // [END_EXCLUDE]

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
        // [END auth_with_facebook]

        public void signOut() {
            mAuth.signOut();
            LoginManager.getInstance().logOut();

            updateUI(null);
        }

        private void updateUI(FirebaseUser user) {
            hideProgressDialog();
            if (user != null) {
                mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
                mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

                findViewById(R.id.buttonFacebookLogin).setVisibility(View.GONE);
                findViewById(R.id.buttonFacebookSignout).setVisibility(View.VISIBLE);
            } else {
                mStatusTextView.setText(R.string.signed_out);
                mDetailTextView.setText(null);

                findViewById(R.id.buttonFacebookLogin).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonFacebookSignout).setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.buttonFacebookSignout) {
                signOut();
            }
        }*/
    }