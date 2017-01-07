package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.senacor.devconfapp.activities.CreateEventActivity;
import com.senacor.devconfapp.activities.LoginActivity;
import com.senacor.devconfapp.fragments.SpeechDialog;
import com.senacor.devconfapp.models.Event;
import com.senacor.devconfapp.models.Speech;

/**
 * Created by saba on 07.01.17.
 */

public class ErrorHandler {

    private static ErrorHandler errorHandlerCreateEvent = new ErrorHandler();

    private ErrorHandler() {

    }

    public static ErrorHandler getInstance() {
        return errorHandlerCreateEvent;
    }

    protected static void handleUnauthorizedError(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        Context context = (Context) activity;
        CharSequence text = "Your session has expired, please log in again.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    protected static void handleConflictError(Object object, Activity activity) {
        if (object instanceof Event) {
            Event event = (Event) object;
            Intent intent = new Intent(activity, CreateEventActivity.class);
            if (event.getEventId() != null) {
                intent.putExtra("eventId", event.getEventId());
            }
            intent.putExtra("name", event.getName());
            intent.putExtra("place", event.getPlace());
            intent.putExtra("date", event.getDate().toString());
            intent.putExtra("validationErrorDate", true);
            activity.startActivity(intent);

        }
        if (object instanceof Speech) {
            Speech speech = (Speech) object;
            DialogFragment speechFragment;
            if (speech.extractAndSaveSpeechId() != null) {
                speechFragment = SpeechDialog.newInstance(speech, true, true, "colliding");
            }else{
                speechFragment = SpeechDialog.newInstance(speech, false, true, "colliding");
            }
            speechFragment.show(activity.getFragmentManager(), "SpeechDialog");
        }

    }
}
