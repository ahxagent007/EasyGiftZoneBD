package easygiftzonebd.com.secretdevelopersltd.dexian.easygiftzonebd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Sales extends AppCompatActivity {
    String TAG = "XIAN";

    TextView TV_SaleRecordData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        TV_SaleRecordData = findViewById(R.id.TV_SaleRecordData);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    readExcelFromWeb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void readExcelFromWeb() throws MalformedURLException, IOException {
        InputStream inputStream = new URL("https://www.bdpigeonweb.com/excel/EasyGiftZoneSalesRecord.xls").openStream();

        //Get the workbook instance for XLS stream
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row; //= sheet.createRow(0);
        HSSFCell cell;// = row.createCell(0);

        //System.out.println("Date\t\t\t\t\t\tProduct Name\t\t\t\t\t\tQuantity\t\t\t\t\t\tCPU\t\t\t\t\t\tTC\t\t\t\t\t\tSPT\t\t\t\t\t\tProfit");
        ArrayList<String> asd = new ArrayList<String>();
        int i = 0;
        boolean breakTheLoop = false;
        while(!breakTheLoop){

            String arr[] = new String [4];

            for(int j=0;j<4;j++){
                row = sheet.getRow(i);
                cell = row.getCell(j);

                try{
                    arr[j] = cell.toString();
                }catch(NullPointerException nu){
                    System.out.println(""+nu);
                    breakTheLoop = true;
                }
            }
            i++;
            Log.i(TAG,arr[0]+"\t\t\t\t\t\t"+arr[1]+"\t\t\t\t\t\t"+arr[2]+"\t\t\t\t\t\t"+arr[3]);
            asd.add(arr[0]+"\t\t\t\t\t\t"+arr[1]+"\t\t\t\t\t\t"+arr[2]+"\t\t\t\t\t\t"+arr[3]+"\n");

        }
        workbook.close();
        String asdasd = "";
        for(int k=0;k<asd.size();k++){
            asdasd+=asd.get(k);
        }

        TV_SaleRecordData.setText(asdasd);
    }


}

