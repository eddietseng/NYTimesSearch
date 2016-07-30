package com.eddietseng.nytimessearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eddietseng.nytimessearch.R;
import com.eddietseng.nytimessearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eddietseng on 7/27/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Article> articles;

    public ArticleArrayAdapter(Context context, List<Article> articles ) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_article_result, parent, false );

        ArticleHolder viewHolder = new ArticleHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleHolder aHolder = (ArticleHolder)holder;
        configureArticleHolder(aHolder,position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    private void configureArticleHolder(ArticleHolder holder, int position) {
        Article article = articles.get(position);

        // clear out recycled image
        holder.ivImage.setImageResource(0);

        holder.tvTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote fetching the image in the background using Picasso

        String thumbnail = article.getThumbNail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(context).load(thumbnail).placeholder(R.drawable.nytimes).into(holder.ivImage);
        } else
            Picasso.with(context).load(R.drawable.nytimes).into(holder.ivImage);
    }

    static class ArticleHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;

        public ArticleHolder(View itemView) {
            super(itemView);
            this.ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
