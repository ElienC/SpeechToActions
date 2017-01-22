package com.example.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeechToActionsActivity extends AppCompatActivity {

    @BindString(R.string.nospeech)
    String noSpeech;

    @BindString(R.string.saysomething)
    String saySomething;

    @BindView(R.id.googleText)
    EditText googleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_totext);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.imageButton)
    public void onMicClick(View v) {
        promptSpeechInput();
    }

    @OnClick(R.id.websiteButton)
    public void goToWebsite(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tinfbo2.hogent.be/nativeapps/"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.contactsButton)
    public void goToContacts(View v){
        Intent i= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
        startActivity(i);
    }

    @OnClick(R.id.dialerButton)
    public void goToDialer(View v){
        Intent i= new Intent(Intent.ACTION_DIAL);
        startActivity(i);
    }

    @OnClick(R.id.googleButton)
    public void goToGoogle(View v){
        String query = googleText.getText().toString().trim();
        searchGoogle(query);
    }

    public void searchGoogle(String query)
    {
        String url = "http://www.google.com/#q=";
        String final_url = url + query;

        Uri uri = Uri.parse(final_url);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(final_url));
        startActivity(i);
    }

    @OnClick(R.id.voiceCommandButton)
    public void executeVoiceCommand()
    {
        Intent i = new Intent(Intent.ACTION_VOICE_COMMAND);
        startActivity(i);
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, saySomething);
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(SpeechToActionsActivity.this, noSpeech, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String text = result.get(0).toLowerCase();
            if(text.equals("open"))
            {
                goToWebsite(null);
            } else if (text.equals("contacts"))
            {
                goToContacts(null);
            } else if (text.equals("call"))
            {
                goToDialer(null);
            } else if (text.startsWith("google"))
            {
                String query = text.split(" ", 2)[1];
                searchGoogle(query);
            } else
            {
                executeVoiceCommand();
            }
        }
    }


}
