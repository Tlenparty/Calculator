package com.geekbrain.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {
    /*
    1. Переделайте все кнопки на Материал.+
    2. Все размеры и строки сделайте ресурсами. +
    3. Создайте стиль для своего приложения. +
    4. * Создайте светлую и тёмную тему для приложения.
    */
    //

    TextView tvOutput;
    EditText tvInput;
    String process;
    SwitchMaterial switchModeBtn;
    String resultInfoKey = "resultInfoKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMessage("onCreate");

        initViews();


        tvInput.setShowSoftInputOnFocus(false);

        tvInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getString(R.string.enter_the_value).equals(tvInput.getText().toString())) {
                    tvInput.setText("");
                }
            }
        });

        switchModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


    }

    private void initViews() {
        tvInput = findViewById(R.id.tvInput);
        tvOutput = findViewById(R.id.tvOutput);
        switchModeBtn = findViewById(R.id.switchModeBtn);
    }


    private void updateText(String strToAdd) {
        String oldStr = tvInput.getText().toString();
        int cursorPos = tvInput.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if (getString(R.string.enter_the_value).equals(tvInput.getText().toString())) {
            tvInput.setText(strToAdd);
            tvInput.setSelection(cursorPos + 1);
        } else {
            tvInput.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
            tvInput.setSelection(cursorPos + 1);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        showMessage("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMessage("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showMessage("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showMessage("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showMessage("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMessage("onDestroy");
    }

    // метод для показа всплывающих уведомлений
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //Log.i("Lifecycle",message); // логирование
    }


    public void zeroBTN(View view) {
        updateText("0");

    }

    public void oneBTN(View view) {
        updateText("1");

    }

    public void twoBTN(View view) {
        updateText("2");
    }

    public void threeBTN(View view) {
        updateText("3");
    }

    public void fourBTN(View view) {
        updateText("4");
    }

    public void fiveBTN(View view) {
        updateText("5");
    }

    public void sixBTN(View view) {
        updateText("6");
    }

    public void sevenBTN(View view) {
        updateText("7");
    }

    public void eightBTN(View view) {
        updateText("8");
    }

    public void nineBTN(View view) {
        updateText("9");
    }

    public void percentBTN(View view) {
        updateText("%");
    }

    public void backspaceBTN(View view) {
        int cursorPos = tvInput.getSelectionStart();
        int textLen = tvInput.getText().length();

        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) tvInput.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            tvInput.setText(selection);
            tvInput.setSelection(cursorPos - 1);
        }

    }

    public void plusBTN(View view) {
        updateText("+");
    }

    public void minusBTN(View view) {
        updateText("-");
    }

    public void multiplicationBTN(View view) {
        updateText("x");
    }

    public void divisionBTN(View view) {
        updateText("÷");
    }

    public void dotBTN(View view) {
        updateText(".");

    }

    public void equalBTN(View view) {
        process = tvInput.getText().toString();
        saveDataInArray(process);
        process = process.replaceAll("x", "*");
        process = process.replaceAll("%", "/100");
        process = process.replaceAll("÷", "/");

        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);

        String finalResult = "";

        try {
            Scriptable scriptable = rhino.initStandardObjects();
            finalResult = rhino.evaluateString(scriptable, process, "javascript", 1, null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            finalResult = "0";
        }
        tvOutput.setText(finalResult);
    }

    // Для сохранения значения при повороте экрана
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // outState - просто контейнер в который можно что-то положить. Необходиомсть моетить поле как Serializable
        process = tvOutput.getText().toString();
        outState.putSerializable(resultInfoKey, process);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Каст по классу TextView. Каст значения, которое мы забрали
        process = (String) savedInstanceState.getSerializable(resultInfoKey);
        tvOutput.setText(process);
    }

    public void plusMinusBTN(View view) {
        updateText("±");
    }

    public void clearBTN(View view) {
        tvInput.setText("");
        tvOutput.setText("");
    }

    public void parenthesesBNT(View view) {
        int cursorPos = tvInput.getSelectionStart();
        int openPar = 0;
        int closePar = 0;
        int textLen = tvInput.getText().length();

        for (int i = 0; i < cursorPos; i++) {
            if (tvInput.getText().toString().substring(i, i + 1).equals("(")) {
                openPar += 1;
            }
            if (tvInput.getText().toString().substring(i, i + 1).equals(")")) {
                openPar += 1;
            }
        }
        if (openPar == closePar || tvInput.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText("(");
            tvInput.setSelection(cursorPos + 1);
        } else if (closePar < openPar && !tvInput.getText().toString().substring(textLen - 1, textLen).equals(")")) {
            updateText(")");
        }
        tvInput.setSelection(cursorPos + 1);
    }

    public void saveDataInArray(String process) {
        ArrayList<String> dataFromTvInput = new ArrayList<>();
        dataFromTvInput.add(process);
    }

}