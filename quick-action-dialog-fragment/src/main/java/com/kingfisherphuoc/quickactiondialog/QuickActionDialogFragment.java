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
    //    public enum Alignment {
//        SCREEN_LEFT,SREEN_RIGHT, SCREEN_TOP,SCREEN_BOTTOM,
//    }

    protected ImageView ivArrow;
    private View mAnchorView;
    // default alignment is screen align left and anchorview bottom
    private int mFlags = AlignmentFlag.ALIGN_SCREEN_LEFT | AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM;

    /**
     * Change aligment of Dialog with flags in class: {@link AlignmentFlag}. Flags can be combined together as:
     * <br><code>AligmentFlag.ALIGN_SCREEN_LEFT | AligmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM</code>
     *
     * @param flags
     * @return
     */
    public QuickActionDialogFragment setAligmentFlags(int flags) {
        if (flags != 0) {
            mFlags = flags;
        }
        return this;
    }

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
        getScreenSize();
        getAnchorViewSizeAndPosition();
        setupDialogPosition();
        setupImageIcon(view);
        return view;
    }

    // Default width height of screen: 720x1200
    private int mScreenWidth = 720;
    private int mScreenHeight = 1200;
    // anchor view size and location in screen
    private int mAnchorWidth;
    private int mAnchorHeight;
    private int[] mAnchorLocation = new int[2]; // location always has status bar height but margin dont!! be careful!!
    // gravity of dialog view
    private int mGravity = 0;

    /**
     * Setup position of dialog and its arrow
     */
    protected void setupDialogPosition() {
        // setup the dialog
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside());
//        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        p.y = 0;

        alignBaseOnFlags(p);
        Log.i(TAG, "margin top: " + p.y);
        getDialog().getWindow().setGravity(mGravity);
        getDialog().getWindow().setAttributes(p);
    }

    /**
     * Align dialog view based on flags
     *
     * @param params
     */
    private void alignBaseOnFlags(WindowManager.LayoutParams params) {
        if ((mFlags & AlignmentFlag.ALIGN_SCREEN_LEFT) == AlignmentFlag.ALIGN_SCREEN_LEFT) {
            alignScreenLeft(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_SCREEN_RIGHT) == AlignmentFlag.ALIGN_SCREEN_RIGHT) {
            alignScreenRight(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_SCREEN_TOP) == AlignmentFlag.ALIGN_SCREEN_TOP) {
            alignScreenTop(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_SCREEN_BOTTOM) == AlignmentFlag.ALIGN_SCREEN_BOTTOM) {
            alignScreenBottom(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_ANCHOR_VIEW_LEFT) == AlignmentFlag.ALIGN_ANCHOR_VIEW_LEFT) {
            alignAnchorViewLeft(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_ANCHOR_VIEW_RIGHT) == AlignmentFlag.ALIGN_ANCHOR_VIEW_RIGHT) {
            alignAnchorViewRight(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_ANCHOR_VIEW_TOP) == AlignmentFlag.ALIGN_ANCHOR_VIEW_TOP) {
            alignAnchorViewTop(params);
        }
        if ((mFlags & AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM) == AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM) {
            alignAnchorViewBottom(params);
        }
    }

    private void alignScreenLeft(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_SCREEN_LEFT");
        mGravity = mGravity | Gravity.LEFT;
        params.x = 0;
    }

    private void alignScreenRight(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_SCREEN_RIGHT");
        mGravity = mGravity | Gravity.RIGHT;
        params.x = 0;
    }

    private void alignScreenTop(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_SCREEN_TOP");
        mGravity = mGravity | Gravity.TOP;
        params.y = 0;
    }

    private void alignScreenBottom(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_SCREEN_BOTTOM");
        mGravity = mGravity | Gravity.BOTTOM;
        params.y = 0;
    }

    private void alignAnchorViewLeft(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_ANCHOR_VIEW_LEFT");
        mGravity = mGravity | Gravity.LEFT;
        params.x = mAnchorLocation[0];
    }

    private void alignAnchorViewRight(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_ANCHOR_VIEW_RIGHT");
        mGravity = mGravity | Gravity.RIGHT;
        params.x = mScreenWidth - mAnchorLocation[0] - mAnchorWidth;
    }

    private void alignAnchorViewTop(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_ANCHOR_VIEW_TOP");
        mGravity = mGravity | Gravity.TOP;
        params.y = mAnchorLocation[1];
        if (isStatusBarVisible()) {
            params.y -= getStatusBarHeight();
        }
    }

    private void alignAnchorViewBottom(WindowManager.LayoutParams params) {
        Log.i(TAG, "ALIGN_ANCHOR_VIEW_BOTTOM");
        mGravity = mGravity | Gravity.TOP;
        params.y = mAnchorLocation[1] + mAnchorHeight;
        if (isStatusBarVisible()) {
            params.y -= getStatusBarHeight();
        }
    }

    /**
     * Show Arrow Icon center of Anchor View
     */
    private void setupImageIcon(final View dialogView) {
        if (ivArrow == null) return;// just show, dont care about the position of arrow icon
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivArrow.getLayoutParams();
//        params.leftMargin = mAnchorLocation[0] + mAnchorWidth / 2;

        ivArrow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // check for compatible
                if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivArrow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    ivArrow.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

//                ivArrow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                int arrowWidth = ivArrow.getWidth();
////                LogUtils.i("Arrow Width: " + arrowWidth);
//                params.leftMargin -= arrowWidth / 2;

                // get dialog view size and location in screen
                int[] dialogLocation = new int[2];
                dialogView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int dialogWidth = dialogView.getWidth();
                dialogView.getLocationOnScreen(dialogLocation);
                // get arrow size
                ivArrow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int arrowWidth = ivArrow.getWidth();
//                Log.i(TAG, String.format("Dialog location: x = %d, y = %d, width = %d, arrowWidth = %d", dialogLocation[0], dialogLocation[1], dialogWidth, arrowWidth));

                // arrow must be inside of dialog view!!
                int maxMarginLeft = dialogWidth - arrowWidth;
                int minMarginLeft = 0;
                if (dialogLocation[0] + dialogWidth < mAnchorLocation[0]) {
                    // dialog is too far to the left of anchor view
                    params.leftMargin = maxMarginLeft;
                } else if (mAnchorLocation[0] + mAnchorWidth <= dialogLocation[0]) {
                    // dialog is too far to the right of anchor view
                    params.leftMargin = minMarginLeft;
                } else {
                    // calculate the arrow image to be showed in center of anchor view
                    int margin = mAnchorLocation[0] + mAnchorWidth / 2 - dialogLocation[0] - arrowWidth / 2;
                    if (margin < minMarginLeft) margin = minMarginLeft;
                    if (margin > maxMarginLeft) margin = maxMarginLeft;
                    params.leftMargin = margin;
                }

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

    private void getScreenSize() {
        if (getActivity() == null) {
            return;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        mScreenWidth = displaymetrics.widthPixels;
    }

    private static final String TAG = "QuickActionDialogFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // show dialog without border
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }
}
