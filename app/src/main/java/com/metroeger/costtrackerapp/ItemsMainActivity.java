package com.metroeger.costtrackerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.metroeger.costtrackerapp.adapter.CategoryItemAdapter;
import com.metroeger.costtrackerapp.adapter.ItemAdapter;
import com.metroeger.costtrackerapp.db.CategoryItemDAO;
import com.metroeger.costtrackerapp.model.CategoryItem;
import com.metroeger.costtrackerapp.model.Item;

import java.util.List;

public class ItemsMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final int RQC_NEW_ITEM = 4;
    private static final int RQC_EDIT_ITEM= 5;

    private List<Item> items;
    private ItemAdapter iAdapter;
    private CategoryItemDAO dao;
    private CategoryItem ci;
    private EditText etCategoryCosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectedCategoryItemActivity.class);
                intent.putExtra("ci", ci);
                startActivityForResult(intent, RQC_NEW_ITEM);
            }
        });

            dao = new CategoryItemDAO(this);
            ci = (CategoryItem) getIntent().getSerializableExtra("ci");

            this.setTitle(ci.getName());

            items = dao.getAllItems(ci);
            iAdapter = new ItemAdapter(this, R.layout.list_item, items);
            ListView lv = findViewById(R.id.lvItems);
            etCategoryCosts = findViewById(R.id.etCategoryCosts);
            calculateCosts();
            lv.setAdapter(iAdapter);
            registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position;
        Item chosenItem = items.get(index);

        if (id==R.id.miRemoveItem){
            items.remove(chosenItem);
            dao.deleteItem(chosenItem);
            iAdapter.notifyDataSetChanged();
            calculateCosts();
            return true;

        }else if (id==R.id.miEditItem){
            Intent intent = new Intent(this,SelectedCategoryItemActivity.class);
            intent.putExtra("item", chosenItem);
            intent.putExtra("index", index);
            startActivityForResult(intent,RQC_EDIT_ITEM);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miRemoveAllItemsFromCategory) {
            dao.deleteCategoryItem(ci);
            iAdapter.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Item item = (Item) data.getSerializableExtra("item");
            if (requestCode == RQC_NEW_ITEM) {
                dao.saveItem(item);
                iAdapter.add(item);
            } else if (requestCode == RQC_EDIT_ITEM) {
                int index = data.getIntExtra("index", -1);
                if (index >= 0) {
                    dao.saveItem(item);
                    items.set(index, item);
                    iAdapter.notifyDataSetChanged();
                } else {
                    Log.e("WLAPP", "ERROR: -1 index");
                }
            }
            calculateCosts();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Intent intent = new Intent(this,MainActivity.class);
            //startActivity(intent);
    }

    public void calculateCosts(){
        double cost = 0;
        for (Item i : dao.getAllItems(ci)){
            cost += i.getAmount();
        }
        etCategoryCosts.setText(cost+"");
        ci.setAmount(cost);
        dao.saveCategoryItem(ci);
    }

}
