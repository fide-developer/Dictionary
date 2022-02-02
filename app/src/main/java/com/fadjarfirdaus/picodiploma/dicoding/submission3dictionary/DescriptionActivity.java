package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model.ModelDictionary;

public class DescriptionActivity extends AppCompatActivity {
    TextView textWord,textDesc;
    public static final String EXTRAS_DATA = "EXTRAS_DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        textWord = findViewById(R.id.text_words);
        textDesc = findViewById(R.id.text_description);

        ModelDictionary detData = getIntent().getParcelableExtra(EXTRAS_DATA);
        textWord.setText(detData.getWords());
        textDesc.setText(detData.getDescription());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Description");
        }
    }
}
