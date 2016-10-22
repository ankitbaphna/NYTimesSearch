package assignment2.bootcamp.com.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import assignment2.bootcamp.com.nytimessearch.R;
import assignment2.bootcamp.com.nytimessearch.activities.NewsArticleActivity;
import assignment2.bootcamp.com.nytimessearch.models.Doc;
import assignment2.bootcamp.com.nytimessearch.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baphna on 10/21/2016.
 */

public class ArticleItemsAdapter extends  RecyclerView.Adapter<ArticleItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Doc> docs;

    public ArticleItemsAdapter(Context mContext, List<Doc> docs) {
        this.mContext = mContext;
        this.docs = docs;
    }

    @Override
    public ArticleItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,  parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleItemsAdapter.ViewHolder holder, int position) {
        Doc doc = docs.get(position);
        holder.tvArticleTitle.setText(doc.getHeadline().getMain());
        if(doc.getMultimedia() != null
                && doc.getMultimedia().size() != 0){
            String imageURL = String.format(Constants.MULTIMEDIA_BASE_URL, doc.getMultimedia().get(0).getUrl());
            Glide.with(mContext)
                    .load(imageURL)
                    .into(holder.ivArticleImage);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_no_image)
                    .into(holder.ivArticleImage);
        }

    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener{
        @BindView(R.id.ivArticleImage)
        ImageView ivArticleImage;

        @BindView(R.id.tvArticleTitle)
        TextView tvArticleTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            Intent articlePageIntent = new Intent(mContext, NewsArticleActivity.class);
            articlePageIntent.putExtra(Constants.ARTICLE_URL, docs.get(position).getWebUrl());
            mContext.startActivity(articlePageIntent);
        }
    }
}
