package com.odownard.symptomlogger.Adapters;

import android.view.View;

public interface SymptomClickListener{
    public void onDeleteClick(View symptomView);
    public void onLogClick(View symptomView);
    public void onHistoryClick(View symptomView);

}
