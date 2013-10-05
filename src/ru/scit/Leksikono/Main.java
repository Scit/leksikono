package ru.scit.Leksikono;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */

    private SuggestionTextView suggestionTextView;
    private Translator translator;
    private Button clearButton;
    private Button translateButton;
    private TextView translationContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        suggestionTextView =
                (SuggestionTextView) this.findViewById(R.id.suggestion_text_view);
        suggestionTextView.addTextChangedListener(suggestionTextWatcher);
        suggestionTextView.setOnItemClickListener(itemClickListener);
        suggestionTextView.setOnKeyListener(keyListener);

        clearButton = (Button) this.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(clearButtonListener);

        translateButton = (Button) this.findViewById(R.id.translate_button);
        translateButton.setOnClickListener(translateButtonListener);

        translationContent = (TextView) this.findViewById(R.id.translation_content);

        translator = new Translator();
    }

    private class SuggestionsTask extends AsyncTask<String, Void, List<String>> {
        protected List<String> doInBackground(String... input) {
            ArrayList<String> suggestions = (ArrayList<String>)
                    Main.this.translator.getVariants(input[0]);
            return suggestions;
        }

        protected void onPostExecute(List<String> suggestions) {
            suggestionTextView.showSuggestions(suggestions);
        }
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... word) {
            String translation = Main.this.translator.getTranslation(word[0]);

            return translation;
        }

        protected void onPostExecute(String translation) {
            translationContent.setText(Html.fromHtml(translation));
        }
    }

    TextWatcher suggestionTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (suggestionTextView.isPerformingCompletion()) {
                return;
            }

            int textLength = suggestionTextView.getText().length();
            requestController.attempt(textLength);
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Main.this.performTranslation();
        }
    };

    View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                performTranslation();
                return true;
            }
            return false;
        }
    };

    private void performTranslation() {
        suggestionTextView.clearSuggestions();
        requestController.revokeRequests();
        String word = Main.this.suggestionTextView.getText().toString();
        new TranslationTask().execute(word);
    }

    private SuggestionsRequestController requestController = new SuggestionsRequestController() {
        @Override
        public void on() {
            String input = Main.this.suggestionTextView.getText().toString();
            new SuggestionsTask().execute(input);
        }
    };

    private View.OnClickListener clearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Main.this.suggestionTextView.setText(null);
        }
    };

    private View.OnClickListener translateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            performTranslation();
        }
    };
}
