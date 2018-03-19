package com.example.jekan.fyp_test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jekan.fyp_test.activity.SetImageActivity;

/**
 * Created by jekan on 2018-03-17.
 */

public class InputDialog extends Dialog {

    EditText editUerHeight;
    String height;

    public InputDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_user_height);
        editUerHeight = (EditText)findViewById(R.id.input_user_height);

        Button btnSubmit = (Button)findViewById(R.id.btn_submit_user_height);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height = editUerHeight.getText().toString();
                dismiss();
            }
        });

        Button btnCancel = (Button)findViewById(R.id.btn_cancel_user_height);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public String getEditUerHeight() {
        return height;
    }

    public void setEditUerHeight(String height) {
        this.height = height;
        editUerHeight.setText(height);
    }
}
