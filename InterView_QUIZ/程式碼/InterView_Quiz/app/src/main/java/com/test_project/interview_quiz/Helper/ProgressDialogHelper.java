package com.test_project.interview_quiz.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class ProgressDialogHelper {
    private final Context mContext;
    private AlertDialog mAlertDialog = null;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    /**
     * 轉圈樣式
     */
    public static final int STYLE_CIRCLE = 0;
    /**
     * 橫線樣式
     */
    public static final int STYLE_LINE = 1;

    /**
     * 訊息文字大小
     * 預設值為 16
     */
    public int msgSize = 16;

    /**
     * 訊息文字顏色
     * 預設值為 0
     */
    public int msgColor = 0;

    /**
     * Progress Bar 顏色
     * 預設值為 0
     */
    public int pbColor = 0;

    public ProgressDialogHelper(Context context) {
        this.mContext = context;
    }

    /**
     * 顯示進度條提示視窗 (預設為圓形)
     *
     * @param message   訊息
     */
    public void showProgressDialog(CharSequence message) {
        showProgressDialog(STYLE_CIRCLE, message, true, -1);
    }

    /**
     * 顯示進度條提示視窗 (預設為圓形)
     *
     * @param message       訊息
     * @param cancelable    是否可取消
     */
    public void showProgressDialog(CharSequence message, boolean cancelable) {
        showProgressDialog(STYLE_CIRCLE, message, cancelable, -1);
    }

    /**
     * 顯示進度條提示視窗 (預設為圓形)
     *
     * @param message   訊息
     * @param progress  進度
     */
    public void showProgressDialog(CharSequence message, int progress) {
        showProgressDialog(STYLE_CIRCLE, message, false, progress);
    }

    /**
     * 顯示進度條提示視窗
     *
     * @param style     樣式
     * @param message   訊息
     */
    public void showProgressDialog(int style, CharSequence message) {
        showProgressDialog(style, message, true, -1);
    }

    /**
     * 顯示進度條提示視窗
     *
     * @param style         樣式
     * @param message       訊息
     * @param cancelable    是否可取消
     */
    public void showProgressDialog(int style, CharSequence message, boolean cancelable) {
        showProgressDialog(style, message, cancelable, -1);
    }

    /**
     * 顯示進度條提示視窗
     *
     * @param style     樣式
     * @param message   訊息
     * @param progress  進度
     */
    public void showProgressDialog(int style, CharSequence message, int progress) {
        showProgressDialog(style, message, false, progress);
    }

    /**
     * 顯示進度條提示視窗
     *
     * @param style         樣式
     * @param message       訊息
     * @param cancelable    是否可取消
     * @param progress      進度, -1 為不跑進度條
     */
    public void showProgressDialog(int style, CharSequence message, boolean cancelable, int progress) {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            int llPadding = 40;
            switch (style) {
                case STYLE_CIRCLE:
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setPadding(llPadding, llPadding, llPadding, llPadding);
                    linearLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params_circle = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params_circle.gravity = Gravity.CENTER;
                    linearLayout.setLayoutParams(params_circle);

                    params_circle = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    ProgressBar pb_circle = new ProgressBar(mContext);
                    pb_circle.setIndeterminate(progress == -1);
                    pb_circle.setPadding(0, 0, llPadding, 0);
                    pb_circle.setLayoutParams(params_circle);

                    params_circle = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params_circle.gravity = Gravity.CENTER;
                    mTextView = new TextView(mContext);

                    mTextView.setLayoutParams(params_circle);

                    linearLayout.addView(pb_circle);
                    linearLayout.addView(mTextView);

                    mProgressBar = pb_circle;
                    break;
                case STYLE_LINE:
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setPadding(llPadding, llPadding, llPadding, llPadding);
                    linearLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params_line.gravity = Gravity.CENTER;
                    linearLayout.setLayoutParams(params_line);

                    params_line = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    ProgressBar pb_line = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
                    pb_line.setIndeterminate(progress == -1);
                    pb_line.setPadding(0, 0, 0, 0);
                    pb_line.setLayoutParams(params_line);

                    params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params_line.gravity = Gravity.CENTER;
                    mTextView = new TextView(mContext);

                    mTextView.setPadding(0, 0, 0, llPadding);
                    mTextView.setLayoutParams(params_line);

                    linearLayout.addView(mTextView);
                    linearLayout.addView(pb_line);

                    mProgressBar = pb_line;
                    break;
                default:
                    break;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(true);
            builder.setView(linearLayout);
            mAlertDialog = builder.create();
        }

        if (progress != -1) {
            mProgressBar.setIndeterminate(false);
            mProgressBar.setProgress(progress);
        }

//        if (pbColor != 0) {
//            if (progress == -1) {
//                mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(pbColor));
//            } else {
//                mProgressBar.setProgressTintList(ColorStateList.valueOf(pbColor));
//            }
//        }

        mTextView.setTextColor(msgColor == 0 ? getThemeTextColor(mContext) : msgColor);

        if (msgSize > 0) {
            mTextView.setTextSize(msgSize);
        }
        mTextView.setText(message);
        mAlertDialog.setCancelable(cancelable);

        if (mAlertDialog != null) {
            if (!mAlertDialog.isShowing()) {
                mAlertDialog.show();
                Window window = mAlertDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(mAlertDialog.getWindow().getAttributes());
                    layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    mAlertDialog.getWindow().setAttributes(layoutParams);
                }
            }
        }

    }

    /**
     * 獲取預設文字顏色
     *
     * @param context
     * @return          顏色
     */
    private int getThemeTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        @SuppressLint("Recycle") TypedArray arr = context.obtainStyledAttributes(typedValue.data, new int[]{
                android.R.attr.textColorPrimary});
        return arr.getColor(0, -1);
    }

    /**
     * 關閉視窗
     */
    public void closeProgressDialog() {
        try {
            if (mAlertDialog != null) {
                if (mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
