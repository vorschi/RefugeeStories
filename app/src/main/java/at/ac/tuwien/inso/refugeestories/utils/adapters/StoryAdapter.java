package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 2.12.2015.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<Story> stories = Collections.<Story>emptyList();

    public StoryAdapter() {}

    public void updateStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ImageView imgStoryPhoto = holder.imgStoryPhoto;
        TextView txtAuthor = holder.txtAuthor;
        TextView txtInfo = holder.txtInfo;
        TextView txtStoryTitle = holder.txtStoryTitle;
        TextView txtStoryText = holder.txtStoryText;

        imgStoryPhoto.setImageResource(stories.get(position).getImgResId());
        txtAuthor.setText(stories.get(position).getAuthor());
        txtInfo.setText( Utils.dtf.print(stories.get(position).getDate()) + ", " + stories.get(position).getLocation() );
        txtStoryTitle.setText(stories.get(position).getTitle());
        txtStoryText.setText(stories.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgStoryPhoto;
        TextView txtAuthor;
        TextView txtInfo;
        TextView txtStoryTitle;
        TextView txtStoryText;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imgStoryPhoto = (ImageView) itemView.findViewById(R.id.img_story_photo);
            this.txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            this.txtInfo = (TextView) itemView.findViewById(R.id.txt_info);
            this.txtStoryTitle = (TextView) itemView.findViewById(R.id.txt_story_title);
            this.txtStoryText = (TextView) itemView.findViewById(R.id.txt_story_text);
        }
    }

    public Story getItem(int position) {
        return stories.get(position);
    }
}
