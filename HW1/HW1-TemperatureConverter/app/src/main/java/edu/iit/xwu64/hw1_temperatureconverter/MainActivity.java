package edu.iit.xwu64.hw1_temperatureconverter;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView historyText;
    private RadioGroup convertDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText) findViewById(R.id.inputText);
        historyText = (TextView) findViewById(R.id.outputText);
        convertDirection = (RadioGroup) findViewById(R.id.radioGroup);
        historyText.setMovementMethod(ScrollingMovementMethod.getInstance());

        if (savedInstanceState != null){
            historyText.setText(savedInstanceState.getString("textView"));
        }
    }

    public void convert(View view){
        String input = inputText.getText().toString();
        if (input.length() == 0){
            return;
        }
        String history = historyText.getText().toString();
        float outputValue;

        if (convertDirection.getCheckedRadioButtonId() == R.id.c2f) {
            float inputValue = Float.valueOf(input).floatValue();
            outputValue = (float) ((inputValue*(9.0/5.0))+32.0);
            historyText.append(String.format(String.valueOf(inputValue)+" C => %.1f F \n",outputValue));
        }
        else{
            float inputValue = Float.valueOf(input).floatValue();
            outputValue = (float) ((inputValue-32.0)*(5.0/9.0));
            historyText.append(String.format(String.valueOf(inputValue)+" F => %.1f C\n",outputValue));
        }
        inputText.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("textView", historyText.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
