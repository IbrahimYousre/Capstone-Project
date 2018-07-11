package com.ibrahimyousre.ama.ui.main.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ibrahimyousre.ama.R;

public class SelectImageDialogFragment extends DialogFragment {

    interface ImageSourceSelectListener {
        void onCameraSelected();

        void onGallerySelected();
    }

    public SelectImageDialogFragment() {
    }

    private ImageSourceSelectListener imageSourceSelectListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        imageSourceSelectListener = (ImageSourceSelectListener) getParentFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence[] items = new CharSequence[]{
                getString(R.string.camera),
                getString(R.string.gallery)
        };
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    imageSourceSelectListener.onCameraSelected();
                } else {
                    imageSourceSelectListener.onGallerySelected();
                }
            }
        });
        return builder.create();
    }
}