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
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class StoriesAdapter extends BaseAdapter {

    private final Context context;
    private List<Person> content = Collections.<Person>emptyList();;

    public StoriesAdapter(Context context, List<Person> content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView txtName;
        TextView txtTags;
        TextView txtStory;
        TextView txtDetails;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.stories_list_item, parent, false);

            txtName     = (TextView) convertView.findViewById(R.id.txt_name);
            txtTags     = (TextView) convertView.findViewById(R.id.txt_tags);
            txtStory    = (TextView) convertView.findViewById(R.id.txt_story);
            txtDetails = (TextView) convertView.findViewById(R.id.txt_details);

            convertView.setTag(new ViewHolder(txtName, txtTags, txtStory, txtDetails));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            txtName     = viewHolder.txtName;
            txtTags     = viewHolder.txtTags;
            txtStory    = viewHolder.txtStory;
            txtDetails = viewHolder.txtDetails;
        }

        Person person = (Person) getItem(position);
        txtName.setText(person.getName());
        txtTags.setText(parseTags(person.getTags()));
        txtStory.setText(person.getStory());
        txtDetails.setText("tba");

        return convertView;
    }

    private static class ViewHolder {
        public final TextView txtName;
        public final TextView txtTags;
        public final TextView txtStory;
        public final TextView txtDetails;

        public ViewHolder(TextView txtName, TextView txtTags, TextView txtStory, TextView txtDetails) {
            this.txtName = txtName;
            this.txtTags = txtTags;
            this.txtStory = txtStory;
            this.txtDetails = txtDetails;
        }
    }

    private String parseTags(List<String> tagsList) {
        String tags = "";
        boolean isFirst = true;
        for(String tag : tagsList) {
            if(isFirst) {
                tags += tag;
                isFirst = false;
            } else {
                tags += ", " + tag;
            }
        }
        return tags;
    }
}
