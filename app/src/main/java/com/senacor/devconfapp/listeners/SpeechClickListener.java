package com.senacor.devconfapp.listeners;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.senacor.devconfapp.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by saba on 19.12.16.
 */

public class SpeechClickListener implements View.OnClickListener {

    private Activity activity;
    private PopupWindow pw;

    public SpeechClickListener(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addSpeechButton) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, null, false);
            pw = new PopupWindow(popupView);
            pw.setWidth(MATCH_PARENT);
            pw.setHeight(MATCH_PARENT);
            pw.setOutsideTouchable(false);
            pw.setContentView(popupView);
            pw.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);

            //Auskommentieren; cancel_new_speech, add_new_speech werden rot angezeigt

         /*
            Button cancel = (Button) activity.findViewById(R.id.cancel_new_speech);
            cancel.setOnClickListener(this);

            Button saveSpeech = (Button) activity.findViewById(R.id.add_new_speech);
            saveSpeech.setOnClickListener(this);
        }

        if (v.getId() == R.id.cancel_new_speech) {
            pw.dismiss();
        }

        if (v.getId() == R.id.add_new_speech) {

        }*/

        }
        /*pw = new PopupWindow(this);
                   pw.setWidth(MATCH_PARENT);
                   pw.setHeight(MATCH_PARENT);
                   pw.setOutsideTouchable(false);
                   pw.setContentView(popupView);
                   pw.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                                    // Use any one method - showAtLocation or showAsDropDown to show the popup
                                    pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                   // speechHandler.addSpeech();
    }*/
    }
}
