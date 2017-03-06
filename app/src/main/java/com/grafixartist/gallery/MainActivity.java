package com.grafixartist.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    ArrayList<ImageModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load pictures / videos from device
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        String targetPath = ExternalStorageDirectoryPath + "/screenshots";
        File targetDirector = new File(targetPath);
        File[] files = targetDirector.listFiles();

        for (int i = 0; i < files.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(files[i].getName());
            imageModel.setUrl(files[i].getPath());
            data.add(imageModel);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAdapter = new GalleryAdapter(MainActivity.this, data);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                openImageDetail(position);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // register context menu the "normal" way
        registerForContextMenu(mRecyclerView);
    }

    private void openImageDetail(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putParcelableArrayListExtra("data", data);
        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();
        int position = info.getPosition();

        switch (item.getItemId()) {
            case R.id.context_open:
                openImageDetail(position);
                break;
            case R.id.context_delete:
                data.remove(position);
                mAdapter.notifyItemRemoved(position);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
