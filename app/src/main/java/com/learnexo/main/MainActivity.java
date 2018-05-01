package com.learnexo.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static String photoUrl;
    private static final int GOOGLE_SIGN_IN_REQ_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText loginEmail;
    private EditText loginPass;

    private Button mGoogleBtn;
    private Button mFacebookBtn;
    private TextView mRegisterBtn;

    private String user_id;
    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference mDatabase;

    private AuthCredential credential;

    // Facebook callbackmanager
    FirebaseUser currentUser;
    private CallbackManager mFbCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wiringViews();
        firebaseSetup();

        setupGoogleApiClient();
        googleLoginListener();

        facebookLoginListener();

        registrationListener();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            gotoFeed();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_REQ_CODE) {
            handleGoogleSigninIntent(data);
        } else {
            handleFBsigninIntent(requestCode, resultCode, data);
        }

    }

    private void handleFBsigninIntent(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleGoogleSigninIntent(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();

                checkGoogleAccInFirebase(account);
            }

        } catch (ApiException e) {
            //Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
        }
    }

    private void registrationListener() {
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(regIntent);
            }
        });
    }

    private void facebookLoginListener() {
        //Facebook callback manager
        mFbCallbackManager = CallbackManager.Factory.create();

        // Facebook Login Button Listener
        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFacebookBtn.setEnabled(false);

                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        checkFbAccInFirebase(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        //...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        //...
                    }
                });
            }
        });
    }

    private void googleLoginListener() {
        // Google sign in Button Listener
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleBtn.setEnabled(false);

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(MainActivity.this.mGoogleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQ_CODE);
            }
        });
    }

    private void setupGoogleApiClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(MainActivity.this, "Error in Connection", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void firebaseSetup() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void wiringViews() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);

        mGoogleBtn = findViewById(R.id.googleBtn);
        mFacebookBtn = findViewById(R.id.facebook_login);
        mRegisterBtn = findViewById(R.id.registerBtn);
    }


    private void gotoFeed() {
        Intent tabsActivityIntent = new Intent(MainActivity.this, TabsActivity.class);
        startActivity(tabsActivityIntent);
        finish();
    }


    private void checkGoogleAccInFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "GoogleSignInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                                photoUrl = user.getPhotoUrl().toString();
                                mGoogleBtn.setEnabled(true);
                                gotoFeed();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                            mGoogleBtn.setEnabled(true);
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void checkFbAccInFirebase(AccessToken token) {
        Log.d(TAG, "checkFbAccInFirebase:" + token);

        facebookOnlySignin(credential);
    }

    private void facebookOnlySignin(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "fbSignInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user != null) {

                                if (!user.getProviderData().isEmpty() && user.getProviderData().size() > 1)
                                    photoUrl = "https://graph.facebook.com/" + user.getProviderData()
                                            .get(1).getUid() + "/picture?type=large";

                            //    photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

                                mFacebookBtn.setEnabled(true);
                                gotoFeed();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "fbSignInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            mFacebookBtn.setEnabled(true);
                          //  updateUI();
                        }
                    }
                });
    }

    public void loginButtonListener(View view) {

        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();
     //   credential = EmailAuthProvider.getCredential(email, pass);

        emailpassSignin(email, pass);

    }

    private void emailpassSignin(String email, String pass) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                       try {
                           throw task.getException();
                       } catch(FirebaseAuthInvalidUserException wrongEmail) {
                           Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                        } catch(FirebaseAuthInvalidCredentialsException wrongPassword) {
                           Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                       } catch(Exception e) {
                           e.printStackTrace();
                       }

                    } else {
                        checkIfUserExists();
                    }

                }
            });

        } else {

            Toast.makeText(MainActivity.this, "Fields can't be empty", Toast.LENGTH_LONG).show();

        }

    }

    public void checkIfUserExists() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
            user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    gotoFeed();
                }else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
