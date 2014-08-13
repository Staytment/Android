package de.trottl.staytment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);

        if (btn_logout != null) {
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutUser();
                }
            });
        }
        return view;
    }

    public void logoutUser() {
        Log.i("LogOut", "Logout user");
        SharedPreferences shPref = this.getActivity().getSharedPreferences("Staytment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();

        editor.remove("Email");
        editor.apply();

        String email = shPref.getString("Email", null);

        if (email == null) {
            Intent loginAct = new Intent(this.getActivity().getApplicationContext(), LogInActivity.class);
            startActivity(loginAct);
            getActivity().finish();
        }
    }
}
