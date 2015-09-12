package com.odownard.symptomlogger;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.R;

/**
 * Created by olive_000 on 10/08/2015.
 */
public class NewEpisodeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.symptom_dialog_layout, null);
        final Bundle data = getArguments();
        //seekBar.getScrollX();
        final TextView textView = (TextView) view.findViewById(R.id.symptom_dialog_text);
        textView.setText(data.getCharSequence("Name"));
        final ToggleButton loButton = (ToggleButton) view.findViewById(R.id.low_button);
        final ToggleButton avgButton = (ToggleButton) view.findViewById(R.id.avg_button);
        final ToggleButton hiButton = (ToggleButton) view.findViewById(R.id.hi_button);
        avgButton.setChecked(true);
        loButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    avgButton.setChecked(false);
                    hiButton.setChecked(false);
                }
            }
        });
        avgButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    loButton.setChecked(false);
                    hiButton.setChecked(false);
                }
            }
        });
        hiButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    loButton.setChecked(false);
                    avgButton.setChecked(false);
                }
            }
        });


        builder.setView(view)
                .setPositiveButton("Record", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float discomfort = 33f;
                        if (avgButton.isChecked()) discomfort = 66f;
                        else discomfort = 99f;
                        Log.v(getTag(),"Adding new episode: discomfort -> "
                                + Float.toString(discomfort));
                        DataManager.getInstance().addEpisode(getActivity().getContentResolver()
                                , data.getLong("Datetime"), data.getLong("ID"), discomfort);
                        Toast.makeText(getActivity().getApplicationContext(), "Symptom Added!"
                                , Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}