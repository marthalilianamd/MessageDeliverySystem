package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    View viewFragAccount;
    TextInputEditText textEmail;
    TextInputEditText tvNameUser;
    TextInputEditText tvNombre;
    User user;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewFragAccount= inflater.inflate(R.layout.fragment_account, container, false);
        return viewFragAccount;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textEmail = view.findViewById(R.id.textEmail);
        tvNameUser = view.findViewById(R.id.textUser);
        tvNombre = view.findViewById(R.id.textNombre);

        String email = getArguments().getString("email");
        user = User.getInstance();

        String[] nickname = email.split("@");
        String nameUser = nickname[0];
        textEmail.setText(email);
        tvNameUser.setText(nameUser);
        tvNombre.setText(nameUser);
    }
}
