package com.example.spectroom_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdaptadorTitulares extends RecyclerView.Adapter<AdaptadorTitulares.TitularesViewHolder> implements View.OnClickListener {
    private final ArrayList<Titular> datos;
    private View.OnClickListener listener;
    private DatabaseReference databaseReference;

    public AdaptadorTitulares(ArrayList<Titular> datos) {
        this.databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://spectroom-c8d5c-default-rtdb.europe-west1.firebasedatabase.app").child("Tienda");
        this.datos = datos;

        // Obtener datos de Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Titular titular = snapshot.getValue(Titular.class);
                    datos.add(titular);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("AdaptadorTitulares", "Error al obtener datos de Firebase: " + databaseError.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public TitularesViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        itemView.setOnClickListener(this);

        TitularesViewHolder tvh = new TitularesViewHolder(itemView);
        return tvh;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    public void addItem(Titular titular, int position) {
        datos.add(position, titular);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        datos.remove(position);
        notifyItemRemoved(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        Titular aux = datos.get(fromPosition);
        datos.remove(fromPosition);
        datos.add(toPosition, aux);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onBindViewHolder(TitularesViewHolder viewHolder, int pos) {
        Titular item = datos.get(pos);
        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class TitularesViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo;
        private final TextView txtSubtitulo;

        public TitularesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.lblTitulo);
            txtSubtitulo = itemView.findViewById(R.id.lblSubTitulo);
        }

        public void bindTitular(Titular t) {
            txtTitulo.setText(t.getTitulo());
            txtSubtitulo.setText(t.getSubtitulo());
        }
    }
}