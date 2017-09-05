package com.weqa.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.ui.LandingScreenActivity;

/**
 * Created by Manish Ballav on 9/3/2017.
 */

public class DialogUtil {

    public static class Timer {

        private Dialog dialog;
        private boolean refreshHotspots;
        private LandingScreenActivity activity;

        public Timer(Dialog dialog, boolean refreshHotspots, LandingScreenActivity activity) {
            this.dialog = dialog;
            this.refreshHotspots = refreshHotspots;
            this.activity = activity;
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000); // 5 seconds timer
                    if (dialog.isShowing())
                        dialog.dismiss();
                    if (refreshHotspots)  {
                        activity.updateFloorplanAvailability();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static class Timer2 {

        private Dialog dialog;
        private boolean refreshHotspots;
        private LandingScreenActivity activity;
        private String qrCodeBooked, qrCodeNew;
        private String bookingTime;

        public Timer2(Dialog dialog, boolean refreshHotspots, LandingScreenActivity activity,
                      String qrCodeBooked, String qrCodeNew, String bookingTime) {
            this.dialog = dialog;
            this.refreshHotspots = refreshHotspots;
            this.activity = activity;
            this.qrCodeBooked = qrCodeBooked;
            this.qrCodeNew = qrCodeNew;
            this.bookingTime = bookingTime;
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000); // 5 seconds timer
                    if (dialog.isShowing())
                        dialog.dismiss();
                    SharedPreferencesUtil util = new SharedPreferencesUtil(activity);
                    util.addBooking(qrCodeNew, bookingTime);
                    activity.releaseQRCodeItem(qrCodeBooked);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void showOkDialogWithCancel(final LandingScreenActivity activity, String textToDisplay,
                                              final String qrCode, final boolean refreshHotspots) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.releaseQRCodeItem(qrCode);
            }
        });
        cancelButton.setText("Release");

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setText("Close");
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (refreshHotspots) {
                    activity.updateFloorplanAvailability();
                }
            }
        });

        // start the timer
        new DialogUtil.Timer(dialog, refreshHotspots, activity);

        dialog.show();
    }

    public static void showOkDialogWithCancelForSecondBooking(final LandingScreenActivity activity, String textToDisplay,
                                                              final String qrCodeOld, final String qrCodeNew, final String bookingTime) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setText("Release");
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.releaseQRCodeItem(qrCodeNew);
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setText("Close");
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferencesUtil util = new SharedPreferencesUtil(activity);
                util.addBooking(qrCodeNew, bookingTime);
                activity.releaseQRCodeItem(qrCodeOld);
            }
        });

        // start the timer
        new DialogUtil.Timer2(dialog, true, activity, qrCodeOld, qrCodeNew, bookingTime);

        dialog.show();
    }

    public static void showOkDialog(final LandingScreenActivity activity, String textToDisplay,
                                    final boolean refreshHotspots, boolean doTimer) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.GONE);

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        okButton.setText("Close");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (refreshHotspots) {
                    activity.updateFloorplanAvailability();
                }
            }
        });

        if (doTimer) {
            // start the timer
            new DialogUtil.Timer(dialog, refreshHotspots, activity);
        }
        dialog.show();
    }


    public static void showDialogWithThreeButtons(final LandingScreenActivity activity, String textToDisplay,
                                              final String qrCode) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_3buttons);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.message);
        text.setText(textToDisplay);

        Button cancelButton = (Button) dialog.findViewById(R.id.button1);
        cancelButton.setText("Release");
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.releaseQRCodeItem(qrCode);
            }
        });

        Button renewButton = (Button) dialog.findViewById(R.id.button2);
        renewButton.setText("Renew");
        // if button is clicked, close the custom dialog
        renewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.renewQRCodeItem(qrCode);
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.button3);
        okButton.setText("Close");
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
