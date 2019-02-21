package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProduct extends AppCompatActivity {

    String TAG = "XIAN";

    EditText ET_addProdName, ET_addProdCost, ET_addProdPrice, ET_addQuantity;
    Button btn_addProdPic, btn_addStock;
    ImageView IV_AddProdPic;
    private ProgressBar PB_progress;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private Long productID;

    //FIREBASE
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ET_addProdName = findViewById(R.id.ET_addProdName);
        ET_addProdCost = findViewById(R.id.ET_addProdCost);
        ET_addProdPrice = findViewById(R.id.ET_addProdPrice);
        ET_addQuantity = findViewById(R.id.ET_addQuantity);
        btn_addProdPic = findViewById(R.id.btn_addProdPic);
        btn_addStock = findViewById(R.id.btn_addStock);
        IV_AddProdPic = findViewById(R.id.IV_AddProdPic);
        PB_progress = findViewById(R.id.PB_progress);


        mStorageRef = FirebaseStorage.getInstance().getReference("PRODUCTS_PICTURE");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PRODUCTS");


        btn_addProdPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btn_addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productID = System.currentTimeMillis();

                if(ET_addProdName.getText().toString().trim().length() > 0 && ET_addProdCost.getText().toString().trim().length() > 0
                        && ET_addProdPrice.getText().toString().trim().length() > 0 && ET_addQuantity.getText().toString().trim().length() > 0){

                    btn_addProdPic.setEnabled(false);
                    btn_addStock.setEnabled(false);
                    uploadFileToFirebase();

                }else{
                    Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(IV_AddProdPic);
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileExtention(Uri uri){

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadFileToFirebase(){
        if(mImageUri != null){

            Toast.makeText(getApplicationContext(),"Uploading Image",Toast.LENGTH_SHORT).show();
           /* try {
                Bitmap b = decodeUri(getApplicationContext(),mImageUri,100);
                mImageUri = getImageUri(getApplicationContext(),b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            final StorageReference fileRef = mStorageRef.child(productID+"."+getFileExtention(mImageUri));

            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PB_progress.setProgress(0);
                        }
                    },5000);

                    Toast.makeText(getApplicationContext(),"SUCCESSFULLY ADDED",Toast.LENGTH_LONG).show();

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String propicURL = uri.toString();
                            Log.i(TAG,"DOWNLOAD URL = "+propicURL);
                            addToFirebaseDatabase(propicURL);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"UPLOAD FAILED",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    PB_progress.setProgress((int)progress);
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"NO IMAGE SELECTED!",Toast.LENGTH_SHORT).show();
        }

    }

    private void addToFirebaseDatabase(String pic){

        String name = ET_addProdName.getText().toString();
        int cost = Integer.parseInt(ET_addProdCost.getText().toString());
        int price = Integer.parseInt(ET_addProdPrice.getText().toString());
        int quantity = Integer.parseInt(ET_addQuantity.getText().toString());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date date = new Date();
        String dddd = dateFormat.format(date);

        Product product = new Product(productID,name, cost, price, pic,dddd,quantity);

        mDatabaseRef.child(""+productID).setValue(product);

        ResetAll();

    }

    private void ResetAll(){

        ET_addProdName.setText("");
        ET_addProdName.setHint("Product Name");

        ET_addQuantity.setText("");
        ET_addQuantity.setHint("Product Quantity");

        ET_addProdPrice.setText("");
        ET_addProdPrice.setHint("Product Price");

        ET_addProdCost.setText("");
        ET_addProdCost.setHint("Product Cost");

        IV_AddProdPic.setImageDrawable(null);

        btn_addProdPic.setEnabled(true);
        btn_addStock.setEnabled(true);

    }
}

