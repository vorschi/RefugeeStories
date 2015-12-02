package at.ac.tuwien.inso.refugeestories.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Vorschi on 02.12.2015.
 */
public class FragmentExplore extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    public static FragmentExplore getInstance() {
        FragmentExplore f = new FragmentExplore();
        return f;
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



