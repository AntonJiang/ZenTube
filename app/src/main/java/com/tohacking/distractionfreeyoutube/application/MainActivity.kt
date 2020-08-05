package com.tohacking.distractionfreeyoutube.application


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.enum.PageCount
import com.tohacking.distractionfreeyoutube.databinding.UiBackboneBinding
import com.tohacking.distractionfreeyoutube.repository.data.User
import com.tohacking.distractionfreeyoutube.util.loadUser
import com.tohacking.distractionfreeyoutube.util.saveUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: UiBackboneBinding
    lateinit var uiScope: CoroutineScope
    lateinit var uiJob: Job
    lateinit var viewPager: ViewPager2
    var youTubePlayer: YouTubePlayer? = null
    val youtubePlayerTracker = YouTubePlayerTracker()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun changeViewSize(view: View, increasedValue: Int) {
        val valueAnimator =
            ValueAnimator.ofInt(view.measuredHeight, increasedValue)
        valueAnimator.duration = 500L
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        valueAnimator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Starting Main Activity...")


        // Data binding for ui_backbone.xml
        binding = DataBindingUtil.setContentView(this, R.layout.ui_backbone)

        // Coroutine
        uiJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + uiJob)

        // Populate top environment menu
        // Dummy Environments
        val environments =
            arrayOf("Env1", "Env2", "Env3", "Env4")
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this
            , R.layout.environment_item
            , environments
        )
        val editTextFilledExposedDropdown: AutoCompleteTextView =
            binding.environmentMenuLayout.filledExposedDropdown
        editTextFilledExposedDropdown.setAdapter(adapter)

        // Set up bottom navigation menu
        // Default button set to history button
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        val mBottomNavigationView = binding.bottomNavigation

        // Set up view pager
        viewPager = binding.mainViewPager
        val mViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = mViewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Timber.d("onPageScrollStateChanged")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Timber.d("onPageScrolled")
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Timber.d("onPageSelected")
                when (position) {
                    PageCount.PLAYLIST_PAGE_FRAGMENT.pagePosition -> {
                        mBottomNavigationView.selectedItemId = R.id.playlist_button
                    }
                    PageCount.SEARCH_PAGE_FRAGMENT.pagePosition -> {
                        mBottomNavigationView.selectedItemId = R.id.search_button
                    }
                    PageCount.SUBS_PAGE_FRAGMENT.pagePosition -> {
                        mBottomNavigationView.selectedItemId = R.id.subscription_button
                    }
                    PageCount.PROFILE_PAGE_FRAGMENT.pagePosition -> {
                        mBottomNavigationView.selectedItemId = R.id.profile_button
                    }
                    PageCount.HISTORY_PAGE_FRAGMENT.pagePosition -> {
                        mBottomNavigationView.selectedItemId = R.id.history_button
                    }
                    else -> throw Exception("Cannot select fragment at position $position")
                }
            }
        })
        viewPager.currentItem = PageCount.HISTORY_PAGE_FRAGMENT.pagePosition

        // Set up top youtube view
        binding.topYoutubePlayer.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                this@MainActivity.youTubePlayer = youTubePlayer
                youTubePlayer.addListener(youtubePlayerTracker)
                youTubePlayer.addListener(object : YouTubePlayerListener {
                    override fun onApiChange(youTubePlayer: YouTubePlayer) {}

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                    }

                    override fun onPlaybackQualityChange(
                        youTubePlayer: YouTubePlayer,
                        playbackQuality: PlayerConstants.PlaybackQuality
                    ) {
                    }

                    override fun onPlaybackRateChange(
                        youTubePlayer: YouTubePlayer,
                        playbackRate: PlayerConstants.PlaybackRate
                    ) {
                    }

                    override fun onReady(youTubePlayer: YouTubePlayer) {}

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        when (state) {
                            PlayerConstants.PlayerState.PAUSED -> {
                                Timber.d("Top player started pausing")
                                changeViewSize(binding.topYoutubePlayer, 1)
                            }
                            PlayerConstants.PlayerState.PLAYING -> {
                                val youtubePlayer = binding.topYoutubePlayer
                                Timber.d("Top player started playing ${youtubePlayer.height}")
                                if (youtubePlayer.height == 1) {
                                    changeViewSize(
                                        youtubePlayer,
                                        binding.topYoutubePlayer.width * 9 / 16
                                    )
                                }
                            }
                            else -> {
                            }
                        }
                    }

                    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}

                    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}

                    override fun onVideoLoadedFraction(
                        youTubePlayer: YouTubePlayer,
                        loadedFraction: Float
                    ) {
                    }
                })

            }
        })

        binding.topMenuPauseButton.setOnTouchListener(TouchListener(this))
        binding.topMenuPauseButton.isClickable = true


        // Load in user
        uiScope.launch {
            application.loadUser()
        }
    }

    override fun onStop() {
        super.onStop()
        // Only save if isn't anon user
            uiScope.launch { application.saveUser() }
    }

    // Set up navigation for the main bottom menu

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_button -> {
                viewPager.currentItem = PageCount.PROFILE_PAGE_FRAGMENT.pagePosition
                true
            }
            R.id.history_button -> {
                viewPager.currentItem = PageCount.HISTORY_PAGE_FRAGMENT.pagePosition
                true
            }
            R.id.subscription_button -> {
                viewPager.currentItem = PageCount.SUBS_PAGE_FRAGMENT.pagePosition
                true
            }
            R.id.search_button -> {
                viewPager.currentItem = PageCount.SEARCH_PAGE_FRAGMENT.pagePosition
                true
            }
            R.id.playlist_button -> {
                viewPager.currentItem = PageCount.PLAYLIST_PAGE_FRAGMENT.pagePosition
                true
            }
            else -> false
        }
    }
}

class TouchListener(val activity: MainActivity) :
    OnTouchListener {

    companion object {
        const val AUTO_DRAG_UP_PERCENTAGE = 0.2
        var initHeight = 1
        var initPos = 0f
    }

    var currHeight = 0
    var params: ViewGroup.LayoutParams? = null
    private val slidingView: View = activity.binding.topYoutubePlayer

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        Timber.d("Touching top player view")
        if (params == null) {
            params = slidingView.layoutParams
            initHeight = slidingView.width / 16 * (9)
            params!!.width = slidingView.width
            params!!.height = slidingView.height
        }
        when (event.actionMasked) {
            ACTION_DOWN -> {
                Timber.d("Action down")
                currHeight = slidingView.height
                initPos = event.rawY
            }
            ACTION_MOVE -> {
                Timber.d("Action move")
                val dPos = initPos - event.rawY
                val mNewHeight = (currHeight - dPos).roundToInt()
                val newHeight =
                    if (mNewHeight < initHeight) (if (mNewHeight > 1) mNewHeight else 1) else initHeight

                Timber.v("New Height $newHeight")

                if (newHeight == 1 && activity.youTubePlayer != null) activity.youTubePlayer?.pause()
                params!!.height = newHeight
                slidingView.requestLayout() //refresh layout
            }
            ACTION_UP -> {
                val dPos = initPos - event.rawY
                var mNewHeight = (currHeight - dPos).roundToInt()
                mNewHeight =
                    if (mNewHeight < initHeight) (if (mNewHeight > 1) mNewHeight else 1) else initHeight
                val newHeight =
                    if (mNewHeight > initHeight * AUTO_DRAG_UP_PERCENTAGE) mNewHeight else 1

                if (newHeight == 1 && activity.youTubePlayer != null) activity.youTubePlayer?.pause()
                params!!.height = newHeight
                slidingView.requestLayout()
            }
        }
        return false
    }
}