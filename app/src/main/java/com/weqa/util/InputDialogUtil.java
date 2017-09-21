package com.weqa.util;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.ui.SplashScreenActivity;

/**
 * Created by Manish Ballav on 9/17/2017.
 */

public class InputDialogUtil {

    public static void showCodeInputDialog(final SplashScreenActivity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_code_input);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText("Enter Code:");

        final EditText code = (EditText) dialog.findViewById(R.id.code);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.okButton);

        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    activity.sendCode();
                }
            });

        dialog.show();
    }
}
