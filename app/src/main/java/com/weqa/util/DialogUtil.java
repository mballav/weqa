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
        public boolean buttonPressed = false;

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
                    if (refreshHotspots && (!buttonPressed))  {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.updateFloorplanAvailability();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static class Timer2 {

        private Dialog dialog;
        private boolean refreshHotspots, removeLocalBooking;
        private LandingScreenActivity activity;
        private String qrCodeBooked, qrCodeNew;
        private String bookingTime;
        public boolean buttonPressed = false;

        public Timer2(Dialog dialog, boolean refreshHotspots, boolean removeLocalBooking, LandingScreenActivity activity,
                      String qrCodeBooked, String qrCodeNew, String bookingTime) {
            this.dialog = dialog;
            this.refreshHotspots = refreshHotspots;
            this.removeLocalBooking = removeLocalBooking;
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
                    if (!buttonPressed) {
                        SharedPreferencesUtil util = new SharedPreferencesUtil(activity);
                        util.addBooking(qrCodeNew, bookingTime);
                        activity.releaseQRCodeItem(qrCodeBooked, false, removeLocalBooking);
                    }
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

        // start the timer
        final DialogUtil.Timer timer = new DialogUtil.Timer(dialog, refreshHotspots, activity);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.buttonPressed = true;
                dialog.dismiss();
                activity.releaseQRCodeItem(qrCode, true, true);
            }
        });
        cancelButton.setText("Release");

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setText("Close");
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.buttonPressed = true;
                dialog.dismiss();
                if (refreshHotspots) {
                    activity.updateFloorplanAvailability();
                }
            }
        });

        dialog.show();
    }

    public static void showOkDialogWithCancelForSecondBooking(final LandingScreenActivity activity, String textToDisplay,
                                                              final String qrCodeOld, final String qrCodeNew,
                                                              final String bookingTime, final boolean refreshHotspots) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);


        // start the timer
        final DialogUtil.Timer2 timer2 = new DialogUtil.Timer2(dialog, refreshHotspots, false, activity, qrCodeOld, qrCodeNew, bookingTime);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setText("Release");
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer2.buttonPressed = true;
                dialog.dismiss();
                activity.releaseQRCodeItem(qrCodeNew, false, false);
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setText("Close");
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer2.buttonPressed = true;
                dialog.dismiss();
                SharedPreferencesUtil util = new SharedPreferencesUtil(activity);
                util.addBooking(qrCodeNew, bookingTime);
                activity.releaseQRCodeItem(qrCodeOld, false, false);
                if (refreshHotspots) {
                    activity.updateFloorplanAvailability();
                }
            }
        });

        dialog.show();
    }

    public static void showOkDialog(final LandingScreenActivity activity, String textToDisplay,
                                    final boolean refreshHotspots, final boolean doTimer) {
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
        if (doTimer) {
            // start the timer
            final DialogUtil.Timer timer = new DialogUtil.Timer(dialog, refreshHotspots, activity);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.buttonPressed = true;
                    dialog.dismiss();
                    if (refreshHotspots) {
                        activity.updateFloorplanAvailability();
                    }
                }
            });
        }
        else {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (refreshHotspots) {
                        activity.updateFloorplanAvailability();
                    }
                }
            });
        }

        dialog.show();
    }


    public static void showDialogWithThreeButtons(final LandingScreenActivity activity, String textToDisplay,
                                              final String qrCode, final boolean refreshHotspots) {
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
                activity.releaseQRCodeItem(qrCode, true, true);
                if (refreshHotspots) {
                    activity.updateFloorplanAvailability();
                }
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
