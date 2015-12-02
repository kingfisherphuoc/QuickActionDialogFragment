package com.kingfisherphuoc.quickactiondialog;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by kingfisher on 12/2/15.
 */
public abstract class QuickActionDialogFragment extends DialogFragment {

    protected ImageView ivArrow;
    private View mAnchorView;

    /**
     * Set anchorview to show this dialog below
     *
     * @param anchorView
     */
    public QuickActionDialogFragment setAnchorView(View anchorView) {
        this.mAnchorView = anchorView;
        return this;
    }


    /**
     * get id of arrow image view so this image can be center and below anchorview
     *
     * @return
     */
    protected abstract int getArrowImageViewId();

    /**
     * Custome layout of this dialog
     *
     * @return
     */
    protected abstract int getLayout();


    /**
     * Is this dialog will be dismissed when user touch outside?
     *
     * @return
     */
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    /**
     * Is system status bar visible when showing this dialog fragment? This will affect positioning
     * the view below anchorView
     *
     * @return
     */
    protected boolean isStatusBarVisible() {
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        if (getArrowImageViewId() != 0)
            ivArrow = (ImageView) view.findViewById(getArrowImageViewId());
        getAnchorViewSizeAndPosition();
        setupDialogPosition();
        setupImageIcon();
        return view;
    }

    private int mAnchorWidth;
    private int mAnchorHeight;
    private int[] mAnchorLocation = new int[2];

    /**
     * Setup position of dialog and its arrow
     */
    protected void setupDialogPosition() {
        // setup the dialog
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        p.y = 0;
        // margin top this dialog
        p.y += mAnchorLocation[1] + mAnchorHeight;
        if (isStatusBarVisible()) {
            p.y -= getStatusBarHeight();
        }
        Log.i(TAG, "margin top: " + p.y);
        getDialog().getWindow().setAttributes(p);
    }

    /**
     * Show Arrow Icon center of Anchor View
     */
    private void setupImageIcon() {
        if (ivArrow == null) return;// just show, dont care about the position of arrow icon
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivArrow.getLayoutParams();
        params.leftMargin = mAnchorLocation[0] + mAnchorWidth / 2;

        ivArrow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // check for compatible
                if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivArrow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    ivArrow.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                ivArrow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int arrowWidth = ivArrow.getWidth();
//                LogUtils.i("Arrow Width: " + arrowWidth);
                params.leftMargin -= arrowWidth / 2;
            }
        });

    }

    private final static int DEFAULT_STATUS_BAR = 24;

    /**
     * Get status bar height or returning default height: 24dp in pixels
     *
     * @return
     */
    private int getStatusBarHeight() {
        if (getActivity() == null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            return (int) (metrics.density * DEFAULT_STATUS_BAR);
        }
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight;
    }

    /**
     * Find anchor view size
     */
    private void getAnchorViewSizeAndPosition() {
        if (mAnchorView == null) { // throw exception to other
            throw new IllegalStateException("AnchorView not found! You must set AnchorView first");
        }
        // find anchor location 0: left, 1: top
        mAnchorView.getLocationInWindow(mAnchorLocation);
        // find anchorview width, height
        mAnchorView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mAnchorWidth = mAnchorView.getWidth();
        mAnchorHeight = mAnchorView.getHeight();
        Log.i(TAG, "Anchor Location: left: " + mAnchorLocation[0] + ", top: " + mAnchorLocation[1]);
        Log.i(TAG, "Anchor Width: " + mAnchorWidth + ", height: " + mAnchorHeight);
    }

    private static final String TAG = "QuickActionDialogFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // show dialog without border
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }
}
