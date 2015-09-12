package com.odownard.symptomlogger;

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
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.odownard.symptomlogger.Adapters.CursorRecyclerAdapter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.R;

/**
 * Created by olive_000 on 17/08/2015.
 */
public class AddDialogFragment extends DialogFragment {

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
        textView.setText("Add Symptom or Tag");
        final Spinner spinner = (Spinner) view.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getActivity(),R.array.types_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        builder.setView(view)
                .setPositiveButton("Record", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getEditText().getText().toString();
                        String desc = inputDesc.getEditText().getText().toString();
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                DataManager.getInstance().addSymptom(getActivity()
                                        .getContentResolver(), name, desc);
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "New Symptom Added!", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                DataManager.getInstance().addTag(getActivity()
                                        .getContentResolver(), name, desc);
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "New Tag Added!", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        ((RecordListFragment) getTargetFragment()).refreshAdapter();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}