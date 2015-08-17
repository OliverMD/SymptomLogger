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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.odownard.symptomlogger.DataManager.DataManager;

/**
 * Created by olive_000 on 10/08/2015.
 */
public class SymptomDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.symptom_dialog_layout, null);
        final Bundle data = getArguments();
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView progressText = (TextView) view.findViewById(R.id.progress_text);
        progressText.setText("0%");
        seekBar.setMax(0);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                progressText.setText(Integer.toString(progress) + "%");
                Log.v(getTag(), "Progress: " + Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //seekBar.getScrollX();
        final TextView textView = (TextView) view.findViewById(R.id.symptom_dialog_text);
        textView.setText(data.getCharSequence("Name"));

        builder.setView(view)
        .setPositiveButton("Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp = progressText.getText().toString();
                tmp = tmp.substring(0,tmp.length()-1);
                float discomfort = Integer.parseInt(tmp);
                Log.v(getTag(), "text " + progressText.getText());
                Log.v(getTag(),"Adding new episode: discomfort -> " + Float.toString(discomfort));
                DataManager.getInstance().addEpisode(getActivity().getContentResolver(), data.getLong("Datetime"), data.getLong("ID"), discomfort);
                Toast.makeText(getActivity().getApplicationContext(), "Symptom Added!", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
