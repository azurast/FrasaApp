package com.labill.frasaapp.ui.write;

<<<<<<< Updated upstream
=======
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
>>>>>>> Stashed changes
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

<<<<<<< Updated upstream
=======
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
>>>>>>> Stashed changes
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
<<<<<<< Updated upstream

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labill.frasaapp.R;

public class write_second extends Fragment {

    private WriteSecondViewModel mViewModel;
=======
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.labill.frasaapp.R;

import java.io.ByteArrayOutputStream;

public class write_second extends Fragment implements View.OnDragListener {

    private WriteSecondViewModel mViewModel;
    private float xCoOrdinate, yCoOrdinate;
    public TextView prev;
    public TextView next;
    public TextView title3;
    public ImageView cover;
    public TabLayout tab;
    public TabItem stickerTab, frameTab;
    private ViewPager viewPager;
    public WriteAdapter writeAdapter;
>>>>>>> Stashed changes

    public static write_second newInstance() {
        return new write_second();
    }

<<<<<<< Updated upstream
=======
    @SuppressLint("ClickableViewAccessibility")
>>>>>>> Stashed changes
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_second, container, false);
<<<<<<< Updated upstream
        WriteFragment f1 = new WriteFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(f1);
        fragmentTransaction.commit();
=======

        final String genre  = getArguments().getString("genre");
        final String title  = getArguments().getString("title");
        String image  = getArguments().getString("image");
        Log.i("test3", image);
        Log.i("test2", genre);
        Log.i("test", title);

        prev = view.findViewById(R.id.preview);
        next = view.findViewById(R.id.next);
        cover = view.findViewById(R.id.cover);
        tab = view.findViewById(R.id.tab2);
        stickerTab = (TabItem) view.findViewById(R.id.stickers);
        frameTab = (TabItem) view.findViewById(R.id.frame);
        title3 = view.findViewById(R.id.title2);
        title3.setText(title);

        viewPager = view.findViewById(R.id.viewPager);
        writeAdapter = new WriteAdapter(getActivity().getSupportFragmentManager(), tab.getTabCount());
        viewPager.setAdapter(writeAdapter);



        tab.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tabs) {
                viewPager.setCurrentItem(tabs.getPosition());
                if(tabs.getPosition() == 0){
                    writeAdapter.notifyDataSetChanged();
                    viewPager.setVisibility(View.VISIBLE); // or View.INVISIBLE
                } else if(tabs.getPosition() == 1) {
                    writeAdapter.notifyDataSetChanged();
                    viewPager.setVisibility(View.VISIBLE); // or View.INVISIBLE
                }
            }

            @Override
            public void onTabUnselected(Tab tabs) {

            }

            @Override
            public void onTabReselected(Tab tabs) {

            }
        });
        view.findViewById(R.id.write2).setOnDragListener(this);
        view.findViewById(R.id.viewPager).setOnDragListener(this);


        if(image == "") {

        }else{
            Bitmap pic = stringToBitmap(image);
            cover.setImageBitmap(pic);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 221, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
            cover.getLayoutParams().height = height;
            cover.getLayoutParams().width = width;
            cover.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteFragment fragment = new WriteFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.write2, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        final String finalImage = image;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_mode fragment = new write_mode();
                Bundle args = new Bundle();
//                args.putInt("layout", );
                args.putString("title", title);
                args.putString("genre", genre);
                args.putString("image", finalImage);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.write2, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

>>>>>>> Stashed changes
        return view;
    }

    @Override
<<<<<<< Updated upstream
=======
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept
                    // data.

                    //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view
                    viewPager.setVisibility(View.GONE); // or View.INVISIBLE
                    // Invalidate the view to force a redraw in the new tint
                    //  view.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag acceptable view
                // Return true; the return value is ignored.


                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Re-sets the color tint to blue, if you had set the BLUE color or any color in ACTION_DRAG_STARTED. Returns true; the return value is ignored.

                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
                Toast.makeText(getActivity(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();


                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();
                v.setId(View.generateViewId());
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//remove the dragged view
                ConstraintLayout container = (ConstraintLayout) view.findViewById(R.id.write2);//caste the view into LinearLayout as our drag acceptable layout is LinearLayout

                // Get existing constraints into a ConstraintSet
                ConstraintSet constraints = new ConstraintSet();
                constraints.clone(container);

                container.addView(v);//Add the dragged view
                constraints.connect(v.getId(), ConstraintSet.TOP, container.getId(), ConstraintSet.TOP, 60);
                constraints.clear(container.getId(), ConstraintSet.TOP);
                constraints.clear(container.getId(), ConstraintSet.LEFT);
                constraints.clear(container.getId(), ConstraintSet.RIGHT);
                constraints.clear(container.getId(), ConstraintSet.BOTTOM);

                view.findViewById(v.getId()).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                xCoOrdinate = view.getX() - event.getRawX();
                                yCoOrdinate = view.getY() - event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                    // ... similarly add other constraints
                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting

                // Invalidates the view to force a redraw
                view.invalidate();

                // Does a getResult(), and displays what happened.

                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }


    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
>>>>>>> Stashed changes
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WriteSecondViewModel.class);
        // TODO: Use the ViewModel
    }

}
