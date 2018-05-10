package com.learnexo.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.util.FirebaseUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQ_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private String photoUrl;
    private String displayName;
    private String googleEmail;

    private EditText loginEmail;
    private EditText loginPass;

    private ConstraintLayout mConstraintLayout;
    private Snackbar snackbar;

    private Button mGoogleBtn;
    private Button mFacebookBtn;
    private TextView mRegisterBtn;
    private TextView forgotPassTView;

    private GoogleApiClient mGoogleApiClient;

    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    private CallbackManager mFbCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireViews();


        setupGoogleApiClient();
        googleLoginListener();

        fbLoginListener();

        forgotPassTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = loginEmail.getText().toString();

                if (!TextUtils.isEmpty(emailAddress)) {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                        builder.setTitle("Forgot Password");
                                        builder.setMessage("We sent an email to " + emailAddress + " to reset your password");
                                        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                snackbar = Snackbar.make(mConstraintLayout, "Check your mail", Snackbar.LENGTH_SHORT);
                                                snackbar.show();
                                            }
                                        });
                                        builder.show();
                                        Log.d(TAG, "Email sent.");
                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                        builder.setMessage("Could not send password reset email, please verify the email address");
                                        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                               snackbar = Snackbar.make(mConstraintLayout, "This mail not found in Database", Snackbar.LENGTH_SHORT);
                                               snackbar.show();
                                            }
                                        });
                                        builder.show();

                                    }
                                }
                            });
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage("Enter your email address");
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           snackbar = Snackbar.make(mConstraintLayout, "Enter email and proceed", Snackbar.LENGTH_SHORT);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                           snackbar.show();
                        }
                    });
                    builder.show();
                }
            }
        });

        registrationListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseUtil.doesUserExist()) {
            gotoFeed();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_REQ_CODE) {
            handleGoogleSigninIntent(data);
        } else {
            // Pass the activity result back to the Facebook SDK
            mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleGoogleSigninIntent(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null) {
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

    private void fbLoginListener() {
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

                Intent gglSigninIntent = Auth.GoogleSignInApi.getSignInIntent(MainActivity.this.mGoogleApiClient);
                startActivityForResult(gglSigninIntent, GOOGLE_SIGN_IN_REQ_CODE);
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

    private void wireViews() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        forgotPassTView = findViewById(R.id.forgotPassTView);

        mConstraintLayout = findViewById(R.id.constraintLayout);

        mGoogleBtn = findViewById(R.id.googleBtn);
        mFacebookBtn = findViewById(R.id.facebook_login);
        mRegisterBtn = findViewById(R.id.registerBtn);
    }


    private void gotoFeed() {
        Intent tabsActivityIntent = new Intent(MainActivity.this, TabsActivity.class);
        startActivity(tabsActivityIntent);
        finish();
    }


    private void checkGoogleAccInFirebase(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        FirebaseUtil.sAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "GoogleSignInWithCredential:success");

                            if(FirebaseUtil.doesUserExist()) {
                                    photoUrl = account.getPhotoUrl().toString();
                                    displayName = account.getDisplayName();
                                    googleEmail = account.getEmail();

                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("firstName", displayName);
                                userMap.put("lastName", "");
                                userMap.put("emailId", googleEmail);
                                userMap.put("googleDpUri", photoUrl);

                                mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("reg_details")
                                        .document("doc").set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        gotoFeed();
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_LONG).show();
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                        mGoogleBtn.setEnabled(true);

                        // ...
                    }
                });
    }

    private void checkFbAccInFirebase(AccessToken token) {
        Log.d(TAG, "checkFbAccInFirebase:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseUtil.sAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "fbSignInWithCredential:success");

                            FirebaseUser user = FirebaseUtil.getCurrentUser();
                            if(user != null) {
                                if (!user.getProviderData().isEmpty() && user.getProviderData().size() > 1)
                                    photoUrl = "https://graph.facebook.com/" + user.getProviderData()
                                            .get(1).getUid() + "/picture?type=large";
                                    //photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                gotoFeed();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "fbSignInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                          //  updateUI();
                        }
                        mFacebookBtn.setEnabled(true);
                    }
                });
    }

    public void loginButtonListener(View view) {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !email.contains(" ")) {
            FirebaseUtil.sAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkIfUserExists();
                    } else {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                throw Objects.requireNonNull(task.getException());
                            }
                        } catch (FirebaseAuthInvalidUserException wrongEmail) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setMessage("This email doesn't exist. Enter registered email");
                            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    snackbar = Snackbar.make(mConstraintLayout, "Email not found in database", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.YELLOW);
                                    snackbar.show();
                                }
                            });
                            builder.show();

                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setMessage("Entered password is wrong. Give relevant password");
                            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    snackbar = Snackbar.make(mConstraintLayout, "Password doesn't match", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.YELLOW);
                                    snackbar.show();
                                }
                            });
                            builder.show();

                        } catch (Exception e) {
                            Log.e(TAG, Arrays.toString(e.getStackTrace()));
                        }
                    }
                }
            });
        } else if(email.contains(" ") && !TextUtils.isEmpty(pass)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("The email you entered contains spaces. Give proper email");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "No spaces in email", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Enter email and password");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    snackbar = Snackbar.make(mConstraintLayout, "Fields can't be empty", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
            builder.show();

        }
    }

    public void checkIfUserExists() {

        String user_id = FirebaseUtil.getCurrentUserId();
        mFirebaseUtil.mFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    gotoFeed();
                }else {
                    String errorMessage = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                    }
                    Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
