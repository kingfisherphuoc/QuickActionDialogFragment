# QuickActionDialogFragment
After longtime searching in GG to find a way to show Quick Action Dialog with up Arrow in the top of dialog, I failed!! Therefore, I create This Library to help lazy devs as me to show quick action view in a dialog fragment. Therefore, you can custom your quick action view easily even with: viewpager inside your quick action.

![alt sample](https://cloud.githubusercontent.com/assets/962484/11528816/a62c3ce6-991b-11e5-862b-fee09e89cb12.png)
![alt sample](https://cloud.githubusercontent.com/assets/962484/11551602/61918946-99ad-11e5-97ea-249d832d4208.png)
## Gradle dependency

compile 'com.kingfisherphuoc:quick-action-dialog-fragment:1.1'

## Usage
1. You need to create your own `DialogFragment` class which `extends QuickActionDialogFragment` and `override` some abstract methods
2. `setAnchorView(View)` before showing it.
3. You can change Alignment of Dialog based on position of AnchorView (like RealativeLayout) with methods: `setAligmentFlags(AlignmentFlag.ALIGN_ANCHOR_VIEW_LEFT | AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM);`
```
// Sample class
public static class MySampleDialogFragment extends QuickActionDialogFragment {

        @Override
        protected int getArrowImageViewId() {
            return R.id.ivArrow; //return 0; that mean you do not have an up arrow icon
        }
        @Override
        protected int getLayout() {
            return R.layout.dialog_sample_view;
        }
        @Override
        protected boolean isStatusBarVisible() {
            return true; //optional: if status bar is visible in your app
        }
        @Override
        protected boolean isCanceledOnTouchOutside() {
            return true; //optional 
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            // Set listener, view, data for your dialog fragment
            view.findViewById(R.id.btnSample).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Button inside Dialog!!", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }
// Sample XML:
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical">
        <ImageView
            android:id="@+id/ivArrow"
            android:layout_width="30dp"
            android:layout_height="20dp"
            android:background="@drawable/ic_up_arrow"/>
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="@android:color/black"
            android:gravity="center"
            android:orientation="vertical"
            >
            <TextView
                android:layout_width="200dp"
                android:layout_height="30dp"
                android:background="@drawable/header"
                android:text="Test TITLE"
                android:textColor="@android:color/black"/>
            <Button
                android:id="@+id/btnSample"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Show Toast"/>
        </LinearLayout>
    </LinearLayout>
</LinearLayout>
// Sample show dialog fragment
mySampleDialogFragment = new MySampleDialogFragment();
mySampleDialogFragment.setAnchorView(buttonShow);
mySampleDialogFragment.setAligmentFlags(AlignmentFlag.ALIGN_ANCHOR_VIEW_LEFT | AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM);
mySampleDialogFragment.show(getSupportFragmentManager(), null);
```
## Notice:
- You should dismiss this dialog when orientation change to avoid `anchor View` null:
```
 @Override
    protected void onSaveInstanceState(Bundle outState) {
        // must dismiss this dialog before orientation change to avoid AnchorView is deleted!
        if (mySampleDialogFragment != null && mySampleDialogFragment.isVisible()) {
            mySampleDialogFragment.dismiss();
        }
        super.onSaveInstanceState(outState);
    }
```
- The Up Arrow Icon of `ImageView` must be declared inside `LinearLayout` with `vertical` orientation (Otherwise the position of arrow will not accurate)
- The current version of Library only supports `DialogFragment` with left and top gravity. You should be careful when using it!


## Upcoming Version:
I'm really lazy man, so when I have free time or there are many requests, I will implement the below features:

1. Support toRightOfView, toLeftOfView, toTopOfView, toBottomOfView
2. Support down,left,right arrow
