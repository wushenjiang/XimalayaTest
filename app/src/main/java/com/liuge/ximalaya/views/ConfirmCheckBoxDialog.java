package com.liuge.ximalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuge.ximalaya.R;
import com.liuge.ximalaya.utils.LogUtil;

/**
 * FileName: ConfirmDialog
 * Author: LiuGe
 * Date: 2020/8/2 22:23
 * Description: 自定义的dialog
 */
public class ConfirmCheckBoxDialog extends Dialog {

    private static final String TAG = "ConfirmCheckBoxDialog";
    private View mCancelSub;
    private View mConfirm;
    private onDialogActionClickListener mClickListener = null;
    private CheckBox mCheckBox;

    public ConfirmCheckBoxDialog(@NonNull Context context) {
        this(context,0);
    }

    public ConfirmCheckBoxDialog(@NonNull Context context, int themeResId) {
        this(context, true,null);
    }

    protected ConfirmCheckBoxDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_check_box);
        initView();
        initListener();
    }

    private void initListener() {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    boolean checked = mCheckBox.isChecked();
                    LogUtil.d(TAG,"checked -->" + checked);
                    mClickListener.onConfirmClick(checked);
                    dismiss();
                }
            }
        });
        mCancelSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onCancelSubClick();
                    dismiss();
                }
            }
        });
    }

    private void initView() {
        mCancelSub = this.findViewById(R.id.dialog_check_box_cancel);
        mConfirm = this.findViewById(R.id.dialog_check_box_confirm);
        mCheckBox = this.findViewById(R.id.dialog_check_box);
    }
    public void setonDialogActionClickListener(onDialogActionClickListener listener){
        this.mClickListener = listener;
    }
    public interface onDialogActionClickListener{
        void onCancelSubClick();

        void onConfirmClick(boolean isChecked);
    }
}
