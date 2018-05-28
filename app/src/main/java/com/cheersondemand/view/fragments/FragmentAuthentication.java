package com.cheersondemand.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheersondemand.R;
import com.cheersondemand.util.CustomEditText;
import com.cheersondemand.util.DrawableClickListener;
import com.cheersondemand.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAuthentication extends Fragment implements View.OnClickListener {


    @BindView(R.id.btnLoginTab)
    Button btnLoginTab;
    @BindView(R.id.btnSignUpTab)
    Button btnSignUpTab;
    @BindView(R.id.viewSignUp)
    RelativeLayout viewSignUp;
    @BindView(R.id.btnSignUpTabLoginView)
    Button btnSignUpTabLoginView;
    @BindView(R.id.btnLoginTabLoginView)
    Button btnLoginTabLoginView;
    @BindView(R.id.viewSignIn)
    RelativeLayout viewSignIn;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    CustomEditText etPassword;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.btnFbLogin)
    Button btnFbLogin;
    @BindView(R.id.btnGoogleLogin)
    Button btnGoogleLogin;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.viewA)
    LinearLayout viewA;
    @BindView(R.id.etEmailLogin)
    EditText etEmailLogin;
    @BindView(R.id.etPasswordLogin)
    CustomEditText etPasswordLogin;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.LLView)
    LinearLayout LLView;
    @BindView(R.id.viewB)
    LinearLayout viewB;
    Unbinder unbinder;
    CallbackManager callbackManager;
    @BindView(R.id.btnFbLoginViewLogin)
    Button btnFbLoginViewLogin;
    @BindView(R.id.btnGoogleLoginViewLogin)
    Button btnGoogleLoginViewLogin;

    private AccessToken mAccessToken;
    Util util;
    private static final int RC_SIGN_IN = 234;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    public FragmentAuthentication() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        util = new Util();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewA.setVisibility(View.VISIBLE);
        viewB.setVisibility(View.GONE);
        viewSignUp.setVisibility(View.VISIBLE);
        viewSignIn.setVisibility(View.GONE);
        btnLoginTab.setOnClickListener(this);
        btnSignUpTab.setOnClickListener(this);
        btnSignUpTabLoginView.setOnClickListener(this);
        btnLoginTabLoginView.setOnClickListener(this);
        btnFbLoginViewLogin.setOnClickListener(this);
        btnGoogleLoginViewLogin.setOnClickListener(this);
        btnFbLogin.setOnClickListener(this);
        btnGoogleLogin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        etPassword.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        Toast.makeText(getActivity(),"ddd",Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }

        });
        etPasswordLogin.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        break;

                    default:
                        break;
                }
            }

        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("DEBUG", "UserID=" + loginResult.getAccessToken().getUserId() + "Token=" + loginResult.getAccessToken().getToken());
                mAccessToken = loginResult.getAccessToken();
                util.setSnackbarMessage(getActivity(), "Login Sucess", LLView);
                getUserProfile(mAccessToken);

            }

            @Override
            public void onCancel() {
                Log.e("DEBUG", "Login Cancelled");
                //   Utils.showToast(getActivity(),getString(R.string.login_cancled));
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("DEBUG", "error" + error.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //You can fetch user info like this…
                            object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.e("Response", object.toString());
                            object.getString("name");
                            //object.getString(“email”));
                            //object.getString(“id”));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }





    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("DEBUG",user.getDisplayName());
                            Log.e("DEBUG",user.getEmail());
                            util.setSnackbarMessage(getActivity(), "Login Sucess", LLView);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginTab:
                viewSignIn.setVisibility(View.VISIBLE);
                viewSignUp.setVisibility(View.GONE);
                viewA.setVisibility(View.GONE);
                viewB.setVisibility(View.VISIBLE);
                //   btnLoginTab.animate().translationY(0);

                initAnimation(viewSignUp,btnLoginTabLoginView);
                break;
            case R.id.btnSignUpTab:
                viewSignIn.setVisibility(View.GONE);
                viewSignUp.setVisibility(View.VISIBLE);
                viewA.setVisibility(View.VISIBLE);
                viewB.setVisibility(View.GONE);
                // initAnimation(viewSignIn,viewSignUp);

                break;
            case R.id.btnSignUpTabLoginView:
                viewSignUp.setVisibility(View.VISIBLE);
                viewSignIn.setVisibility(View.GONE);
                viewA.setVisibility(View.VISIBLE);
                viewB.setVisibility(View.GONE);
                //  initAnimation(viewSignIn,viewSignUp);
                initAnimationR(viewSignUp,btnSignUpTab);

                break;
            case R.id.btnLoginTabLoginView:
                viewSignIn.setVisibility(View.VISIBLE);
                viewSignUp.setVisibility(View.GONE);
                viewA.setVisibility(View.GONE);
                viewB.setVisibility(View.VISIBLE);
                //  initAnimation(viewSignUp,viewSignIn);

                break;
            case R.id.btnFbLogin:
                if (Util.isNetworkConnectivity(getActivity())) {
                    loginButton.performClick();
                }
            case R.id.btnFbLoginViewLogin:
                if (Util.isNetworkConnectivity(getActivity())) {
                    loginButton.performClick();
                }
                break;
            case R.id.btnGoogleLogin:
                if (Util.isNetworkConnectivity(getActivity())) {
                    signIn();
                }
                break;
            case R.id.btnGoogleLoginViewLogin:
                if (Util.isNetworkConnectivity(getActivity())) {
                    signIn();
                }
                break;
        }
    }

    private void initAnimation( View  viewA, View viewB)
    {
        Animation animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        viewB.startAnimation(animShow);
       /* Animation  animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
        viewA.setAnimation(animHide);*/
    }
    private void initAnimationR( View  viewA, View viewB)
    {
        Animation animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
        viewB.startAnimation(animShow);
       /* Animation  animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
        viewA.setAnimation(animHide);*/
    }
}
