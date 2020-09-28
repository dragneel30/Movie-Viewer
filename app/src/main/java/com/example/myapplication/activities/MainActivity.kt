package com.example.myapplication.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.adapters.LabelDisplayAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.models.Movie
import com.example.myapplication.utils.ActivityUtils
import com.example.myapplication.utils.Constants.KEY_THEATER
import com.example.myapplication.viewmodels.DetailsViewModel
import com.github.ybq.android.spinkit.style.DoubleBounce


class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mCastList: RecyclerView
    private lateinit var mLoading: ProgressBar
    private lateinit var mCastListAdapter: LabelDisplayAdapter<String>
    private lateinit var mRating: RatingBar
    private lateinit var mPortraitImage: ImageView
    private lateinit var mLandImage: ImageView
    private lateinit var mTitle: TextView
    private lateinit var mGenre: TextView
    private lateinit var mSynopsis: TextView
    private lateinit var mAdvisoryrRating: TextView
    private lateinit var mDuration: TextView
    private lateinit var mReleaseDate: TextView
    private lateinit var mViewSeatmapButton: Button
    private lateinit var mDetailsViewModel: DetailsViewModel
    private var mMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initObservers()
        initDisplay()

    }

    override fun initObservers() {

        mDetailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        mDetailsViewModel.getData().observe(this,
            Observer<Movie> { movie ->
                mMovie = movie
                initDisplay()
            })

        mDetailsViewModel.getIsLoading().observe(this,
            Observer<Boolean> { loading -> setLoading(mLoading, loading) })

    }



    override fun initDisplay() {

        if ( mMovie != null ) {

            mCastListAdapter = LabelDisplayAdapter(this, mMovie!!.casts)

            mCastListAdapter.onListInitialized = object: LabelDisplayAdapter.OnListInitialized<String> {
                override fun contentInitialize(item: String): String {
                    return item
                }

                override fun backgroundInitialize(item: String): Boolean {
                    return true
                }
            }

            createRecycler(mCastList, mCastListAdapter)

            Glide.with(this)
                .load(mMovie?.poster_landscape)
                .into(mLandImage)
                .onLoadFailed(resources.getDrawable(R.drawable.land_failed, null))
            Glide.with(this)
                .load(mMovie?.poster)
                .into(mPortraitImage)
                .onLoadFailed(resources.getDrawable(R.drawable.port_failed, null))

            mGenre.text = mMovie?.genre
            mTitle.text = mMovie?.canonical_title
            mSynopsis.text = mMovie?.synopsis
            mAdvisoryrRating.text = mMovie?.advisory_rating
            mDuration.text = mMovie?.runtime_mins
            mReleaseDate.text = mMovie?.release_date

        }

    }
    override fun initViews() {

        mPortraitImage = findViewById(R.id.portait_image)
        mLandImage = findViewById(R.id.land_image)
        mTitle = findViewById(R.id.title)
        mRating = findViewById(R.id.rating)
        mGenre = findViewById(R.id.genre)
        mSynopsis = findViewById(R.id.synopsis)
        mCastList = findViewById(R.id.castList)
        mAdvisoryrRating = findViewById(R.id.advisoryRating)
        mReleaseDate = findViewById(R.id.releaseDate)
        mDuration = findViewById(R.id.duration)
        mViewSeatmapButton = findViewById(R.id.viewSeatmapButton)
        mLoading = findViewById(R.id.loading)
        mViewSeatmapButton.setOnClickListener(this)
        mLoading.indeterminateDrawable = DoubleBounce()
    }

    override fun onClick(v: View) {

        when(v.id) {
            R.id.viewSeatmapButton -> {
                ActivityUtils.navigate(MainActivity@this, SeatMapActivity::class.java, bundleOf(
                    Pair(KEY_THEATER, mMovie?.theater)
                ))
            }

        }

    }
}