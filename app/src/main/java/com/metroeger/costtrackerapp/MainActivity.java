package com.metroeger.costtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.metroeger.costtrackerapp.adapter.CategoryItemAdapter;
import com.metroeger.costtrackerapp.db.CategoryItemDAO;
import com.metroeger.costtrackerapp.model.CategoryItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int RQC_NEW_CATEGORY = 1;
    private static final int RQC_EDIT_CATEGORY = 2;
    private static final int RQC_UPDATE_ITEM = 10;
    private static double allCosts;


    private List<CategoryItem> categories;
    private CategoryItemAdapter cAdapter;
    private CategoryItemDAO dao;
    private ListView lvCategories;
    private Map<Integer, CategoryItem> ciMap;
    private EditText etAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Categories");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryItemActivity.class);
                startActivityForResult(intent, RQC_NEW_CATEGORY);
            }
        });

        dao = new CategoryItemDAO(this);
        categories = dao.getAllCategoryItems();
        ciMap = dao.getCategoryMap();

        cAdapter = new CategoryItemAdapter(this, R.layout.category_item, categories);
        lvCategories = findViewById(R.id.lvCategories);
        lvCategories.setAdapter(cAdapter);

        etAmount = findViewById(R.id.etAllCosts);
        calculateAndSetAllCosts();

        registerForContextMenu(lvCategories);
        lvCategories.setOnItemClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        // index needs to be cast
        AdapterView.AdapterContextMenuInfo positionInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = positionInfo.position;

        CategoryItem ci = categories.get(index);

        if (id == R.id.miRemove) {
            categories.remove(ci);
            dao.deleteCategoryItem(ci);
            cAdapter.notifyDataSetChanged();
            return true;

        } else if (id == R.id.miEdit) {
            Intent intent = new Intent(this, CategoryItemActivity.class);
            intent.putExtra("ci", ci);
            intent.putExtra("index", index);
            startActivityForResult(intent, RQC_EDIT_CATEGORY);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.miRemoveAll) {
            Toast.makeText(this, "Delete all categories", Toast.LENGTH_LONG).show();
            dao.deleteAllCategories();
            cAdapter.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) { // ie if save (button clicked)
            CategoryItem ci = (CategoryItem) data.getSerializableExtra("ci");
            if (requestCode == RQC_NEW_CATEGORY) {
                dao.saveCategoryItem(ci);
                cAdapter.add(ci); // adapter adds it also to categoriee
            } else if (requestCode == RQC_EDIT_CATEGORY) {
                int index = data.getIntExtra("index", -1);
                if (index >= 0) {
                    dao.saveCategoryItem(ci);
                    categories.set(index, ci);
                    cAdapter.notifyDataSetChanged();
                } else {
                    Log.e("WLAPP", "ERROR: -1 index");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ItemsMainActivity.class);
        CategoryItem ci = categories.get(position);
        intent.putExtra("ci", ci);
        Toast.makeText(this,ci.getName() + " ciItem = "+ci.getId(),Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public void calculateAndSetAllCosts() {
        double cost = 0;
        for (CategoryItem c : categories) {
            cost += c.getAmount();
        }
        etAmount.setText(cost + "");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
