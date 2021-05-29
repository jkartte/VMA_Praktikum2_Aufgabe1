package de.thk.vma.vma_praktikum2_aufgabe1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    String stadt;
    int counterAll = 0;
    int counterRadweg = 0;

    private RadioGroup group;
    private EditText editText;
    private EditText editNumber;
    private EditText anz_all;
    private EditText anz_rad;
    private EditText letzter_input;
    private EditText vorletzter_input;
    private ArrayList<Falschparker> falschparker;
    CheckBox checkbox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.tablelayout);

        //ArrayList der Falschparker erstellen
        falschparker = new ArrayList<Falschparker>();

        //RadioGroup setzen
        group = findViewById(R.id.radiogroup);
        group.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangeListener());

        //EditTexts erstellen und Tastatur verstecken, wenn EditTexts Focus verliert
        editText = findViewById(R.id.input_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editNumber = findViewById(R.id.input_number);
        editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //Ausgabefelder initialisieren
        anz_all = findViewById(R.id.anz_all);
        anz_rad = findViewById(R.id.anz_rad);
        anz_all.setText("0");
        anz_rad.setText("0");
        letzter_input = findViewById(R.id.letzer_input);
        vorletzter_input = findViewById(R.id.vorletzer_input);
        letzter_input.setText("");
        vorletzter_input.setText("");

        //Checkbox initialisieren
        checkbox = findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new MyCheckboxOnCheckedChangeListener());

        //OK-Button und Clear-Button initialisieren
        final Button buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new ButtonListenerOK());
        final Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new ButtonListenerClear());

        //Toast.makeText(this,"Activity 1: onCreate()", Toast.LENGTH_LONG).show();
        //Log.v("DEMO","---> Activity1: onCreate() <--- ");
    }

    //Funktion, die beim Aufruf die Tastatur versteckt
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Überprüfen, ob ein Button der Radio-Group ausgewählt wurde
    class MyRadioGroupOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int buttonId = group.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) findViewById(buttonId);
            stadt = (String) button.getText();
            //Toast.makeText(MainActivity.this, button.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    //Überprüfen, ob eine Checkbox ausgewählt wurde
    class MyCheckboxOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            String ausgabe = button.getText()+": ";
            if (isChecked)
                ausgabe += "markiert";
            else
                ausgabe += "nicht markiert";
            //Toast.makeText(MainActivity.this,ausgabe,Toast.LENGTH_LONG).show();
        }
    }

    //Übernahme der Eingaben, wenn der Button OK geklickt wird
    class ButtonListenerOK implements OnClickListener {

        public void onClick(View v){
            //Auf falsche Eingabe überprüfen
            if (group.getCheckedRadioButtonId()==-1 || editText.getText().toString().isEmpty() || editNumber.getText().toString().isEmpty())
                Toast.makeText(MainActivity.this, "Falsche Eingabe!", Toast.LENGTH_SHORT).show();
            else
            {
                //neuen Falschparker erzeugen
                falschparker.add(new Falschparker(stadt, editText.getText().toString(), editNumber.getText().toString(), checkbox.isChecked()));

                //Zähler hochzählen
                counterAll++;
                anz_all.setText(String.valueOf(counterAll));
                if (checkbox.isChecked()){
                    counterRadweg++;
                    anz_rad.setText(String.valueOf(counterRadweg));
                }

                //Ausgabe der letzten beiden Inputs
                if (falschparker.size()>=2)
                    vorletzter_input.setText(falschparker.get(falschparker.size()-2).getAusgabe());
                letzter_input.setText(falschparker.get(falschparker.size()-1).getAusgabe());
            }
        }
    }

    //Löschen aller Eingaben, wenn der Button Clear gedrückt wird
    class ButtonListenerClear implements OnClickListener {

        public void onClick(View v){
            //RadioButtons zurücksetzen
            RadioButton radioButton_K = (RadioButton) findViewById(R.id.radiobutton_K);
            radioButton_K.setChecked(false);
            RadioButton radioButton_BM = (RadioButton) findViewById(R.id.radiobutton_BM);
            radioButton_BM.setChecked(false);
            RadioButton radioButton_GL = (RadioButton) findViewById(R.id.radiobutton_GL);
            radioButton_GL.setChecked(false);

            //Eingabefelder zurücksetzen
            editText.setText("");
            editNumber.setText("");

            //Checkbox zurücksetzen
            if (checkbox.isChecked())
                checkbox.toggle();

            //Falls alles zurückgesetzt werden soll:
            /*falschparker.clear();         //Liste an Falschparkern zurücksetzen
            anz_all.setText("0");
            anz_rad.setText("0");
            counterAll=0;
            counterRadweg=0;
            letzter_input.setText("");
            vorletzter_input.setText("");
             */
        }
    }

    //Klasse Falschparker
    class Falschparker {
        private String stadt;
        private String name;
        private String number;
        private Boolean checkbox;

        public Falschparker(String stadt, String name, String  number, Boolean checkbox){
            this.name = name;
            this.stadt = stadt;
            this.number = number;
            this.checkbox = checkbox;
        }

        public String getAusgabe(){
            String ausgabe;
            String aufRadweg = "Auf Radweg";
            if (this.checkbox)
                ausgabe = this.stadt + " | " + this.name + " | " + this.number + " | " + aufRadweg;
            else
                ausgabe = this.stadt + " | " + this.name + " | " + this.number ;
            return ausgabe;
        }
    }

}



