package com.weqa.ui;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weqa.R;
import com.weqa.model.Authentication;
import com.weqa.model.CodeConstants;
import com.weqa.model.Org;
import com.weqa.service.InstanceIdService;
import com.weqa.util.SharedPreferencesUtil;

import net.glxn.qrgen.android.QRCode;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static String LOG_TAG = "WEQA-LOG";

    private SharedPreferencesUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "font/HelveticaNeueMed.ttf");

        util = new SharedPreferencesUtil(this, LOG_TAG);
        Authentication authentication = util.getAuthenticationInfo();

        String[] nameTokens = authentication.getEmployeeName().split(" ");
        String mobile = authentication.getMobileNo();

        String firstName = "";
        String lastName = "";
        if (nameTokens.length == 1) {
            firstName = nameTokens[0];
        }
        else if (nameTokens.length > 1){
            firstName = nameTokens[0];
            lastName = nameTokens[1];
        }

        String email = getEmail(authentication);
        String designation = getDesignation(authentication);

        String qrCodeText = CodeConstants.QR_CODE_MEMBER + "," + InstanceIdService.getAppInstanceId(this)
                            + "," + getOrgIdString(authentication)
                            + "," + firstName + "," + lastName
                            + "," + designation + "," + mobile;
        Bitmap bm = QRCode.from(qrCodeText).bitmap();

        float bitmapWidth = bm.getWidth();
        float bitmapHeight = bm.getHeight();

        Log.d(LOG_TAG, "Bitmap width = " + bitmapWidth + ", height = " + bitmapHeight);

        ImageView qrCodeImage = (ImageView) findViewById(R.id.qrCode);
        qrCodeImage.setImageBitmap(bm);

        TextView nameTextView = (TextView) findViewById(R.id.nameText);
//        nameTextView.setText(authentication.getEmployeeName());
        nameTextView.setText("Amarjit Singh");

        nameTextView.setTypeface(tf);

        TextView emailTextView = (TextView) findViewById(R.id.emailText);
        emailTextView.setText(email);

        TextView mobileTextView = (TextView) findViewById(R.id.mobileText);
        mobileTextView.setText(mobile);

        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });

        TextView arrow1 = (TextView) findViewById(R.id.arrow1);
        TextView arrow2 = (TextView) findViewById(R.id.arrow2);
        TextView arrow3 = (TextView) findViewById(R.id.arrow3);
        TextView arrow4 = (TextView) findViewById(R.id.arrow4);

        arrow1.setOnClickListener(this);
        arrow2.setOnClickListener(this);
        arrow3.setOnClickListener(this);
        arrow4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
    }

    private String getEmail(Authentication auth) {
        String email = auth.getOrganization().get(0).getEmailId();
        if (email == null || email.equals(""))
            email = "Not Specified";
        return email;
    }

    private String getOrgIdString(Authentication auth) {
        StringBuffer sb = new StringBuffer("");
        boolean firstTime = true;
        for (Org o : auth.getOrganization()) {
            if (!firstTime) {
                sb.append("_");
            }
            sb.append(o.getOrganizationId());
            firstTime = false;
        }
        return sb.toString();
    }

    private String getDesignation(Authentication auth) {
        String designation = auth.getOrganization().get(0).getDesignation();
        if (designation == null || designation.equals(""))
            designation = "Not Specified";
        return designation;
    }
}