package com.odownard.symptomlogger.TopLevelViews.Symptoms;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.R;

/**
 * Created by olive_000 on 17/08/2015.
 */
public class AddSymptomDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_symptom_dialog_layout, null);
        final TextInputLayout inputName = (TextInputLayout) view.findViewById(R.id.input_name);
        inputName.setHint("Name");
        final TextInputLayout inputDesc = (TextInputLayout) view.findViewById(R.id.input_desc);
        inputDesc.setHint("Description");
        //seekBar.getScrollX();
        final TextView textView = (TextView) view.findViewById(R.id.add_symptom_dialog_text);
        textView.setText("Add Symptom");

        builder.setView(view)
                .setPositiveButton("Record", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getEditText().getText().toString();
                        String desc = inputDesc.getEditText().getText().toString();
                        DataManager.getInstance().addSymptom(getActivity().getContentResolver(), name, desc);
                        Toast.makeText(getActivity().getApplicationContext(), "New Symptom Added!", Toast.LENGTH_SHORT).show();
                        ((SymptomListFragment) getTargetFragment()).refreshAdapter();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}