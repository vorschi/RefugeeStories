package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class PeopleAdapter extends BaseAdapter {

    private final Context context;
    private List<Person> people = Collections.<Person>emptyList();

    public PeopleAdapter(Context context, List<Person> people) {
        this.context = context;
        this.people = people;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView photo;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.people_list_item, parent, false);

            photo = (ImageView) convertView.findViewById(R.id.img_photo);

            convertView.setTag(new ViewHolder(photo));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            photo       = viewHolder.photo;
        }

        Person person = (Person) getItem(position);

        return convertView;
    }

    private static class ViewHolder {

        private final ImageView photo;

        public ViewHolder(ImageView photo) {
            this.photo = photo;
        }
    }
}
