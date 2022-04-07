package com.gori.acmeexplorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.models.MenuItem;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    private ArrayList<MenuItem> localDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MainMenuAdapter(ArrayList<MenuItem> dataSet) {
        localDataSet = dataSet;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView textView;

        public ViewHolder(View view){
            super(view);

            view.setOnClickListener(this);

            imageView = view.findViewById(R.id.itemMenu_image);
            textView = view.findViewById(R.id.itemMenu_text);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();

            if(getLayoutPosition()==0){
                Toast.makeText(view.getContext(), "equis", Toast.LENGTH_SHORT).show();

            }else if(getLayoutPosition()==1){
                Toast.makeText(view.getContext(), "de", Toast.LENGTH_SHORT).show();

            }
        }

        private TextView getTextView() {
            return textView;
        };

        private ImageView getImageView() {
            return imageView;
        };
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_menu_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getText());
        viewHolder.getImageView().setImageResource(localDataSet.get(position).getImageId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


}
