package com.metroeger.costtrackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.metroeger.costtrackerapp.model.CategoryItem;

public class CategoryItemActivity extends AppCompatActivity {

    private Intent intent;
    private CategoryItem categoryItem;
    private EditText etCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);

        etCategoryName = findViewById(R.id.etCategoryName);
        intent = getIntent();
        categoryItem = (CategoryItem) intent.getSerializableExtra("ci");
        if (categoryItem!=null){
            etCategoryName.setText(categoryItem.getName());
        }
    }

    public void cancelCategory(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void saveCategory(View view) {
        if (categoryItem==null){
            categoryItem=new CategoryItem();
        }
        categoryItem.setName(etCategoryName.getText().toString());

        intent.putExtra("ci", categoryItem);
        setResult(RESULT_OK,intent);
        finish();
    }
}
