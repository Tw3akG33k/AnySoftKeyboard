package com.anysoftkeyboard.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;

import com.menny.android.anysoftkeyboard.R;

public class GeneralDialogController {

    @VisibleForTesting
    static final int TAG_ID = R.id.progress_dialog_message_text_view;
    @VisibleForTesting
    static final String TAG_VALUE = "GeneralDialogController";

    private final Context mContext;
    private final DialogPresenter mDialogPresenter;
    private AlertDialog mDialog;

    public GeneralDialogController(Context context, JustSetupDialogPresenter dialogPresenter) {
        this(context, new NoOpImpl(dialogPresenter));
    }

    public GeneralDialogController(Context context, DialogPresenter dialogPresenter) {
        mContext = context;
        mDialogPresenter = dialogPresenter;
    }

    public boolean dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
            return true;
        }

        return false;
    }

    public void showDialog(int optionId) {
        showDialog(optionId, null);
    }

    public void showDialog(int optionId, @Nullable Object data) {
        dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Dialog_Alert);
        mDialogPresenter.onSetupDialogRequired(builder, optionId, data);
        mDialog = builder.create();
        mDialog.getWindow().getDecorView().setTag(TAG_ID, TAG_VALUE);
        mDialogPresenter.beforeDialogShown(mDialog, data);
        mDialog.show();
    }

    public interface DialogPresenter extends JustSetupDialogPresenter {
        void beforeDialogShown(@NonNull AlertDialog dialog, @Nullable Object data);
    }

    public interface JustSetupDialogPresenter {
        void onSetupDialogRequired(AlertDialog.Builder builder, int optionId, @Nullable Object data);
    }

    private static class NoOpImpl implements DialogPresenter {
        private final JustSetupDialogPresenter mDialogPresenter;

        public NoOpImpl(JustSetupDialogPresenter dialogPresenter) {
            mDialogPresenter = dialogPresenter;
        }

        @Override
        public void beforeDialogShown(@NonNull AlertDialog dialog, @Nullable Object data) {
        }

        @Override
        public void onSetupDialogRequired(AlertDialog.Builder builder, int optionId, @Nullable Object data) {
            mDialogPresenter.onSetupDialogRequired(builder, optionId, data);
        }
    }
}
