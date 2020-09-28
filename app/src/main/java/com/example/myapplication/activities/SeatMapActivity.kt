package com.example.myapplication.activities

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.alexvasilkov.gestures.views.GestureFrameLayout
import com.example.myapplication.R
import com.example.myapplication.adapters.LabelDisplayAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.models.*
import com.example.myapplication.models.SeatMap
import com.example.myapplication.utils.Constants.KEY_THEATER
import com.example.myapplication.utils.Constants.SEAT_SEPARATOR
import com.example.myapplication.viewmodels.ScheduleViewModel
import com.example.myapplication.viewmodels.SeatViewModel
import com.github.ybq.android.spinkit.style.DoubleBounce


class SeatMapActivity : BaseActivity() {
    private lateinit var mSeatMapRoot: LinearLayout
    private lateinit var mRootLayout: GestureFrameLayout
    private lateinit var mBaseImage: ImageView
    private lateinit var mBaseText: TextView
    private lateinit var mLoading : ProgressBar
    private lateinit var mTotalPrice: TextView
    private lateinit var mSeatViewModel: SeatViewModel
    private lateinit var mScheduleViewModel: ScheduleViewModel
    private lateinit var mDateOptionListAdapter: LabelDisplayAdapter<Date>
    private lateinit var mTimeOptionListAdapter: LabelDisplayAdapter<Time>
    private lateinit var mCinemaOptionListAdapter: LabelDisplayAdapter<Cinema>
    private lateinit var mSelectedSeatsListAdapter: LabelDisplayAdapter<SelectedSeat>
    private lateinit var mDateOptionList: RecyclerView
    private lateinit var mSelectedSeatsList: RecyclerView
    private lateinit var mTimeOptionList: RecyclerView
    private lateinit var mCinemaOptionList: RecyclerView
    private lateinit var mTheatreName: TextView
    private var mSeatMap: SeatMap? = null
    private var mSchedule: Schedule? = null
    private var mTimes = ArrayList<Time>()
    private var mCinemas = ArrayList<Cinema>()
    private var mSelectedSeats = ArrayList<SelectedSeat>()
    private var mCurrentPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_map)
        initViews()
        initAdapters()
        initObservers()
    }

    private fun initAdapters() {
        mTimeOptionListAdapter = LabelDisplayAdapter(this, mTimes)
        mCinemaOptionListAdapter = LabelDisplayAdapter(this, mCinemas)
        mSelectedSeatsListAdapter = LabelDisplayAdapter(this, mSelectedSeats)
    }

    private fun initDisplayFromBundle() {
        val intent = getIntent()
        if ( intent != null ) {
            mTheatreName.text = intent.getStringExtra(KEY_THEATER) ?: getString(R.string.no_title_err)
        }
    }

    override fun initDisplay() {
        initDisplayFromBundle()
        initSeatmap()
    }

    private fun initOptions() {

        if ( mSchedule != null ) {

            if ( mSchedule?.dates != null ) {

                mDateOptionListAdapter = LabelDisplayAdapter(this, mSchedule?.dates!!)
                mDateOptionListAdapter.onListInitialized = object: LabelDisplayAdapter.OnListInitialized<Date> {
                    override fun contentInitialize(item: Date): String {
                        return item.label
                    }
                    override fun backgroundInitialize(item: Date): Boolean {
                        return item.clicked
                    }
                }

                mDateOptionListAdapter.onItemClicked = object: LabelDisplayAdapter.OnItemClicked<Date> {
                    override fun onItemClick(item: Date, position: Int) {
                        if ( !item.clicked ) {
                            mScheduleViewModel.changeDate(item, position)
                            mDateOptionListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            mTimeOptionListAdapter.onListInitialized = object: LabelDisplayAdapter.OnListInitialized<Time> {
                override fun contentInitialize(item: Time): String {
                    return item.label
                }
                override fun backgroundInitialize(item: Time): Boolean {
                    return item.clicked
                }
            }

            mTimeOptionListAdapter.onItemClicked = object: LabelDisplayAdapter.OnItemClicked<Time> {
                override fun onItemClick(item: Time, position: Int) {
                    if ( !item.clicked ) {
                        mScheduleViewModel.changeTime(item, position)
                        mTimeOptionListAdapter.notifyDataSetChanged()
                    }
                }
            }


            mCinemaOptionListAdapter.onListInitialized = object: LabelDisplayAdapter.OnListInitialized<Cinema> {
                override fun contentInitialize(item: Cinema): String {
                    return item.label
                }
                override fun backgroundInitialize(item: Cinema): Boolean {
                    return item.clicked
                }

            }

            mCinemaOptionListAdapter.onItemClicked = object: LabelDisplayAdapter.OnItemClicked<Cinema> {
                override fun onItemClick(item: Cinema, position: Int) {
                    if ( !item.clicked ) {
                        mScheduleViewModel.changeCinema(item, position)
                        mCinemaOptionListAdapter.notifyDataSetChanged()
                    }
                }
            }

            mSelectedSeatsListAdapter.onListInitialized = object: LabelDisplayAdapter.OnListInitialized<SelectedSeat> {
                override fun contentInitialize(item: SelectedSeat): String {
                    return item.seat_name
                }

                override fun backgroundInitialize(item: SelectedSeat): Boolean {
                    return true
                }
            }

            createRecycler(mDateOptionList, mDateOptionListAdapter)
            createRecycler(mTimeOptionList, mTimeOptionListAdapter)
            createRecycler(mCinemaOptionList, mCinemaOptionListAdapter)
            createRecycler(mSelectedSeatsList, mSelectedSeatsListAdapter)
        }
    }
    override fun initObservers() {

        mSeatViewModel = ViewModelProviders.of(this).get(SeatViewModel::class.java)
        mScheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)

        mSeatViewModel.getData().observe(this,
            Observer<SeatMap> { seatMap ->
                mSeatMap = seatMap
                initDisplay()
            })

        mScheduleViewModel.getData().observe(this,
            Observer<Schedule> { schedule ->
                mSchedule = schedule
                initOptions()
            })

        mScheduleViewModel.getCinemas().observe(this,
            Observer<ArrayList<Cinema>> { cinemas ->
                if ( cinemas != null ) {
                    mCinemaOptionListAdapter.updateItems(cinemas)
                } else {
                    mCinemaOptionListAdapter.clear()
                }
            })

        mScheduleViewModel.getTimes().observe(this,
            Observer<ArrayList<Time>> { times ->
                if ( times != null ) {
                    mTimeOptionListAdapter.updateItems(times)
                } else {
                    mTimeOptionListAdapter.clear()
                }
            })

        mSeatViewModel.getSelectedSeats().observe(this,
            Observer<ArrayList<SelectedSeat>> { times ->
                if ( times != null ) {
                    mSelectedSeatsListAdapter.updateItems(times)
                } else {
                    mSelectedSeatsListAdapter.clear()
                }
            })

        mScheduleViewModel.getIsLoading().observe(this,
            Observer<Boolean> { loading -> setLoading(mLoading, loading) })

        mScheduleViewModel.getCurrentPrice().observe(this,
            Observer<String> { price -> mCurrentPrice = price })

        mSeatViewModel.getTotalPrice().observe(this,
            Observer<Float> { totalPrice -> mTotalPrice.text = "Php $totalPrice" })

    }

    // generate seatmap layout
    private fun initSeatmap() {
        val seatmap = mSeatMap?.seatmap
        val available_seats = mSeatMap?.info?.available_seats
        if (seatmap != null) {
            var c = 'A'
            seatmap.forEach { seatset ->
                val linearLayout = LinearLayout(this)
                linearLayout.gravity = Gravity.CENTER_HORIZONTAL
                linearLayout.addView(createSeatLabel(c, mBaseText))
                seatset.forEach { seat ->
                    val imageView = ImageView(this)
                    imageView.layoutParams = mBaseImage.layoutParams
                    val isAvailableSeat = available_seats?.contains(seat)
                    if (seat.toLowerCase() != SEAT_SEPARATOR) {
                        if (isAvailableSeat != null) {
                            if (isAvailableSeat) {
                                imageView.setBackgroundResource(R.drawable.seat_available)
                                imageView.setOnClickListener {
                                    val isSelectionSuccess = mSeatViewModel.selectSeats(mCurrentPrice, seat)
                                    if (isSelectionSuccess) {
                                        imageView.setBackgroundResource(R.drawable.seat_selected)
                                    } else {
                                        imageView.setBackgroundResource(R.drawable.seat_available)
                                    }
                                }
                            } else {
                                imageView.setBackgroundResource(R.drawable.seat_reserved)
                            }
                        }

                    }
                    linearLayout.addView(imageView)
                }
                linearLayout.addView(createSeatLabel(c, mBaseText))
                mSeatMapRoot.addView(linearLayout)
                c++
            }
        }
    }


    override fun initViews() {
        mSeatMapRoot = findViewById(R.id.seatMapRoot)
        mBaseImage = findViewById(R.id.baseImage)
        mBaseText = findViewById(R.id.baseText)
        mDateOptionList = findViewById(R.id.dateOptionList)
        mSelectedSeatsList = findViewById(R.id.selectedSeatsList)
        mCinemaOptionList = findViewById(R.id.cinemaOptionList)
        mTimeOptionList = findViewById(R.id.timeOptionList)
        mTheatreName = findViewById(R.id.theatreName)
        mTotalPrice = findViewById(R.id.totalPrice)
        mRootLayout = findViewById(R.id.rootLayout)
        mLoading = findViewById(R.id.loading)
        mLoading.indeterminateDrawable = DoubleBounce()

    }


}