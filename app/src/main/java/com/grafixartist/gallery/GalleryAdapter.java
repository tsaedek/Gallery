package com.grafixartist.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman19 on 10/22/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private Context context;
    private List<ImageModel> data = new ArrayList<>();

     public GalleryAdapter(Context context, List<ImageModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        MyItemHolder myHolder = (MyItemHolder)holder;
        ImageModel model = data.get(position);

        Glide.with(context).load(model.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(myHolder.mImg);

        myHolder.mName.setText(model.getName());

        myHolder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public static class MyItemHolder extends RecyclerView.ViewHolder {
        View mContainer;
        ImageView mImg;
        TextView mName;

        public MyItemHolder(View itemView) {
            super(itemView);

            itemView.setLongClickable(true);
            mContainer = itemView;
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            mName = (TextView) itemView.findViewById(R.id.name);
        }
    }

}