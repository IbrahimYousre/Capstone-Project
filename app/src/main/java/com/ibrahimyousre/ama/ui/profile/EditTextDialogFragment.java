package com.ibrahimyousre.ama.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ibrahimyousre.ama.R;

public class EditTextDialogFragment extends DialogFragment {

    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "text";

    TextInputListener mTextInputListener;

    public interface TextInputListener {
        void onTextInput(String text);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_text, null);
        final EditText editText = view.findViewById(R.id.editText);
        editText.setText(getArguments().getString(KEY_TEXT));
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString(KEY_TITLE))
                .setView(view)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editText.getText().toString();
                        mTextInputListener.onTextInput(text);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditTextDialogFragment.this.dismiss();
                    }
                });

        return dialogBuilder.create();
    }

    public void setTextInputListener(TextInputListener textInputListener) {
        mTextInputListener = textInputListener;
    }
}
