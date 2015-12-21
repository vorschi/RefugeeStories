package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.viewpagerindicator.LinePageIndicator;

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 5.12.2015.
 */
public class TimelineAdapter extends BaseAdapter {

    private final Context context;
    private List<Story> stories = Collections.<Story>emptyList();

    public TimelineAdapter(Context context) {
        this.context = context;
    }

    public void updateStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Story getItem(int position) {
        return stories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView txtTimelineItemTitle;
        TextView txtTimelineItemDetails;
        TextView txtTimelineItemText;
        ViewPager viewPager;
        LinePageIndicator indicator;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.timeline_item, parent, false);
            txtTimelineItemTitle = (TextView) convertView.findViewById(R.id.timeline_item_title);
            txtTimelineItemDetails = (TextView) convertView.findViewById(R.id.timeline_item_details);
            txtTimelineItemText = (TextView) convertView.findViewById(R.id.timeline_item_text);
            viewPager = (ViewPager) convertView.findViewById(R.id.timeline_view_pager);
            indicator = (LinePageIndicator) convertView.findViewById(R.id.indicator);
            convertView.setTag(new ViewHolder(txtTimelineItemTitle, txtTimelineItemDetails, txtTimelineItemText, viewPager, indicator));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            txtTimelineItemTitle = viewHolder.txtTimelineItemTitle;
            txtTimelineItemDetails = viewHolder.txtTimelineItemDetails;
            txtTimelineItemText = viewHolder.txtTimelineItemText;
            viewPager = viewHolder.viewPager;
            indicator = viewHolder.indicator;
        }

        Story story = (Story) getItem(position);
        txtTimelineItemTitle.setText(story.getTitle());
        txtTimelineItemDetails.setText(Utils.dateFormat.format(stories.get(position).getDate()) + ", " + stories.get(position).getLocation());
        txtTimelineItemText.setText(story.getText());

        Log.e(this.getClass().getSimpleName(), "StoryId: " + story.getId() + ", Images: " + story.getImages().toString());
        if (story.getImages().isEmpty()) {
            viewPager.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.VISIBLE);

            ImageAdapter imageAdapter = new ImageAdapter(context);
            imageAdapter.updateImages(story.getImages());
            viewPager.setAdapter(imageAdapter);
            indicator.setViewPager(viewPager, 0);
            indicator.invalidate();
        }

        return convertView;
    }

    private static class ViewHolder {

        public final TextView txtTimelineItemTitle;
        public final TextView txtTimelineItemDetails;
        public final TextView txtTimelineItemText;
        public final ViewPager viewPager;
        public final LinePageIndicator indicator;

        public ViewHolder(TextView txtTimelineItemTitle, TextView txtTimelineItemDetails,
                          TextView txtTimelineItemText, ViewPager viewPager, LinePageIndicator indicator) {
            this.txtTimelineItemTitle = txtTimelineItemTitle;
            this.txtTimelineItemDetails = txtTimelineItemDetails;
            this.txtTimelineItemText = txtTimelineItemText;
            this.viewPager = viewPager;
            this.indicator = indicator;
        }
    }

}
