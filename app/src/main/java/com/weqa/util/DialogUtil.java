package com.weqa.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.ui.LandingScreenActivity;
import com.weqa.ui.TeamSummaryActivity;

/**
 * Created by Manish Ballav on 9/3/2017.
 */

public class DialogUtil {

    public static class Timer {

        private Dialog dialog;
        private boolean refreshHotspots;
        private LandingScreenActivity activity;
        public boolean buttonPressed = false;
        private TextView countdown;

        public Timer(Dialog dialog, boolean refreshHotspots, LandingScreenActivity activity) {
            this.dialog = dialog;
            this.refreshHotspots = refreshHotspots;
            this.activity = activity;
            this.countdown = (TextView) dialog.findViewById(R.id.countdown);
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    for (int i = 5; i > 0; i--) {
                        final int j = i;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    countdown.setText("" + j);
                            }
                        });
                        Thread.sleep(1000); // 5 seconds timer
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                countdown.setText("0");
                                dialog.dismiss();
                            }
                            if (refreshHotspots && (!buttonPressed))  {
                                activity.updateFloorplanAvailability();
                            }
                        }
                    });
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
        private TextView countdown;

        public Timer2(Dialog dialog, boolean refreshHotspots, boolean removeLocalBooking, LandingScreenActivity activity,
                      String qrCodeBooked, String qrCodeNew, String bookingTime) {
            this.dialog = dialog;
            this.refreshHotspots = refreshHotspots;
            this.removeLocalBooking = removeLocalBooking;
            this.activity = activity;
            this.qrCodeBooked = qrCodeBooked;
            this.qrCodeNew = qrCodeNew;
            this.bookingTime = bookingTime;
            this.countdown = (TextView) dialog.findViewById(R.id.countdown);
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    for (int i = 5; i > 0; i--) {
                        final int j = i;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    countdown.setText("" + j);
                            }
                        });
                        Thread.sleep(1000); // 5 seconds timer
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                countdown.setText("0");
                                dialog.dismiss();
                            }
                        }
                    });
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

    public static class Timer3 {

        private Dialog dialog;
        private Activity activity;
        public boolean buttonPressed = false;
        private TextView countdown;

        public Timer3(Dialog dialog, Activity activity) {
            this.dialog = dialog;
            this.activity = activity;
            this.countdown = (TextView) dialog.findViewById(R.id.countdown);
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    for (int i = 5; i > 0; i--) {
                        final int j = i;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    countdown.setText("" + j);
                            }
                        });
                        Thread.sleep(1000); // 5 seconds timer
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                countdown.setText("0");
                                dialog.dismiss();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static class Timer4 {

        private Dialog dialog;
        private boolean removeLocalBooking;
        private TeamSummaryActivity activity;
        private String qrCodeBooked, qrCodeNew;
        private String bookingTime;
        public boolean buttonPressed = false;
        private TextView countdown;

        public Timer4(Dialog dialog, boolean removeLocalBooking, TeamSummaryActivity activity,
                      String qrCodeBooked, String qrCodeNew, String bookingTime) {
            this.dialog = dialog;
            this.removeLocalBooking = removeLocalBooking;
            this.activity = activity;
            this.qrCodeBooked = qrCodeBooked;
            this.qrCodeNew = qrCodeNew;
            this.bookingTime = bookingTime;
            this.countdown = (TextView) dialog.findViewById(R.id.countdown);
            thread.start();
        }

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    for (int i = 5; i > 0; i--) {
                        final int j = i;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    countdown.setText("" + j);
                            }
                        });
                        Thread.sleep(1000); // 5 seconds timer
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                countdown.setText("0");
                                dialog.dismiss();
                            }
                        }
                    });
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

    public static void showMessageDialog(Context c, String textToDisplay) {
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
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

    public static void showOkDialogWithCancel(final LandingScreenActivity activity, String textToDisplay,
                                              final String qrCode, final boolean refreshHotspots) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_timer);

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

    public static void showOkDialogWithCancel(final TeamSummaryActivity activity, String textToDisplay,
                                              final String qrCode) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_timer);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);

        // start the timer
        final DialogUtil.Timer3 timer = new DialogUtil.Timer3(dialog, activity);

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
            }
        });

        dialog.show();
    }

    public static void showOkDialogWithCancelForSecondBooking(final LandingScreenActivity activity, String textToDisplay,
                                                              final String qrCodeOld, final String qrCodeNew,
                                                              final String bookingTime, final boolean refreshHotspots) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_timer);

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

    public static void showOkDialogWithCancelForSecondBooking(final TeamSummaryActivity activity, String textToDisplay,
                                                              final String qrCodeOld, final String qrCodeNew,
                                                              final String bookingTime) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_timer);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.bookingmessage);
        text.setText(textToDisplay);


        // start the timer
        final DialogUtil.Timer4 timer4 = new DialogUtil.Timer4(dialog, false, activity, qrCodeOld, qrCodeNew, bookingTime);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setText("Release");
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer4.buttonPressed = true;
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
                timer4.buttonPressed = true;
                dialog.dismiss();
                SharedPreferencesUtil util = new SharedPreferencesUtil(activity);
                util.addBooking(qrCodeNew, bookingTime);
                activity.releaseQRCodeItem(qrCodeOld, false, false);
            }
        });

        dialog.show();
    }

    public static void showOkDialog(final LandingScreenActivity activity, String textToDisplay,
                                    final boolean refreshHotspots, final boolean doTimer) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (doTimer) {
            dialog.setContentView(R.layout.dialog_booking_timer);
        }
        else {
            dialog.setContentView(R.layout.dialog_booking);
        }

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

    public static void showOkDialog(final Activity activity, String textToDisplay,
                                    final boolean doTimer) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (doTimer) {
            dialog.setContentView(R.layout.dialog_booking_timer);
        }
        else {
            dialog.setContentView(R.layout.dialog_booking);
        }

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
            final DialogUtil.Timer3 timer = new DialogUtil.Timer3(dialog, activity);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.buttonPressed = true;
                    dialog.dismiss();
                }
            });
        }
        else {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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

    public static void showDialogWithThreeButtons(final TeamSummaryActivity activity, String textToDisplay,
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
                activity.releaseQRCodeItem(qrCode, true, true);
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
