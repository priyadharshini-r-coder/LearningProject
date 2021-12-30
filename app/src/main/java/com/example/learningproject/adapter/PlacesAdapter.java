package com.example.learningproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningproject.R;
import com.example.learningproject.model.Places;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<Places> myPlacesList;
    public PlacesAdapter(List<Places>myPlacesList){
        this.myPlacesList=myPlacesList;
    }


    @NonNull
    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from( parent.getContext())
                .inflate(R.layout.place_autocomplete_adapter,parent,false );

        return new PlacesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.ViewHolder holder, int position) {
        Places places =myPlacesList.get( position );
        holder.primaryText.setText( places.getPrimaryText() );
        holder.secText.setText( places.getAddressDescription() );
        holder.distText.setText( places.getDistance() );
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView primaryText,secText,distText;

        public ViewHolder(View itemView) {
            super( itemView );

            primaryText=(TextView) itemView.findViewById( R.id.primaryText );
            secText=(TextView) itemView.findViewById( R.id.addressDescription );
            distText=(TextView) itemView.findViewById( R.id.distance );
        }
    }
}
