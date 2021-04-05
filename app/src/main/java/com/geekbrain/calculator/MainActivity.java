package com.geekbrain.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.*;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    /*
    1.Создайте активити с настройками, где включите выбор темы приложения.
    2.Доделайте приложение «Калькулятор». Это последний урок с созданием приложения «Калькулятор».
    3.Сделайте интент-фильтр для запуска калькулятора извне, а также напишите тестовое приложение,
     запускающее приложение-калькулятор
    */

    TextView tvOutput;
    EditText tvInput;
    String currentExpression;
    Button btn_plus_minus, btn_plus, btn_minus, btn_multiplication, btn_div, btn_back,
           btn_dot, btn_equals, btn_parentheses, btn_per, btn_del, btn_set;

    CalculatorLogic calculatorLogic = new CalculatorLogic();
    String keyCalculatorLogic = "resultInfoKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDisplays();
        initButtonsListeners();
        tvInput.setShowSoftInputOnFocus(false);
    }

    private void initDisplays() {
        btn_div = findViewById(R.id.button_division);
        btn_multiplication = findViewById(R.id.button_multiplication);
        btn_plus = findViewById(R.id.button_plus);
        btn_minus = findViewById(R.id.button_minus);
        btn_plus_minus = findViewById(R.id.button_plus_minus);
        btn_parentheses = findViewById(R.id.button_parentheses);
        btn_dot = findViewById(R.id.button_dot);
        btn_per = findViewById(R.id.button_percent);
        btn_back = findViewById(R.id.button_backspace);
        btn_del = findViewById(R.id.button_clear);
        btn_equals = findViewById(R.id.button_equals);
        btn_set = findViewById(R.id.button_settings);
        tvInput = findViewById(R.id.tvInput);
        tvOutput = findViewById(R.id.tvOutput);
    }

    @SuppressLint("SetTextI18n")
    private void initButtonsListeners() {
        int[] numberButtonsIds = new int[]{
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
                R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
        };
        for (int i = 0; i < numberButtonsIds.length; i++) {
            int finalI1 = i;
            findViewById(numberButtonsIds[i]).setOnClickListener(v -> updateText(String.valueOf(finalI1)));
        }

        btn_div.setOnClickListener(v -> updateText("÷"));

        btn_multiplication.setOnClickListener(v -> updateText("x"));

        btn_plus.setOnClickListener(v -> updateText("+"));

        btn_minus.setOnClickListener(v -> updateText("-"));

        btn_plus_minus.setOnClickListener(v -> {
            String oldText = tvInput.getText().toString().trim();
            if (!oldText.startsWith("-")) {
                tvInput.setText("-" + oldText);
            }else if(oldText.startsWith("-") || oldText.startsWith(" ")){
                oldText = tvInput.getText().toString().trim();
                String newText = oldText.replaceAll("-", " ");
                tvInput.setText(newText);
            }
        });

        btn_parentheses.setOnClickListener(v -> {
            int cursorPos = tvInput.getSelectionStart();
            int openPar = 0;
            int closePar = 0;
            int textLen = tvInput.getText().length();

            for (int i = 0; i < cursorPos; i++) {
                if (tvInput.getText().toString().startsWith("(", i)) {
                    openPar += 1;
                }
                if (tvInput.getText().toString().startsWith(")", i)) {
                    openPar += 1;
                }
            }
            if (openPar == closePar || tvInput.getText().toString().startsWith("(", textLen - 1)) {
                updateText("(");
                tvInput.setSelection(cursorPos + 1);
            } else if (closePar < openPar && !tvInput.getText().toString().startsWith(")", textLen - 1)) {
                updateText(")");
            }
            tvInput.setSelection(cursorPos + 1);
        });

        btn_dot.setOnClickListener(v -> updateText("."));

        btn_per.setOnClickListener(v -> updateText("%"));

        btn_back.setOnClickListener(v -> {
            int cursorPos = tvInput.getSelectionStart();
            int textLen = tvInput.getText().length();

            if (cursorPos != 0 && textLen != 0) {
                SpannableStringBuilder selection = (SpannableStringBuilder) tvInput.getText();
                selection.replace(cursorPos - 1, cursorPos, "");
                tvInput.setText(selection);
                tvInput.setSelection(cursorPos - 1);
            }
        });

        btn_del.setOnClickListener(v -> {
            tvInput.setText("");
            tvOutput.setText("");
        });

        btn_equals.setOnClickListener(v -> {
            String expression = tvInput.getText().toString();
            String preparedExpression = preparingExpression(expression);
            String currentExpression = polandWriter(preparedExpression);
            backPolandWriter(currentExpression);
        });

        btn_set.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Choose_theme.class);
            startActivity(intent);
        });

        tvInput.setOnClickListener(v -> {
            if (getString(R.string.enter_the_value).equals(tvInput.getText().toString())) {
                tvInput.setText("");
            }
        });
    }

    private void backPolandWriter(String currentExpression) {
        // Пока не дойдем до пробела будем складывать в операнд цифры
        StringBuilder operand;
        LinkedList<Double> stackListEquals = new LinkedList<>();

        for (int i = 0; i < currentExpression.length(); i++) {
            // Если пробел, пропускаем код ниже
            if (currentExpression.charAt(i) == ' ') {
                continue;
            }
            // Если у нас число, то засовываем его в операнд
            if (getPriority(currentExpression.charAt(i)) == 0) {
                operand = new StringBuilder(" ");
                while (currentExpression.charAt(i) != ' ' && getPriority(currentExpression.charAt(i)) == 0) {
                    operand.append(currentExpression.charAt(i++)); // Переходим к следующему символу
                    if (i == currentExpression.length()) {
                        break;
                    }
                }
                stackListEquals.add(Double.parseDouble(operand.toString()));
            }
            // Если попался математический символ
            if (getPriority(currentExpression.charAt(i)) > 1) {
                // Берем 2 верхних числа из списка
                if(!stackListEquals.isEmpty()) {
                    double a = stackListEquals.removeLast(),
                            b = stackListEquals.removeLast();
                    if (currentExpression.charAt(i) == '+') {
                        stackListEquals.add(b + a);
                    }
                    if (currentExpression.charAt(i) == '-') {
                        stackListEquals.add(b - a);
                    }
                    if (currentExpression.charAt(i) == '*') {
                        stackListEquals.add(b * a);
                    }
                    if (currentExpression.charAt(i) == '/') {
                        stackListEquals.add(b / a);
                    }
                    if (currentExpression.charAt(i) == '%') {
                        stackListEquals.add(b / 100);
                    }
                }
            }
        }
        currentExpression = stackListEquals.removeLast().toString();
        Log.i("Результат вычисления", currentExpression);
        tvOutput.setText(currentExpression);
    }

    private String polandWriter(String preparedExpression) {
        preparedExpression = preparedExpression.replaceAll("x", "*");
        preparedExpression = preparedExpression.replaceAll("%", "/100");
        preparedExpression = preparedExpression.replaceAll("÷", "/");

        StringBuilder current = new StringBuilder(" ");
        LinkedList<Character> stackList = new LinkedList<>();
        int priority;
        for (int i = 0; i < preparedExpression.length(); i++) {
            priority = getPriority(preparedExpression.charAt(i));
            if (priority == 0) {
                current.append(preparedExpression.charAt(i));
            }
            if (priority == 1) {
                // Если скобка, то кладем в список
                stackList.add(preparedExpression.charAt(i));
            }
            if (priority > 1) {
                current.append(" "); // Пробел, чтобы символы не слипались
                while (!stackList.isEmpty()) {
                    // Берем верхний символ из стека и если приорите больше текущего символа то
                    if (getPriority(stackList.getLast()) >= priority) {
                        // Добавим элемент параллельно удаляя его из стека
                        current.append(stackList.removeLast());
                    } else break;
                }
                stackList.add(preparedExpression.charAt(i));
            }
            // Для закрывающейся скобки
            if (priority == -1) {
                current.append(" ");
                // Если встретим ( то выкидываем из стека все элменты пока не встретим )
                while (getPriority(stackList.getLast()) != 1) {
                    current.append(stackList.removeLast());
                }
                stackList.removeLast();
            }
        }
        while (!stackList.isEmpty()) {
            current.append(stackList.removeLast());
        }
        System.out.println(current);
        Log.i("Польская запись", current.toString());
        return current.toString();
    }

    private void updateText(String strToAdd) {
        String oldStr = tvInput.getText().toString();
        int cursorPos = tvInput.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if (getString(R.string.enter_the_value).equals(tvInput.getText().toString())) {
            tvInput.setText(strToAdd);
        } else {
            tvInput.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
        }
        tvInput.setSelection(cursorPos + 1);
    }

    private int getPriority(char token) {
        if (token == '*' || token == '/') {
            return 3;
        } else if (token == '+' || token == '-') {
            return 2;
        } else if (token == '(') {
            return 1;
        } else if (token == ')') {
            return -1;
        } else return 0; // Если число
    }

    private String preparingExpression(String currentExpression) {
        // Для отрицетельных значений добавим спереди нули
        StringBuilder preparedExpression = new StringBuilder(" ");
        for (int token = 0; token < currentExpression.length(); token++) {
            char symbol = currentExpression.charAt(token);
            if (symbol == '-') {
                // Если первый символ, добавим 0
                if (token == 0) {
                    preparedExpression.append('0');
                }
            }
            preparedExpression.append(symbol);
        }
        return preparedExpression.toString();
    }

    // Для сохранения значения при повороте
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // outState - просто контейнер в который можно что-то положить.
        // Необходио пометить поле как Serializable
        currentExpression = tvOutput.getText().toString();
        String displayProcess = calculatorLogic.displayInfo(currentExpression);
        outState.putSerializable(keyCalculatorLogic, displayProcess);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Каст по классу TextView. Каст значения, которое мы забрали
        currentExpression = (String) savedInstanceState.getSerializable(keyCalculatorLogic);
        tvOutput.setText(currentExpression);
    }

 /*   @Override
    protected void onStart() {
        super.onStart();
        //showMessage("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showMessage("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //showMessage("onPause");
    }

    // метод для показа всплывающих уведомлений
 *//*   private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //Log.i("Lifecycle",message); // логирование
    }*//*

    @Override
    protected void onStop() {
        super.onStop();
        //showMessage("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //showMessage("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //showMessage("onDestroy");
    }*/
}