package com.demo.starwars.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.starwars.BR;
import com.demo.starwars.R;
import com.demo.starwars.base.BaseActivity;
import com.demo.starwars.databinding.ActivityDetailBinding;
import com.demo.starwars.model.character.ResultsItem;

public class DetailActivity extends BaseActivity {

    private  ActivityDetailBinding binding;
    private ResultsItem resultsItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ViewCompat.setTransitionName(binding.image, "image");
        ViewCompat.setTransitionName(binding.collapsingToolbar, "title" );
        init();
    }

    public void init()
    {
        initMembers();
        initListeners();
        getData();
        setUpToolBar();
        //setScrollFromBottomAnim(this,binding.scroll);
        setData();

    }

    public void getData()
    {
        resultsItem = (ResultsItem) getIntent().getSerializableExtra("DATA");
    }
    private void setUpToolBar()
    {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(resultsItem.getName());
    }

    public void setData()
    {
        binding.setVariable(BR.cast_details,resultsItem);
        Drawable drawable;
//        drawable = getResources().getDrawable(R.drawable.star_wars_logo);
        if (resultsItem.getGender().equalsIgnoreCase("male"))
        {
            drawable = getResources().getDrawable(R.drawable.boy);
        }
        else  if (resultsItem.getGender().equalsIgnoreCase("female"))
        {
            drawable = getResources().getDrawable(R.drawable.girl);
        }
        else
        {
            drawable =getResources().getDrawable(R.drawable.user);
        }
        Glide.with(this).load("")
                .thumbnail(0.5f).placeholder(drawable)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);
    }
    public void initMembers()
    {
        resultsItem = new ResultsItem();
    }
    public void initListeners()
    {
        // init Listeners here
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public  static void setScrollFromBottomAnim(Context context, View view) {
        Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);// animation file
        view.startAnimation(zoomIn);
    }
}
