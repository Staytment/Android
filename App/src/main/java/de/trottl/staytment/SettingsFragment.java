package de.trottl.staytment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
        final EditText distance_txt = (EditText) view.findViewById(R.id.txt_distance);
        final SharedPreferences shPref_settings = getActivity().getSharedPreferences("Staytment_Settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shPref_settings.edit();

        if (btn_logout != null) {
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutUser();
                }
            });
        }

        if (distance_txt != null) {

            if (shPref_settings.getInt("map_load_distance", 0) != 0) {
                distance_txt.setText(String.valueOf(shPref_settings.getInt("map_load_distance", 0)));
            }

            distance_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (distance_txt.getText().length() > 0) {
                        if (Integer.parseInt(distance_txt.getText().toString()) > 0) {
                            editor.putInt("map_load_distance", Integer.parseInt(distance_txt.getText().toString()));
                            editor.apply();
                        }
                    }
                }
            });
        }
        return view;
    }

    public void logoutUser() {
        Log.i("LogOut", "Logout user");
        SharedPreferences shPref = this.getActivity().getSharedPreferences("Staytment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();

        editor.clear();
        editor.apply();

        String email = shPref.getString("Email", null);

        if (email == null) {
            Intent loginAct = new Intent(this.getActivity().getApplicationContext(), LogInActivity.class);
            startActivity(loginAct);
            getActivity().finish();
        }
    }
}
