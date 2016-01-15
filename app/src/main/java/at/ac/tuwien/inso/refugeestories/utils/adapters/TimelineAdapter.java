package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.LinePageIndicator;

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentCreateNewStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 5.12.2015.
 */
public class TimelineAdapter extends BaseAdapter {

    private final FragmentTimeline fragmentTimeline;
    private List<Story> stories = Collections.<Story>emptyList();

    //dialog
    AlertDialog.Builder builder;
    AlertDialog deleteDialog;

    public TimelineAdapter(FragmentTimeline fragmentTimeline) {
        this.fragmentTimeline = fragmentTimeline;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageButton btnEdit;
        ImageButton btnDelete;

        TextView txtTimelineItemTitle;
        TextView txtTimelineItemDetails;
        TextView txtTimelineItemText;
        ViewPager viewPager;
        LinePageIndicator indicator;

        ImageView divider;

        if (convertView == null) {
            convertView = LayoutInflater.from(fragmentTimeline.getContext())
                    .inflate(R.layout.timeline_item, parent, false);
            btnEdit = (ImageButton) convertView.findViewById(R.id.btn_edit);
            btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);

            txtTimelineItemTitle = (TextView) convertView.findViewById(R.id.timeline_item_title);
            txtTimelineItemDetails = (TextView) convertView.findViewById(R.id.timeline_item_details);
            txtTimelineItemText = (TextView) convertView.findViewById(R.id.timeline_item_text);
            viewPager = (ViewPager) convertView.findViewById(R.id.timeline_view_pager);
            indicator = (LinePageIndicator) convertView.findViewById(R.id.indicator);
            divider = (ImageView) convertView.findViewById(R.id.timeline_divider);
            convertView.setTag(new ViewHolder(txtTimelineItemTitle, txtTimelineItemDetails, txtTimelineItemText,
                    viewPager, indicator, divider, btnEdit, btnDelete));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            btnEdit = viewHolder.btnEdit;
            btnDelete = viewHolder.btnDelete;
            txtTimelineItemTitle = viewHolder.txtTimelineItemTitle;
            txtTimelineItemDetails = viewHolder.txtTimelineItemDetails;
            txtTimelineItemText = viewHolder.txtTimelineItemText;
            viewPager = viewHolder.viewPager;
            indicator = viewHolder.indicator;
            divider = viewHolder.divider;
        }

        final Story story = (Story) getItem(position);

        if(fragmentTimeline.isMe()) {
            btnEdit.setVisibility(Button.VISIBLE);
            if(!btnEdit.hasOnClickListeners()) {
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentCreateNewStory fcns = FragmentCreateNewStory.getInstance();
                        fcns.setStory(story);
                        ((MainActivity) fragmentTimeline.getActivity()).pushFragments(fcns, true, Consts.TAB_EDIT_STORY);
                    }
                });
            }
            btnDelete.setVisibility(Button.VISIBLE);
            if(!btnDelete.hasOnClickListeners()) {
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createDeleteDialog(position);
                        deleteDialog.show();
                    }
                });
            }
        } else {
            btnEdit.setVisibility(Button.GONE);
            btnDelete.setVisibility(Button.GONE);
        }

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

            ImageAdapter imageAdapter = new ImageAdapter(fragmentTimeline.getContext());
            imageAdapter.updateImages(story.getImages());
            viewPager.setAdapter(imageAdapter);
            indicator.setViewPager(viewPager, 0);
        }

        if(position == stories.size()-1) {
            divider.setVisibility(ImageView.GONE);
        } else {
            divider.setVisibility(ImageView.VISIBLE);
        }

        return convertView;
    }

    private void createDeleteDialog(final int position) {
        builder = new AlertDialog.Builder(fragmentTimeline.getContext());
        builder.setMessage(R.string.delete_check)
                .setPositiveButton(R.string.delete_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stories.remove(position);
                        notifyDataSetChanged();
                        Toast toast = Toast.makeText(fragmentTimeline.getContext(), "The story was successfully deleted!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDialog.cancel();
                    }
                });
        deleteDialog = builder.create();
    }

    private static class ViewHolder {

        public final ImageButton btnEdit;
        public final ImageButton btnDelete;

        public final TextView txtTimelineItemTitle;
        public final TextView txtTimelineItemDetails;
        public final TextView txtTimelineItemText;
        public final ViewPager viewPager;
        public final LinePageIndicator indicator;
        public final ImageView divider;

        public ViewHolder(TextView txtTimelineItemTitle, TextView txtTimelineItemDetails,
                          TextView txtTimelineItemText, ViewPager viewPager, LinePageIndicator indicator,
                          ImageView divider, ImageButton btnEdit, ImageButton btnDelete) {
            this.btnEdit = btnEdit;
            this.btnDelete = btnDelete;

            this.txtTimelineItemTitle = txtTimelineItemTitle;
            this.txtTimelineItemDetails = txtTimelineItemDetails;
            this.txtTimelineItemText = txtTimelineItemText;
            this.viewPager = viewPager;
            this.indicator = indicator;
            this.divider = divider;
        }
    }

}
