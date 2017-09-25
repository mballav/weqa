package com.weqa.widget;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.weqa.R;
import com.weqa.model.CodeConstants;
import com.weqa.model.TeamUser;
import com.weqa.model.adapterdata.TeamMemberListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class CustomQRScannerActivity extends Activity implements View.OnClickListener {

    private static final String LOG_TAG = "WEQA-LOG";

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;

    private List<String> previousCodes = new ArrayList<String>();
    private ArrayList<TeamMemberListItem> alreadyAddedUsers = new ArrayList<TeamMemberListItem>();
    private ArrayList<TeamMemberListItem> newlyAddedUsers = new ArrayList<TeamMemberListItem>();

    private long orgId, screenId;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String code = result.getText();
            if(code == null) {
                return;
            }

            if ((previousCodes.indexOf(code) != -1)) {
                return;
            }

            if (!isValidUserCode(code)) {
                barcodeView.setStatusText("Invalid QR Code!");
                return;
            }

            String uuid = getUuid(code);

            if (isUserAlreadyPartOfTeam(uuid)) {
                barcodeView.setStatusText("User already added!");
                return;
            }

            if (!isUserFromAcceptableOrg(code)) {
                barcodeView.setStatusText("User does not belong to this organization!");
                return;
            }

            previousCodes.add(code);
            String name = addUser(code);
            barcodeView.setStatusText(name + " successfully added!");
            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_continuous_scan);

        Intent intent = getIntent();

        orgId = intent.getIntExtra("ORG_ID", 0);
        screenId = intent.getIntExtra("SCREEN_ID", 0);

        this.alreadyAddedUsers = intent.getParcelableArrayListExtra("EXISTING_USERS");

        printUsers();

        Log.d(LOG_TAG, "org id inside CustomQRScannerActivity is ---------------------- " + orgId);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
/*        String qrCode1 = "0000,cDb0EVR9cxA,1_2,TestUser,One,Business Analyst,0400904480";
        String qrCode2 = "0000,ftGkQAQyV6Q,1,Deborah,McCarthy,Project Manager,0405983938";
        String qrCode3 = "0000,dynEls2Wytc,1,Bertrand,Cheminat,Business Manager,0405324938";

        testAddCode(qrCode1);
        testAddCode(qrCode2);
        testAddCode(qrCode3);
*/
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("NEW_USER_LIST", newlyAddedUsers);
        setResult(2,intent);
        finish();//finishing activity
    }

    private void printUsers() {
        Log.d(LOG_TAG, "Already added users: ");
        for (TeamMemberListItem t : this.alreadyAddedUsers) {
            Log.d(LOG_TAG, "UUID: " + t.getUuid() + ", Name: " + t.getFirstName() + " " + t.getLastName());
        }
    }

    private void testAddCode(String code) {
        if(code == null) {
            return;
        }

        if ((previousCodes.indexOf(code) != -1)) {
            return;
        }

        if (!isValidUserCode(code)) {
            Log.d(LOG_TAG, "Invalid QR Code! -- " + code);
            return;
        }

        String uuid = getUuid(code);

        if (isUserAlreadyPartOfTeam(uuid)) {
            Log.d(LOG_TAG, "User already added! -- " + code);
            return;
        }

        if (!isUserFromAcceptableOrg(code)) {
            Log.d(LOG_TAG, "User does not belong to this organization! -- " + code);
            return;
        }

        previousCodes.add(code);
        String name = addUser(code);
        Log.d(LOG_TAG, name + " successfully added!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private boolean isValidUserCode(String code) {
        return code.startsWith(CodeConstants.QR_CODE_MEMBER);
    }

    private boolean isUserFromAcceptableOrg(String code) {
        String[] tokens = code.split(",");
        String[] orgIds = tokens[2].split("_");

/*        Log.d(LOG_TAG, "----------------------------------------------- OrgId = " + orgId + " Profile org ids = " + tokens[2]);
*/
        boolean found = false;
        for (String oId : orgIds) {
            long orgIdLong = Long.parseLong(oId);
            if (orgId == orgIdLong) {
                found = true;
                break;
            }
        }
        return found;
    }

    private String getUuid(String code) {
        String[] tokens = code.split(",");
        return tokens[1];
    }

    private boolean isUserAlreadyPartOfTeam(String uuid) {
        boolean found = false;
        for (TeamMemberListItem u : alreadyAddedUsers) {
            if (u.getUuid().equals(uuid)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private String addUser(String code) {
        String[] tokens = code.split(",");
        TeamMemberListItem user = new TeamMemberListItem();
        user.setUuid(tokens[1]);
        user.setFirstName(tokens[3]);
        user.setLastName(tokens[4]);
        user.setDesignation(tokens[5]);
        user.setMobile(tokens[6]);
        alreadyAddedUsers.add(user);
        newlyAddedUsers.add(user);

        return tokens[3] + " " + tokens[4];
    }
}
