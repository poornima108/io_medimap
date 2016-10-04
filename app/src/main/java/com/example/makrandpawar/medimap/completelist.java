package com.example.makrandpawar.medimap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.makrandpawar.medimap.MapsActivity.medicinename;

public class completelist extends AppCompatActivity {

    static public String nomedicine="nomedicine";
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        View mview;

        public CardViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }
        public void setShopName(String shopName){
            TextView shopname=(TextView)mview.findViewById(R.id.shopnametxt);
            shopname.setText(shopName);
        }
        public void setMedicineQuantity(String medicineQuantity){
            TextView medicinequantity=(TextView)mview.findViewById(R.id.medicinequantity);
            medicinequantity.setText(medicineQuantity);
        }
        public void setShopAddress(String address){
            TextView shopaddress = (TextView) mview.findViewById(R.id.shopaddress);
            shopaddress.setText(address);
        }
        public void setShopImage(Context ctx,String shopImage){
            ImageView shopimage = (ImageView) mview.findViewById(R.id.shopimage);
            Picasso.with(ctx).load(shopImage).resize(640,640).into(shopimage);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completelist);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        final String med = intent.getStringExtra(medicinename);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medimap-fbbb5.firebaseio.com/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(med).exists()){
                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReferenceFromUrl("https://medimap-fbbb5.firebaseio.com/"+med);
                    final Query query = databaseReference1.orderByChild("value").startAt(1);

                    final FirebaseRecyclerAdapter<medicinedb,CardViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<medicinedb, CardViewHolder>(
                            medicinedb.class, R.layout.cardrow, CardViewHolder.class, query)
                    {
                        @Override
                        protected void populateViewHolder(CardViewHolder viewHolder, medicinedb model, int position) {
                            viewHolder.setShopName(model.getName());
                            viewHolder.setMedicineQuantity("Available Quantity: "+String.valueOf(model.getValue()));
                            viewHolder.setShopAddress(model.getAddress());
                            viewHolder.setShopImage(getApplicationContext(),model.getImage());
                        }
                    };
                    recyclerView.setAdapter(firebaseRecyclerAdapter);

                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseRecyclerAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    Intent intent = new Intent(completelist.this,MainActivity.class);
                    intent.putExtra(nomedicine,"error");
                    setResult(2,intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


