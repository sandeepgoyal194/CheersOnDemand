package com.cheersondemand.view.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.cheersondemand.model.AuthenticationResponse;
import com.cheersondemand.model.LoginRequest;
import com.cheersondemand.model.SignUpRequest;
import com.cheersondemand.model.SocialLoginRequest;
import com.cheersondemand.model.User;
import com.cheersondemand.presenter.AuthenicationPresenterImpl;
import com.cheersondemand.presenter.IAutheniticationPresenter;
import com.cheersondemand.util.C;
import com.cheersondemand.util.CustomEditText;
import com.cheersondemand.util.DrawableClickListener;
import com.cheersondemand.util.SharedPreference;
import com.cheersondemand.util.Util;
import com.cheersondemand.view.ActivityHome;
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
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
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

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAuthentication extends Fragment implements IAutheniticationPresenter.IAuthenticationView,View.OnClickListener {


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
    CircularProgressButton btnSignUp;
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
    CircularProgressButton btnLogin;
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
    @BindView(R.id.skip_and_continue)
    TextView skipAndContinue;
    @BindView(R.id.skip_and_continue_login)
    TextView skipAndContinueLogin;

    private AccessToken mAccessToken;
    Util util;
    private static final int RC_SIGN_IN = 234;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
     boolean isPasswordVisibleSignUP=false;
    String accessToken;

     boolean isPasswordVisibleLogin=false;
     boolean isLoginScreen=false;
    IAutheniticationPresenter iAutheniticationPresenter;
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
        iAutheniticationPresenter=new AuthenicationPresenterImpl(this,getActivity());

            isLoginScreen=getArguments().getBoolean(C.IS_LOGIN_SCREEN);

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
    public void onDestroy() {
        try {
            super.onDestroy();
            iAutheniticationPresenter.onDestroy();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if(isLoginScreen){
            viewA.setVisibility(View.GONE);
            viewB.setVisibility(View.VISIBLE);
            viewSignUp.setVisibility(View.GONE);
            viewSignIn.setVisibility(View.VISIBLE);
        }
        else {
            viewA.setVisibility(View.VISIBLE);
            viewB.setVisibility(View.GONE);
            viewSignUp.setVisibility(View.VISIBLE);
            viewSignIn.setVisibility(View.GONE);
        }
        btnLoginTab.setOnClickListener(this);
        btnSignUpTab.setOnClickListener(this);
        btnSignUpTabLoginView.setOnClickListener(this);
        btnLoginTabLoginView.setOnClickListener(this);
        btnFbLoginViewLogin.setOnClickListener(this);
        btnGoogleLoginViewLogin.setOnClickListener(this);
        btnFbLogin.setOnClickListener(this);
        btnGoogleLogin.setOnClickListener(this);
        skipAndContinue.setOnClickListener(this);
        skipAndContinueLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        initLogin();
        initSignUp();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("DEBUG", "UserID=" + loginResult.getAccessToken().getUserId() + "Token=" + loginResult.getAccessToken().getToken());
                mAccessToken = loginResult.getAccessToken();
                util.setSnackbarMessage(getActivity(), "Login Sucess", LLView,false);
               // getUserProfile(mAccessToken);
                socailLogin(""+mAccessToken,"facebook");

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
               final GoogleSignInAccount account = task.getResult(ApiException.class);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String scope = "oauth2:"+ Scopes.EMAIL+" "+ Scopes.PROFILE;
                             accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
                            Log.d("DEBUG", "accessToken:"+accessToken); //accessToken:ya29.Gl...

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GoogleAuthException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AsyncTask.execute(runnable);
                //authenticating with firebase
                firebaseAuthWithGoogle(account,accessToken);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final String token) {
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

                            Log.e("DEBUG", user.getDisplayName());
                            Log.e("DEBUG", user.getEmail());
                            util.setSnackbarMessage(getActivity(), "Login Sucess", LLView,false);
                            socailLogin(token,"google");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    void socailLogin(String token,String provider){
        SocialLoginRequest socialLoginRequest=new SocialLoginRequest();
        socialLoginRequest.setAccessToken(token);
        socialLoginRequest.setLoginType(2);
        socialLoginRequest.setGrantType("password");
        socialLoginRequest.setProvider(provider);
        socialLoginRequest.setUuid(Util.id(getActivity()));
        iAutheniticationPresenter.setSignUpSocail(socialLoginRequest);
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

                initAnimation(viewSignUp, btnLoginTabLoginView);
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
                initAnimationR(viewSignUp, btnSignUpTab);

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
            case R.id.skip_and_continue:
                if (Util.isNetworkConnectivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), ActivityHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.skip_and_continue_login:
                if (Util.isNetworkConnectivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), ActivityHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.btnSignUp:
                btnSignUp.startAnimation();
                SignUpRequest signUpRequest=new SignUpRequest();
                User user=new User();
                user.setName(etName.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());
                signUpRequest.setUser(user);
                iAutheniticationPresenter.setSignUp(signUpRequest);
                break;
            case R.id.btnLogin:
                btnLogin.startAnimation();
                LoginRequest loginRequest=new LoginRequest();
                loginRequest.setEmail(etEmailLogin.getText().toString());
                loginRequest.setGrantType("password");
                loginRequest.setLoginType(1);
                loginRequest.setUuid(Util.id(getActivity()));
                loginRequest.setPassword(etPasswordLogin.getText().toString());
                iAutheniticationPresenter.setLoginUsingEmail(loginRequest);
                break;
        }
    }


    private void initSignUp() {
        btnSignUp.setEnabled(false);

        etPassword.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        if(!isPasswordVisibleSignUP) {
                            isPasswordVisibleSignUP=true;
                            etPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_password, 0, R.drawable.ic_eye_visible, 0);

                        }
                        else {
                            isPasswordVisibleSignUP=false;
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_password, 0, R.drawable.ic_eye, 0);

                        }
                        break;

                    default:
                        break;
                }
            }

        });


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
             try {
                 if (b) {
                     etName.setBackgroundResource(R.drawable.edit_text_back_enable);
                     etName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_username_enable, 0, 0, 0);

                 } else {
                     etName.setBackgroundResource(R.drawable.edit_text_back_disable);
                     etName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_username, 0, 0, 0);

                 }
             }
             catch (Exception e){
                 e.printStackTrace();
             }
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                try {
                    if (b) {
                        etEmail.setBackgroundResource(R.drawable.edit_text_back_enable);
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_enable, 0, 0, 0);

                    } else {
                        etEmail.setBackgroundResource(R.drawable.edit_text_back_disable);
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    if (b) {
                        etPassword.setBackgroundResource(R.drawable.edit_text_back_enable);

                    } else {
                        etPassword.setBackgroundResource(R.drawable.edit_text_back_disable);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
        etName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                       validation();



            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                validation();



            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                validation();



            }
        });


    }

    private void initLogin() {

        btnLogin.setEnabled(false);
        etPasswordLogin.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        if(!isPasswordVisibleLogin) {
                            isPasswordVisibleLogin=true;
                            etPasswordLogin.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_password, 0, R.drawable.ic_eye_visible, 0);

                        }
                        else {
                            isPasswordVisibleLogin=false;
                            etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(  R.drawable.ic_password, 0, R.drawable.ic_eye, 0);

                        }
                        break;

                    default:
                        break;
                }
            }

        });
        etEmailLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

               try {
                   if (b) {
                       etEmailLogin.setBackgroundResource(R.drawable.edit_text_back_enable);
                       etEmailLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_enable, 0, 0, 0);

                   } else {
                       etEmailLogin.setBackgroundResource(R.drawable.edit_text_back_disable);
                       etEmailLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);

                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
        etPasswordLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

               try {
                   if (b) {
                       etPasswordLogin.setBackgroundResource(R.drawable.edit_text_back_enable);


                   } else {
                       etPasswordLogin.setBackgroundResource(R.drawable.edit_text_back_disable);

                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
        etEmailLogin.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                validationLogin();



            }
        });
        etPasswordLogin.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                validationLogin();



            }
        });


    }

    void  validationLogin(){


            if(Util.isValidMail(etEmailLogin.getText().toString())){

                if(etPasswordLogin.length()>4 && etPasswordLogin.length()<31){
                    btnLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
                    btnLogin.setEnabled(true);

                }
                else {
                    btnLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_disable));
                    btnLogin.setEnabled(false);

                }
            }
            else {
                btnLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_disable));
                btnLogin.setEnabled(false);

            }
    }
    void  validation(){
        if(etName.getText().length()>2 && etName.length()<31){

            if(Util.isValidMail(etEmail.getText().toString())){

                if(etPassword.length()>4 && etPassword.length()<31){
                    btnSignUp.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
                    btnSignUp.setEnabled(true);

                }
                else {

                    btnSignUp.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_disable));
                    btnSignUp.setEnabled(false);

                }
            }
            else {

                btnSignUp.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_disable));
                btnSignUp.setEnabled(false);

            }
        }
        else {

            btnSignUp.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_disable));
            btnSignUp.setEnabled(false);

        }


    }
    private void initAnimation(View viewA, View viewB) {
        Animation animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.view_show);
        viewB.startAnimation(animShow);
       /* Animation  animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
        viewA.setAnimation(animHide);*/
    }

    private void initAnimationR(View viewA, View viewB) {
        Animation animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide);
        viewB.startAnimation(animShow);
       /* Animation  animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
        viewA.setAnimation(animHide);*/
    }


    @Override
    public void getResponseSuccess(AuthenticationResponse response) {
       // Log.e("DEBUG",""+response.toString());
        if(response.getSuccess()){
            btnLogin.revertAnimation();
            if (Util.isNetworkConnectivity(getActivity())) {
                SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN,true);

                SharedPreference.getInstance(getActivity()).setUser(C.AUTH_USER,response);

                Intent intent = new Intent(getActivity(), ActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void getResponseError(String response) {
        Log.e("DEBUG",""+response);
        btnLogin.revertAnimation();
        btnSignUp.revertAnimation();
        util.setSnackbarMessage(getActivity(),response,LLView,true);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
