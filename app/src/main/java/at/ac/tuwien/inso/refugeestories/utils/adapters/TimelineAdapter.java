package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 5.12.2015.
 */
public class TimelineAdapter extends BaseAdapter {

    private int[] testImages = { R.drawable.tutanchamun, R.drawable.lion, R.drawable.dog, R.drawable.woman2 };

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
    public Object getItem(int position) {
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
        ViewPager timelineCarousel;
        LinePageIndicator indicator;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.timeline_item, parent, false);
            txtTimelineItemTitle = (TextView) convertView.findViewById(R.id.timeline_item_title);
            txtTimelineItemDetails = (TextView) convertView.findViewById(R.id.timeline_item_details);
            txtTimelineItemText = (TextView) convertView.findViewById(R.id.timeline_item_text);
            timelineCarousel = (ViewPager) convertView.findViewById(R.id.timeline_carousel);
            indicator = (LinePageIndicator) convertView.findViewById(R.id.indicator);
            convertView.setTag(new ViewHolder(txtTimelineItemTitle, txtTimelineItemDetails, txtTimelineItemText, timelineCarousel, indicator));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            // TODO Amer Salkovic: reset all values maybe ...
            txtTimelineItemTitle = viewHolder.txtTimelineItemTitle;
            txtTimelineItemDetails = viewHolder.txtTimelineItemDetails;
            txtTimelineItemText = viewHolder.txtTimelineItemText;
            timelineCarousel = viewHolder.timelineCarousel;
            indicator = viewHolder.indicator;
        }

        Story story = (Story) getItem(position);
        //System.out.println("Story title: " + story.getTitle());
        txtTimelineItemTitle.setText(story.getTitle());
        txtTimelineItemDetails.setText( Utils.dtf.print(stories.get(position).getDate()) + ", " + stories.get(position).getLocation() );
        txtTimelineItemText.setText(story.getText());

        ImageAdapter imageAdapter = new ImageAdapter(context);
        List<Integer> imgs = new ArrayList<>();
        // TODO: get image for story
        //int imageResource;
        //for(Image image: story.getImages()) {
        //    imageResource = context.getResources().getIdentifier(path, null, null);
        //    imgs.add(imageResource);
        //}
        imgs.add(R.drawable.tutanchamun);
        imgs.add(R.drawable.dog);
        imgs.add(R.drawable.woman2);
        imageAdapter.updateImages(imgs);

        timelineCarousel.setAdapter(imageAdapter);
        indicator.setViewPager(timelineCarousel);

        return convertView;
    }

    private static class ViewHolder {

        //private final Context context;
        public final TextView txtTimelineItemTitle;
        public final TextView txtTimelineItemDetails;
        public final TextView txtTimelineItemText;
        public final ViewPager timelineCarousel;
        public final LinePageIndicator indicator;

        public ViewHolder(TextView txtTimelineItemTitle, TextView txtTimelineItemDetails, TextView txtTimelineItemText, ViewPager timelineCarousel, LinePageIndicator indicator) {
            //this.context = context;
            this.txtTimelineItemTitle = txtTimelineItemTitle;
            this.txtTimelineItemDetails = txtTimelineItemDetails;
            this.txtTimelineItemText = txtTimelineItemText;
            this.timelineCarousel = timelineCarousel;
            this.indicator = indicator;
        }

        public void resetValues() {
            txtTimelineItemTitle.setText("");
            txtTimelineItemDetails.setText("");
            txtTimelineItemText.setText("");
            //timelineCarousel.setAdapter(new ImageAdapter(context));
            indicator.setViewPager(timelineCarousel);
        }

    }

}
