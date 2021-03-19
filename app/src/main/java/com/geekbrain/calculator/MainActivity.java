package com.geekbrain.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    /**
     * 1. С этого урока будем писать приложение «Калькулятор».
     * Выберите макет для работы с калькулятором. Обоснуйте,
     * почему будете использовать именно этот тип макета.
     * 2. Сверстайте главный экран калькулятора. На нём должны быть кнопки,
     * обозначающие цифры и знаки действия: «Плюс», «Умножить», «Разделить»,
     * «Вычесть» и т. п.
     * 3. * Добавьте фоновый рисунок для экрана калькулятора. Следите, чтобы рисунок был для
     * общего использования. Ресурсы: https://pxhere.com/, https://pixabay.com/ru/.
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}