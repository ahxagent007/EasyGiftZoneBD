package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {


    Button btn_Stock, btn_Sales, btn_Shipment, btn_others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_Stock = findViewById(R.id.btn_Stock);
        btn_Sales = findViewById(R.id.btn_Sales);
        btn_Shipment = findViewById(R.id.btn_Shipment);
        btn_others = findViewById(R.id.btn_others);


        btn_Stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Stock.class));
            }
        });

        btn_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddProduct.class));
            }
        });

        btn_Shipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Shipment.class));
            }
        });

        btn_Sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sales.class));
            }
        });

    }
}
