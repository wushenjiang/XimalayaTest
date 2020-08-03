package com.liuge.ximalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuge.ximalaya.R;

/**
 * FileName: ConfirmDialog
 * Author: LiuGe
 * Date: 2020/8/2 22:23
 * Description: 自定义的dialog
 */
public class ConfirmDialog extends Dialog {

    private View mCancelSub;
    private View mGiveUp;
    private onDialogActionClickListener mClickListener = null;

    public ConfirmDialog(@NonNull Context context) {
        this(context,0);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        this(context, true,null);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
        initListener();
    }

    private void initListener() {
        mGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onGiveUpClick();
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
        mCancelSub = this.findViewById(R.id.dialog_cancel_sub);
        mGiveUp = this.findViewById(R.id.dialog_give_up_tv);
    }
    public void setonDialogActionClickListener(onDialogActionClickListener listener){
        this.mClickListener = listener;
    }
    public interface onDialogActionClickListener{
        void onCancelSubClick();

        void onGiveUpClick();
    }
}
