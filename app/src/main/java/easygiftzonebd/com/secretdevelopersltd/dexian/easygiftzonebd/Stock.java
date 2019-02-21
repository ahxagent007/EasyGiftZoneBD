package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Stock extends AppCompatActivity {

    String TAG = "XIAN";

    private EditText TV_searchText;
    private Button btn_search;
    private ProgressBar PB_loading;


    //RecycleView
    RecyclerView RecyclerView_StockList;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mRecycleAdapter;

    ArrayList<Product> pList;

    //FIREBASE
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        PB_loading = findViewById(R.id.PB_loading);

        //Recycle View----------------------------------------------------------------------------------
        // Calling the RecyclerView
        RecyclerView_StockList = findViewById(R.id.RecyclerView_StockList);
        RecyclerView_StockList.setHasFixedSize(true);
        RecyclerView_StockList.setItemViewCacheSize(10);
        RecyclerView_StockList.setDrawingCacheEnabled(true);
        RecyclerView_StockList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView_StockList.setLayoutManager(mLayoutManager);


        //Recycle View----------------------------------------------------------------------------------------------------

        mStorageRef = FirebaseStorage.getInstance().getReference("PRODUCTS_PICTURE");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PRODUCTS");

        pList = new ArrayList<Product>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Product p = ds.getValue(Product.class);
                    pList.add(p);
                }

                mRecycleAdapter = new HLVAdapter(getApplicationContext(), pList);
                RecyclerView_StockList.setAdapter(mRecycleAdapter);

                PB_loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                PB_loading.setVisibility(View.INVISIBLE);
            }
        });


    }


    public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

        ArrayList<Product> productList;
        Context context;

        public HLVAdapter(Context context, ArrayList<Product> pList) {
            super();
            this.context = context;
            this.productList = pList;
            Log.i(TAG,"RECYCLE VIEW COnstructor");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_product_listview, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            Log.i(TAG,i+" RECYCLE VIEW "+(productList.get(i).getProductName()));

            viewHolder.TV_productName.setText(productList.get(i).getProductName());
            viewHolder.TV_productQuantity.setText(productList.get(i).getProductStockQuantity()+" pc");
            viewHolder.TV_productPrice.setText(productList.get(i).getProductPrice()+" BDT");

            Picasso.get().load(productList.get(i).getProductPicture()).fit().centerCrop().into(viewHolder.IV_productPic);



            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {

                    viewHolder.IV_productPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowPicture(productList.get(position).getProductPicture());
                        }
                    });

                    if (isLongClick) {
                        //Toast.makeText(context, "#" + position + " (Long click)", Toast.LENGTH_SHORT).show();
                        showPopupMenu(view,position);

                    } else {
                        //Toast.makeText(context, "#" + position + " Not Long Click", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            public TextView TV_productName, TV_productBrand, TV_productID, TV_productQuantity, TV_productPrice;
            public ImageView IV_productPic;
            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                TV_productName = itemView.findViewById(R.id.TV_productName);
                TV_productQuantity = itemView.findViewById(R.id.TV_productQuantity);
                TV_productPrice = itemView.findViewById(R.id.TV_productPrice);
                IV_productPic = itemView.findViewById(R.id.IV_productPic);


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

    private void ShowPicture(String URL){

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(Stock.this);
        View myView = getLayoutInflater().inflate(R.layout.open_picture, null);

        ImageView IV_openPicture;

        IV_openPicture = myView.findViewById(R.id.IV_openPicture);

        Picasso.get().load(URL).fit().into(IV_openPicture);

        myBuilder.setView(myView);
        final AlertDialog Dialog = myBuilder.create();
        Dialog.show();

        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Dialog.getWindow().setLayout(width, height);*/ //Controlling width and height.

    }

    private void showPopupMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ProDelete:
                        //your code
                        DeleteProduct(pList.get(position));
                        Log.i(TAG,"DELETE");
                        return true;

                    case R.id.ProEdit:
                        Log.i(TAG,"EDIT");
                        //your code
                        return true;

                    case R.id.ProCost:
                        Log.i(TAG,"COST");
                        //your code
                        Toast.makeText(getApplicationContext(),"COST: "+pList.get(position).getProductCost(),Toast.LENGTH_LONG).show();
                        return true;

                    default:
                        return false;
                }

            }
        });
    }

    private void DeleteProduct(Product PPPPP){
        mDatabaseRef.child(""+PPPPP.getProductID()).removeValue();

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(PPPPP.getProductPicture());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(getApplicationContext(),"PRODUCT DELETED",Toast.LENGTH_SHORT).show();
                Log.e("firebasestorage", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.e("firebasestorage", "onFailure: did not delete file");
            }
        });

    }
}

