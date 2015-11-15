package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        throw new UnsupportedOperationException("not ready yet!");
    }

    private static class ViewHolder {
        // XXX @ ASA TODO
    }
}
