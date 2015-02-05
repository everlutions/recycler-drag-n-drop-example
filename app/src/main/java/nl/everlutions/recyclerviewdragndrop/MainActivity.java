package nl.everlutions.recyclerviewdragndrop;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity implements MyClickListener {

    public final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private MyHoverAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ArrayList<MyObject> mData;
    private int mDraggedViewPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHorizontalScrollBarEnabled(true);
        mGridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        mData = new ArrayList<MyObject>();
        for (int i = 0; i < 6; i++) {
            mData.add(new MyObject("pos " + i, i % 2 == 1 ? Color.YELLOW : Color.WHITE));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new MyHoverAdapter(this, this, mData, mItemDragListener);
        mRecyclerView.setAdapter(mAdapter);

    }

    public View.OnDragListener mItemDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int pos = mRecyclerView.getChildPosition(v);
            if (pos == mDraggedViewPos) {
                return false;
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.e(TAG, "ENTERED: " + pos);
                    if (pos > mDraggedViewPos) {
                        Collections.rotate(mData.subList(mDraggedViewPos, mData.size()), -1);
                    } else {
                        Collections.rotate(mData.subList(0, mDraggedViewPos+1), 1);
                    }
                    int oldpos = mDraggedViewPos;
                    mDraggedViewPos = pos;
                    mAdapter.notifyItemMoved(oldpos, mDraggedViewPos);
            }
            return true;
        }
    };

    public void showMsg(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v, int pos, boolean longClick) {
        mDraggedViewPos = pos;
        ClipData data = ClipData.newPlainText("myLabel","what");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.setTag(pos);
        v.startDrag(data, shadowBuilder, v, 0);
        v.setVisibility(View.INVISIBLE);
    }

    public class MyObject {
        public String name;
        public int color;

        public MyObject(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }
}
