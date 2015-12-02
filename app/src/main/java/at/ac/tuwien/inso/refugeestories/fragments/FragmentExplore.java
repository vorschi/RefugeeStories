package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_explore, container, false);

        mRecyclerView = (RecyclerView) mFragmentLayout.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new StoryAdapter(MockFactory.getStories(6)));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(context, "Story: " + position + " clicked!", Toast.LENGTH_SHORT).show();
                    }
                }));

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



