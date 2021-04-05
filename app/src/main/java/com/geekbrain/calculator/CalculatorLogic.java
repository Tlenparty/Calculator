package com.geekbrain.calculator;

import java.io.Serializable;

public class CalculatorLogic implements Serializable {
    private String strMainDisplay = " ";

    public String displayInfo(String process){
        strMainDisplay = process;
        return strMainDisplay;
    }

    public CalculatorLogic() {
    }

    public String getStrMainDisplay() {
        return strMainDisplay;
    }

    public void setStrMainDisplay(String strMainDisplay) {
        this.strMainDisplay = strMainDisplay;
    }

}
