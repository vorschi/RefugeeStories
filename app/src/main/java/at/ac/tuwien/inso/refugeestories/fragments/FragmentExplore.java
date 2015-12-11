package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;

import android.graphics.Point;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.utils.RecyclerItemClickListener;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoryAdapter;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;

/**
 * Created by Vorschi, Amer Salkovic on 02.12.2015.
 */
public class FragmentExplore extends Fragment {

    private Context context;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private RelativeLayout mFragmentLayout;

    private float cardWidth;
    private float cardPadding;
    private float allPixels;

    OnStorySelectedListener mStoryCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_explore, container, false);
        mRecyclerView = (RecyclerView) mFragmentLayout.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //Display display = getActivity().getWindowManager().getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        cardWidth = getResources().getDimension(R.dimen.card_width);
        cardPadding = getResources().getDimension(R.dimen.card_padding);
        //cardPadding = (size.x - cardWidth) / 2;
        allPixels = 0;

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new StoryAdapter(MockFactory.getStories(6)));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mStoryCallback.onStorySelected(position);
                    }
                }));

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                synchronized (this) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        calculatePositionAndScroll(recyclerView);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                allPixels += dx;
            }
        });

        return mFragmentLayout;
    }

    public static FragmentExplore getInstance() {
        FragmentExplore f = new FragmentExplore();
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;

        try {
            mStoryCallback = (OnStorySelectedListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnStorySelectedListener");
        }
    }

    public interface OnStorySelectedListener {
        public void onStorySelected(int position);
    }


    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round((allPixels)/(cardWidth + cardPadding));
        if (expectedPosition < 0) {
            expectedPosition = 0;
        } else if (expectedPosition > recyclerView.getAdapter().getItemCount()) {
            expectedPosition = recyclerView.getAdapter().getItemCount();
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos;
            targetScrollPos = expectedPosition * (cardWidth + cardPadding);
        float missingPx = targetScrollPos - allPixels;
        if (missingPx != 0) {
            recyclerView.smoothScrollBy((int) missingPx, 0);
        }
    }
}
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final HorizontalScrollView scroll = (HorizontalScrollView)getView().findViewById(R.id.story_scroll_view);
        scroll.setOnScrollStopListener(new OnScrollStopListener() {

            @Override
            public void onScrollStopped) {

                //get the center
                int center = scroll.getScrollX() + scroll.getWidth() / 2;
                LinearLayout linearLayout = ((LinearLayout) scroll.findViewById(R.id.story_container_layout));
                int chilrenNum = linearLayout.getChildCount();
                for (int i = 0; i < chilrenNum; i++) {
                    View v = linearLayout.getChildAt(i);
                    if (v.getClass() != TextView.class) {
                        // you do no care about that view
                        continue;
                    }
                    int viewLeft = v.getLeft();
                    int viewWidth = v.getWidth();
                    if (center >= viewLeft && center <= viewLeft + viewWidth) {
                        Log.d("Center", "CENTER THIS : " + ((viewLeft + viewWidth / 2) - center));
                        scroll.scrollBy((viewLeft + viewWidth / 2) - center, 0);
                        break;
                    }
                }
            }

            return false;
        }
    });
*/



