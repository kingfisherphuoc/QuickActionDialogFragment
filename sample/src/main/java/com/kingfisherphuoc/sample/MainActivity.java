package com.kingfisherphuoc.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingfisherphuoc.quickactiondialog.QuickActionDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View buttonShow = findViewById(R.id.btnShow);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySampleDialogFragment = new MySampleDialogFragment();
                mySampleDialogFragment.setAnchorView(buttonShow);
                mySampleDialogFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    private MySampleDialogFragment mySampleDialogFragment;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // must dismiss this dialog before orientation change to avoid AnchorView is deleted!
        if (mySampleDialogFragment != null && mySampleDialogFragment.isVisible()) {
            mySampleDialogFragment.dismiss();
        }
        super.onSaveInstanceState(outState);
    }

    public static class MySampleDialogFragment extends QuickActionDialogFragment {

        @Override
        protected int getArrowImageViewId() {
            return R.id.ivArrow;
        }

        @Override
        protected int getLayout() {
            return R.layout.dialog_sample_view;
        }

        @Override
        protected boolean isStatusBarVisible() {
            return true;
        }


        @Override
        protected boolean isCanceledOnTouchOutside() {
            return true;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);


            return view;
        }
    }


}
