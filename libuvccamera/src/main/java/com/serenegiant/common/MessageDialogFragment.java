package com.serenegiant.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog.Builder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.serenegiant.utils.BuildCheck;

public class MessageDialogFragment extends DialogFragment {
    private static final String TAG = MessageDialogFragment.class.getSimpleName();
    private MessageDialogFragment.MessageDialogListener mDialogListener;

    public static MessageDialogFragment showDialog(AppCompatActivity parent, int requestCode, int id_title, int id_message, String[] permissions) {
        MessageDialogFragment dialog = newInstance(requestCode, id_title, id_message, permissions);
        dialog.show(parent.getSupportFragmentManager(), TAG);
        return dialog;
    }

    public static MessageDialogFragment showDialog(Fragment parent, int requestCode, int id_title, int id_message, String[] permissions) {
        MessageDialogFragment dialog = newInstance(requestCode, id_title, id_message, permissions);
        dialog.setTargetFragment(parent, parent.getId());
        dialog.show(parent.getFragmentManager(), TAG);
        return dialog;
    }

    public static MessageDialogFragment newInstance(int requestCode, int id_title, int id_message, String[] permissions) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putInt("requestCode", requestCode);
        args.putInt("title", id_title);
        args.putInt("message", id_message);
        args.putStringArray("permissions", permissions != null ? permissions : new String[0]);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageDialogFragment() {
    }

    @SuppressLint({"NewApi"})
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MessageDialogFragment.MessageDialogListener) {
            this.mDialogListener = (MessageDialogFragment.MessageDialogListener)activity;
        }

        Fragment target;
        if (this.mDialogListener == null) {
            target = this.getTargetFragment();
            if (target instanceof MessageDialogFragment.MessageDialogListener) {
                this.mDialogListener = (MessageDialogFragment.MessageDialogListener)target;
            }
        }

        if (this.mDialogListener == null && BuildCheck.isAndroid4_2()) {
            target = this.getParentFragment();
            if (target instanceof MessageDialogFragment.MessageDialogListener) {
                this.mDialogListener = (MessageDialogFragment.MessageDialogListener)target;
            }
        }

        if (this.mDialogListener == null) {
            throw new ClassCastException(activity.toString());
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = savedInstanceState != null ? savedInstanceState : this.getArguments();
        final int requestCode = this.getArguments().getInt("requestCode");
        int id_title = this.getArguments().getInt("title");
        int id_message = this.getArguments().getInt("message");
        final String[] permissions = args.getStringArray("permissions");
        return (new Builder(this.getActivity())).setIcon(0).setTitle(id_title).setMessage(id_message).setPositiveButton(0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, requestCode, permissions, true);
                } catch (Exception var4) {
                    Log.w(MessageDialogFragment.TAG, var4);
                }

            }
        }).setNegativeButton(0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, requestCode, permissions, false);
                } catch (Exception var4) {
                    Log.w(MessageDialogFragment.TAG, var4);
                }

            }
        }).create();
    }

    public interface MessageDialogListener {
        void onMessageDialogResult(MessageDialogFragment var1, int var2, String[] var3, boolean var4);
    }
}