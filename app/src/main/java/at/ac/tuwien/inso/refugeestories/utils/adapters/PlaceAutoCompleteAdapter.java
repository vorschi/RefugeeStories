package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Prediction;
import at.ac.tuwien.inso.refugeestories.utils.tasks.MyGooglePlacesApi;

/**
 * Created by Amer Salkovic on 14.1.2016.
 */

public class PlaceAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final MyGooglePlacesApi placesApi;
    private List<Prediction> predictions;

    public PlaceAutoCompleteAdapter(Context context, MyGooglePlacesApi placesApi) {
        this.context = context;
        this.placesApi = placesApi;
        predictions = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return predictions.size();
    }

    @Override
    public Prediction getItem(int position) {
        return predictions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView description;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.autocomplete_list_item, parent, false);
            description = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(new ViewHolder(description));
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            description = holder.description;
        }

        description.setText(getItem(position).getDescription());
        return convertView;
    }

    public static class ViewHolder {
        public final TextView description;

        public ViewHolder(TextView description) {
            this.description = description;
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Prediction> result = updatePredictions(constraint.toString());
                    if(result != null && !result.isEmpty()) {
                        filterResults.values = result;
                        filterResults.count = result.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    predictions = (List<Prediction>) filterResults.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<Prediction> updatePredictions(String constraint) {
        return placesApi.getPredictions(constraint);
    }

}
