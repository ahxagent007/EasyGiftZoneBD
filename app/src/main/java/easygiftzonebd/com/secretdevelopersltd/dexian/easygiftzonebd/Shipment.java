package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Shipment extends AppCompatActivity {

    String TAG ="XIAN";

    RecyclerView RecyclerView_Shipment;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;

    DatabaseReference mDatabase;
    ProgressBar PB_shipmentLoading;

    private ArrayList<ShipmentClass> ShipmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment);

        RecyclerView_Shipment = findViewById(R.id.RecyclerView_Shipment);
        PB_shipmentLoading = findViewById(R.id.PB_shipmentLoading);

        mDatabase = FirebaseDatabase.getInstance().getReference("SHIPMENT");

        ShipmentList = new ArrayList<ShipmentClass>();

        RecyclerView_Shipment = findViewById(R.id.RecyclerView_Shipment);
        RecyclerView_Shipment.setHasFixedSize(true);
        RecyclerView_Shipment.setItemViewCacheSize(10);
        RecyclerView_Shipment.setDrawingCacheEnabled(true);
        RecyclerView_Shipment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView_Shipment.setLayoutManager(mLayoutManager);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ShipmentList.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ShipmentClass SC = ds.getValue(ShipmentClass.class);
                    ShipmentList.add(SC);
                }

                mRecycleAdapter = new ShipmentAdapter(getApplicationContext(), ShipmentList);
                RecyclerView_Shipment.setAdapter(mRecycleAdapter);

                PB_shipmentLoading.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ViewHolder> {

        ArrayList<ShipmentClass> ShipmentList;
        Context context;

        public ShipmentAdapter(Context context, ArrayList<ShipmentClass> sList) {
            super();
            this.context = context;
            this.ShipmentList = sList;
            Log.i(TAG,"RECYCLE VIEW Constructor");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_shipment_list, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            Log.i(TAG,i+" RECYCLE VIEW "+(ShipmentList.get(i).getShipmentName()));

            viewHolder.TV_ShipmentName.setText(ShipmentList.get(i).getShipmentName());



            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {


                    if (isLongClick) {
                        //Toast.makeText(context, "#" + position + " (Long click)", Toast.LENGTH_SHORT).show();


                    } else {
                        //Toast.makeText(context, "#" + position + " Not Long Click", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,ShipmentList.get(position).getArivalDate()+" "+ShipmentList.get(position).getShipmentName()+" "+ShipmentList.get(position).getDetails()+" "+
                                ShipmentList.get(position).getShipmentPicture()+" "+ShipmentList.get(position).getShippingDate()+" "+ShipmentList.get(position).getTotalCost());
                        //ShowShipmentDetails(ShipmentList.get(position));

                        String arr[] = {ShipmentList.get(position).getShipmentName(), ShipmentList.get(position).getShippingDate(), ShipmentList.get(position).getArivalDate(),
                                ""+ShipmentList.get(position).getTotalCost(), ShipmentList.get(position).getDetails(), ShipmentList.get(position).getShipmentPicture()};

                        Intent i = new Intent(getApplicationContext(),ShipmentDetails.class);
                        i.putExtra("Shipment",arr);
                        startActivity(i);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ShipmentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            public TextView TV_ShipmentName;
            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                TV_ShipmentName = itemView.findViewById(R.id.TV_ShipmentName);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);
            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }

    }

    public void ShowShipmentDetails(ShipmentClass shipment){

        Log.i(TAG,"ShowShipmentDetails");

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getApplicationContext());     Log.i(TAG,"ShowShipmentDetails 178");
        View myView = getLayoutInflater().inflate(R.layout.shipment_details, null);     Log.i(TAG,"ShowShipmentDetails 179");

        ImageView IV_ShipmentPicture;
        TextView TV_ShipmentNamee, TV_ShippingDate, TV_ArivalDate, TV_ShipmentCost, TV_ShipmentDetails;

        Log.i(TAG,"ShowShipmentDetails 184");

        IV_ShipmentPicture = myView.findViewById(R.id.IV_ShipmentPicture1);
        TV_ShipmentNamee = myView.findViewById(R.id.TV_ShipmentNamee1);
        TV_ShippingDate = myView.findViewById(R.id.TV_ShippingDate1);
        TV_ArivalDate = myView.findViewById(R.id.TV_ArivalDate1);
        TV_ShipmentCost = myView.findViewById(R.id.TV_ShipmentCost1);
        TV_ShipmentDetails = myView.findViewById(R.id.TV_ShipmentDetails1);

        Log.i(TAG,"ShowShipmentDetails 193");

        Picasso.get().load(shipment.getShipmentPicture()).into(IV_ShipmentPicture);
        TV_ShipmentNamee.setText("Shipemnt Name: "+shipment.getShipmentName());
        TV_ShippingDate.setText("Shipping Date: " + shipment.getShippingDate());
        TV_ArivalDate.setText("Arrival Date: "+shipment.getArivalDate());
        TV_ShipmentCost.setText(""+shipment.getTotalCost());
        TV_ShipmentDetails.setText("Details: "+shipment.getDetails());

        Log.i(TAG,"ShowShipmentDetails 202");

        myBuilder.setView(myView);Log.i(TAG,"ShowShipmentDetails 205");
        final AlertDialog Dialog = myBuilder.create();Log.i(TAG,"ShowShipmentDetails 206");
        Dialog.show();Log.i(TAG,"ShowShipmentDetails 207");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Dialog.getWindow().setLayout(width, height); //Controlling width and height.

    }
}
