package com.cheersondemand.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheersondemand.R;
import com.cheersondemand.model.AuthenticationResponse;
import com.cheersondemand.util.C;
import com.cheersondemand.util.ImageLoader.ImageLoader;
import com.cheersondemand.util.SharedPreference;
import com.cheersondemand.view.ActivityContainer;
import com.cheersondemand.view.MainActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment implements View.OnClickListener {


    @BindView(R.id.imgProfile)
    RoundedImageView imgProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.llProfileView)
    LinearLayout llProfileView;
    @BindView(R.id.profile)
    LinearLayout profile;
    @BindView(R.id.tvNumberWishListItems)
    TextView tvNumberWishListItems;
    @BindView(R.id.llWishList)
    RelativeLayout llWishList;
    @BindView(R.id.llOrders)
    RelativeLayout llOrders;
    @BindView(R.id.llSavedAddress)
    RelativeLayout llSavedAddress;
    @BindView(R.id.llPaymentInfo)
    RelativeLayout llPaymentInfo;
    @BindView(R.id.llConnectedAcc)
    RelativeLayout llConnectedAcc;
    @BindView(R.id.llChangePassword)
    RelativeLayout llChangePassword;
    @BindView(R.id.llHelp)
    RelativeLayout llHelp;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.llNotification)
    RelativeLayout llNotification;
    @BindView(R.id.llLogout)
    RelativeLayout llLogout;
    Unbinder unbinder;
    boolean isLogin;
    AuthenticationResponse authenticationResponse;
    @BindView(R.id.viewChangePassword)
    View viewChangePassword;
    ImageLoader imageLoader;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new ImageLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLogin = SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN);

        if (isLogin) {
            authenticationResponse = SharedPreference.getInstance(getActivity()).getUser(C.AUTH_USER);
            tvEmail.setText(authenticationResponse.getData().getUser().getEmail());
            tvName.setText(authenticationResponse.getData().getUser().getName());
            llChangePassword.setVisibility(View.VISIBLE);
            viewChangePassword.setVisibility(View.VISIBLE);
            if (authenticationResponse.getData().getUser().getProfilePicture() != null) {
                imageLoader.DisplayImage(authenticationResponse.getData().getUser().getProfilePicture(), imgProfile);
            }

        } else {
            btnEdit.setVisibility(View.GONE);
            tvEmail.setText(getString(R.string.view_orders));
            tvName.setText(getString(R.string.sign_in));
            llChangePassword.setVisibility(View.GONE);
            viewChangePassword.setVisibility(View.GONE);
        }

        btnEdit.setOnClickListener(this);
        llProfileView.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llProfileView:
                // move to login screen if not login
                if (!isLogin) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_AUTHNITICATION);
                    bundle.putBoolean(C.IS_LOGIN_SCREEN, true);

                    intent.putExtra(C.BUNDLE,bundle);
                    startActivity(intent);
                }
                break;
            case R.id.btnEdit:

                Intent intent = new Intent(getActivity(), ActivityContainer.class);
                Bundle bundle = new Bundle();
                intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_UPDATE_PROFILE);
                intent.putExtra(C.BUNDLE,bundle);
                startActivity(intent);
                break;
        }
    }
}
