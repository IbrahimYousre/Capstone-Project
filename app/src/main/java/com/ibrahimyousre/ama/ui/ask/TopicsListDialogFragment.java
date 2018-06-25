package com.ibrahimyousre.ama.ui.ask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ibrahimyousre.ama.R;

public class TopicsListDialogFragment extends DialogFragment {

    public static final String KEY_TOPICS_LIST = "topics_list";

    private TopicSelectListener mTopicSelectListener;

    interface TopicSelectListener {
        void onTopicSelected(int topicIndex);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        String[] topics = getArguments().getStringArray(KEY_TOPICS_LIST);
        dialogBuilder.setTitle(R.string.select_topic)
                .setItems(topics, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTopicSelectListener.onTopicSelected(which);
                    }
                });
        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mTopicSelectListener = (TopicSelectListener) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity must implement " +
                    TopicSelectListener.class.getName());
        }
    }
}
