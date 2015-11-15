package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class StoriesAdapter extends BaseAdapter {

    private final Context context;
    private List<Story> stories = Collections.<Story>emptyList();;

    public StoriesAdapter(Context context, List<Story> stories) {
        this.context = context;
        this.stories = stories;
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

        TextView txtStory;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.stories_list_item, parent, false);
            txtStory = (TextView) convertView.findViewById(R.id.txt_story);
            convertView.setTag(new ViewHolder(txtStory));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            txtStory = viewHolder.txtStory;
        }

        Story story =  (Story) getItem(position);
        txtStory.setText(story.getStory());

        return convertView;
    }

    private static class ViewHolder {
        public final TextView txtStory;

        public ViewHolder(TextView txtStory) {
            this.txtStory = txtStory;
        }
    }
}
