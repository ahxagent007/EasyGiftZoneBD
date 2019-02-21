package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShipmentDetails extends AppCompatActivity {

    ImageView IV_ShipmentPicture;
    TextView TV_ShipmentNamee, TV_ShippingDate, TV_ArivalDate, TV_ShipmentCost, TV_ShipmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_details);

        IV_ShipmentPicture = findViewById(R.id.IV_ShipmentPicture1);
        TV_ShipmentNamee = findViewById(R.id.TV_ShipmentNamee1);
        TV_ShippingDate = findViewById(R.id.TV_ShippingDate1);
        TV_ArivalDate = findViewById(R.id.TV_ArivalDate1);
        TV_ShipmentCost = findViewById(R.id.TV_ShipmentCost1);
        TV_ShipmentDetails = findViewById(R.id.TV_ShipmentDetails1);

        String arr[] = getIntent().getStringArrayExtra("Shipment");

        Picasso.get().load(arr[5]).into(IV_ShipmentPicture);
        TV_ShipmentNamee.setText("Shipment Name: "+arr[0]);
        TV_ShippingDate.setText("Shipping Date: " + arr[1]);
        TV_ArivalDate.setText("Arrival Date: "+arr[2]);
        TV_ShipmentCost.setText("TOTAL COST: "+arr[3]);
        TV_ShipmentDetails.setText("Details: "+arr[4]);

    }
}
