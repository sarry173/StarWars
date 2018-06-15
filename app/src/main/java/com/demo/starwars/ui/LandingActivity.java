package com.demo.starwars.ui;

import android.animation.Animator;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.demo.starwars.R;
import com.demo.starwars.adapter.PaginationAdapter;
import com.demo.starwars.base.BaseActivity;
import com.demo.starwars.databinding.ActivityLandingBinding;
import com.demo.starwars.util.PaginationAdapterCallback;
import com.demo.starwars.util.PaginationScrollListener;
import com.demo.starwars.viewmodel.CharacterViewModel;

public class LandingActivity extends BaseActivity implements PaginationAdapterCallback {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int TOTAL_PAGES = 9;
    private int currentPage = PAGE_START;
    private int revealX;
    private int revealY;


    private ActivityLandingBinding binding;
    private LayoutManagerType mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
    RecyclerView.LayoutManager mLayoutManager;
    private PaginationAdapter adapter;
    private Dialog mDialog;
    private CharacterViewModel characterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);
        showLoadAnimation(savedInstanceState);
        init();
    }

    public void init()
    {
        setUpToolBar();
        initMembers();
        initListeners();
        fetchNextPeopleData();
    }

    public void showLoadAnimation(Bundle savedInstanceState)
    {
        final Intent intent = getIntent();
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y))
        {
            binding.rootLayout.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            ViewTreeObserver viewTreeObserver = binding.rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive())
            {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout()
                    {
                        revealActivity(revealX, revealY);
                        binding.rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else
        {
            binding.rootLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUpToolBar()
    {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.screen_people));
    }

    public void initMembers()
    {

        adapter = new PaginationAdapter(LandingActivity.this);
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        binding.rvPeople.setItemAnimator(new DefaultItemAnimator());

        binding.rvPeople.setAdapter(adapter);
        binding.rvPeople.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                fetchNextPeopleData();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    public void initListeners()
    {
        binding.ivListType.setOnClickListener(view -> {

                    if (mCurrentLayoutManagerType == LayoutManagerType.GRID_LAYOUT_MANAGER)
                    {
                        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
                        binding.ivListType.setBackgroundResource(R.drawable.baseline_view_module_white_24dp);
                    }
                    else
                    {
                        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
                        binding.ivListType.setBackgroundResource(R.drawable.baseline_view_list_white_24dp);
                    }
                }
        );
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(binding.rootLayout.getWidth(), binding.rootLayout.getHeight()) * 1.1);
            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(binding.rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            binding.rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }


    public void fetchNextPeopleData()
    {
        if(currentPage==1)
        {
            binding.pbProgress.setVisibility(View.VISIBLE);
        }
        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        characterViewModel.fetchNextCharactersData(currentPage);
        characterViewModel.getAllCharacterDataObservable().observe(this, characterResponseWrapper ->{
            binding.pbProgress.setVisibility(View.GONE);
            binding.rvPeople.setVisibility(View.VISIBLE);
            if (characterResponseWrapper != null) {
                if (characterResponseWrapper.data != null)
                {
                    if(currentPage>1)
                    {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                    }
                    adapter.addAll(characterResponseWrapper.data.getResults());
                    if (currentPage < TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    }
                    else {
                        isLastPage = true;
                    }
                }
                else
                {
                    if (characterResponseWrapper.error!=null) {
                        if (currentPage > 1) {
                            adapter.showRetry(true, characterViewModel.fetchErrorMessage(characterResponseWrapper.error));
                        }
                        if (mDialog == null)
                            noNetworkDialog();
                    }
                }
            }
        });

    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if ( binding.rvPeople.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager)  binding.rvPeople.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(LandingActivity.this, 2);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(LandingActivity.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(LandingActivity.this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        binding.rvPeople.setLayoutManager(mLayoutManager);
        binding.rvPeople.scrollToPosition(scrollPosition);
    }


    @Override
    public void retryPageLoad() {
        fetchNextPeopleData();
    }

    public enum LayoutManagerType
    {
        GRID_LAYOUT_MANAGER,LINEAR_LAYOUT_MANAGER
    }


    public void noNetworkDialog()
    {
        mDialog = new Dialog(LandingActivity.this);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.network_dialog);
        mDialog.setCancelable(false);

        TextView tvHeaderTxt=mDialog.findViewById(R.id.tvHeaderTxt);
        tvHeaderTxt.setText("No Network");
        TextView tvMsg=mDialog.findViewById(R.id.tvmsg);
        tvMsg.setText(Html.fromHtml("No Network Please try Again."));
        final TextView btnTryAgain=mDialog.findViewById(R.id.btn_try_again);
        final TextView btnAccept=mDialog.findViewById(R.id.btn_exit);
        btnTryAgain.setOnClickListener(view -> {
            mDialog.dismiss();
            mDialog = null;
            adapter.showRetry(false, null);
            fetchNextPeopleData();

        });
        btnAccept.setOnClickListener (view -> {
            mDialog.dismiss();
            mDialog = null;
            finish();

        });

        mDialog.show();
    }

}
