package com.demo.starwars.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.starwars.R;
import com.demo.starwars.model.character.ResultsItem;
import com.demo.starwars.ui.DetailActivity;
import com.demo.starwars.util.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suresh on 6/13/2018.
 */
public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private String errorMsg;
    private List<ResultsItem> castResults;

    private Context context;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private PaginationAdapterCallback mCallback;

    public PaginationAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        castResults = new ArrayList<>();
    }

    public List<ResultsItem> getCastResults() {
        return castResults;
    }

    public void setCastResults(List<ResultsItem> castResults) {
        this.castResults = castResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.cast_list_item, parent, false);
        viewHolder = new CastVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ResultsItem resultsItem = castResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:

                final CastVH castVH = (CastVH) holder;
                castVH.tvCastName.setText(resultsItem.getName());
                Drawable drawable;
//                drawable = context.getResources().getDrawable(R.drawable.star_wars_logo);
                if (resultsItem.getGender().equalsIgnoreCase("male")) {
                    drawable = context.getResources().getDrawable(R.drawable.boy);
                } else if (resultsItem.getGender().equalsIgnoreCase("female")) {
                    drawable = context.getResources().getDrawable(R.drawable.girl);
                } else {
                    drawable = context.getResources().getDrawable(R.drawable.user);
                }
                Glide.with(context).load("")
                        .thumbnail(0.5f).placeholder(drawable)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(castVH.ivCastThumb);

                castVH.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("DATA", resultsItem);
                        final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity) context,
                                Pair.create(castVH.ivCastThumb, "image"),
                                Pair.create(castVH.tvCastName, "title"));
                        ActivityCompat.startActivityForResult((AppCompatActivity) context, intent, 666, options.toBundle());


                    }
                });

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return castResults == null ? 0 : castResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == castResults.size() - 1 && isLoadingAdded) {
            return LOADING;
        } else {
            return ITEM;
        }
    }


//    public void add(ResultsItem r) {
//        castResults.add(r);
//        notifyItemInserted(castResults.size() - 1);
//    }

    public void addAll(List<ResultsItem> moveResults) {
        for (ResultsItem result : moveResults) {
            add(result);
        }
    }

    public void remove(ResultsItem r) {
        int position = castResults.indexOf(r);
        if (position > -1) {
            castResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(ResultsItem r) {
        castResults.add(r);
        notifyItemInserted(castResults.size() - 1);
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ResultsItem());

    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = castResults.size() - 1;
        ResultsItem result = getItem(position);

        if (result != null) {
            castResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ResultsItem getItem(int position) {
        return castResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(castResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class CastVH extends RecyclerView.ViewHolder {
        TextView tvCastName;
        ImageView ivCastThumb;
        CardView cardView;

        public CastVH(View itemView) {
            super(itemView);

            tvCastName = itemView.findViewById(R.id.tv_cast_name);
            ivCastThumb = itemView.findViewById(R.id.iv_cast_thumb);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

}
