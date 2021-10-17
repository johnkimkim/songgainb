package com.tistory.starcue.songgainb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView listview;
    private ListAdapter listAdapter;
    private EditText editText;
    private SearchView searchView;
    private MenuItem item;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView) findViewById(R.id.search_listview);
        registerForContextMenu(listview);
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        List<SongModel> songModels = databaseHandler.allSongList();
        if (songModels != null) {
            listview.setAdapter(new ListAdapter(getApplicationContext(), (ArrayList<SongModel>) songModels));
        }

        listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listview.setItemsCanFocus(false);
//        listAdapter.notifyDataSetChanged();

        sqLiteDatabase = databaseHandler.getWritableDatabase();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PlayActivity.class);

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.openDatabase();
                databaseHandler.getWritableDatabase();
                SongModel model = (SongModel) parent.getItemAtPosition(position);

                int index = model.get_index();
                int pos = index + 1;
                sqLiteDatabase.execSQL("update songTable set position = 1 where indexField = " + index);

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_activity_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_activity_menu));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("노래를 검색하세요!");

        searchView.requestFocusFromTouch();
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(true);

        searchView.onActionViewExpanded();

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.requestFocus();

        Log.d("TEST", "현재포커스 = " + getCurrentFocus());

        //리스너에 처리로직도구현
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchSongList(s);
                Log.d("TEST", "현재포커스 = " + getCurrentFocus());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchSongList(s);
                Log.d("TEST", "현재포커스 = " + getCurrentFocus());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchSongList(String keyword) {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        List<SongModel> songModels = databaseHandler.search(keyword);
        if (songModels != null) {
            listview.setAdapter(new ListAdapter(getApplicationContext(), (ArrayList<SongModel>) songModels));
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
