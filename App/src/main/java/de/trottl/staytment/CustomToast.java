package de.trottl.staytment;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

    View view;
    Context context;
    String toastText;
    Toast toast;

    public CustomToast(Context context, String toastText) {
        setToastText(toastText);
        setContext(context);
        createToast();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setToastText(String toastText) {
        this.toastText = toastText;
    }

    public void show() {
        toast.show();
    }

    void createToast() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        this.view = inflater.inflate(R.layout.custom_toast, null);

        TextView txt = (TextView) this.view.findViewById(R.id.toast_text);
        txt.setText(this.toastText);

        Toast toast = new Toast(this.context);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(this.view);
        this.toast = toast;
    }
}
