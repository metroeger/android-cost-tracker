package com.metroeger.costtrackerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metroeger.costtrackerapp.model.CategoryItem;
import com.metroeger.costtrackerapp.model.Item;

import java.lang.annotation.Target;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

public class SelectedCategoryItemActivity extends AppCompatActivity {


    private Intent intent;
    private Item item;
    private EditText etItem;
    private EditText etAmount;
    private CategoryItem ci;
    private TextView tvDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_item);

        tvDate = findViewById(R.id.tvDate);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SelectedCategoryItemActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.WHITE)));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("TAG","onDateSet date: " + year + "/" + month + " /" + dayOfMonth);
                String date = dayOfMonth + "/" + month+1  + "/" + year;
                tvDate.setText(date);
            }
        };

        etItem = findViewById(R.id.etItem);
        etAmount = findViewById(R.id.etAmount);

        intent = getIntent();

        ci = (CategoryItem) intent.getSerializableExtra("ci");
        item = (Item) intent.getSerializableExtra("item");

        if (item != null) {
            etItem.setText(item.getName());
            tvDate.setText(item.getDate()+"");
            etAmount.setText(item.getAmount() + "");
        }
    }

    public void saveItem(View view) {
        if (item == null) {
            item = new Item();
            item.setCategoryItem(ci);
        }

        item.setName(etItem.getText()+"");


        if (item.getDate()==null){
            item.setDate(new Date());
        }

        try{
            item.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(tvDate.getText()+""));
            item.setAmount(Double.parseDouble(etAmount.getText()+""));

        }catch(ParseException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        intent.putExtra("item", item);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void cancelItem(View view) {
        setResult(RESULT_CANCELED);
        finish();

    }

}
