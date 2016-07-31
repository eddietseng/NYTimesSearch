package com.eddietseng.nytimessearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eddietseng.nytimessearch.R;
import com.eddietseng.nytimessearch.helper.DynamicHeightImageView;
import com.eddietseng.nytimessearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eddietseng on 7/27/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int IMAGE_TEXT = 0, TEXT = 1;

    private Context context;
    private List<Article> articles;

    public ArticleArrayAdapter(Context context, List<Article> articles ) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case IMAGE_TEXT:
                View view1 = inflater.inflate(R.layout.item_article_result, parent, false );
                viewHolder = new ArticleRegularHolder(view1);
                break;

            case TEXT:
                View view2 = inflater.inflate(R.layout.item_article_text, parent, false );
                viewHolder = new ArticleTextHolder(view2);
                break;

            default:
                View view3 = inflater.inflate(R.layout.item_article_text, parent, false );
                viewHolder = new ArticleTextHolder(view3);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case IMAGE_TEXT:
                ArticleRegularHolder viewHolder = (ArticleRegularHolder)holder;
                configureRegularArticleHolder(viewHolder,position);
                break;

            case TEXT:
                ArticleTextHolder textHolder = (ArticleTextHolder)holder;
                configureTextArticleHolder(textHolder,position);
                break;

            default:
                ArticleTextHolder defaultHolder = (ArticleTextHolder)holder;
                configureTextArticleHolder(defaultHolder,position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(articles.get(position).getThumbNail().length() <= 0)
            return TEXT;
        else
            return IMAGE_TEXT;
    }

    // Clean all elements of the recycler
    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    private void configureRegularArticleHolder(ArticleRegularHolder holder, int position) {
        Article article = articles.get(position);

        // clear out recycled image
        holder.ivImage.setImageResource(0);

        holder.tvTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote fetching the image in the background using Picasso

        String thumbnail = article.getThumbNail();

        if (!TextUtils.isEmpty(thumbnail)) {
            // `holder.ivImage` should be of type `DynamicHeightImageView`
            // Set the height ratio before loading in image into Picasso
            holder.ivImage.setHeightRatio((double)article.getThumbNailHeight()/article.getThumbNailWidth());
            Picasso.with(context).load(thumbnail).placeholder(R.drawable.nytimes).into(holder.ivImage);
        } else
            Picasso.with(context).load(R.drawable.nytimes).into(holder.ivImage);
    }

    private void configureTextArticleHolder(ArticleTextHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvTextTitle.setText(article.getHeadline());
    }

    static class ArticleRegularHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) DynamicHeightImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvTitle;

        public ArticleRegularHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ArticleTextHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTextTitle) TextView tvTextTitle;

        public ArticleTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
