package com.example.memes.activities.tabs.fragments.profile

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.memes.R
import com.example.memes.activities.detailed.DetailedActivity
import com.example.memes.activities.tabs.fragments.panel.Panel
import com.example.memes.activities.tabs.fragments.panel.PanelDataAdapter
import com.example.memes.db.DBHelper
import com.example.memes.db.Meme
import com.example.memes.repository.UserRepository
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    lateinit var window: PopupWindow

    lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHelper = DBHelper(context!!)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        Glide.with(this).load(Consts.PROFILE_PHOTO_URL).transform(CircleCrop()).into(profileImage)

        val repository = UserRepository(view.context)
        val user = repository.getUser()

        val profileName = view.findViewById<TextView>(R.id.profileName)
        profileName.text = user?.username

        val profileDescription = view.findViewById<TextView>(R.id.profileDescription)
        profileDescription.text = user?.userDescription

        initialLoad()

        val moreButton = view.findViewById<ImageView>(R.id.moreButton)
        window = PopupWindow(view.context, null, R.style.Theme_Memes_PopupWindow)
        val popupView = layoutInflater.inflate(R.layout.layout_popup, null)

        val aboutAppOptionButton = popupView.findViewById<Button>(R.id.aboutAppOptionButton)
        aboutAppOptionButton.setOnClickListener {
            // TODO: 05.12.2020 onclick
        }

        val logoutOptionButton = popupView.findViewById<Button>(R.id.logoutOptionButton)
        logoutOptionButton.setOnClickListener {
            // TODO: 05.12.2020 onclick
        }

        window.contentView = popupView
        window.contentView.measure(
            makeDropDownMeasureSpec(window.width),
            makeDropDownMeasureSpec(window.height)
        )
        val offsetX = -window.contentView.measuredWidth

        moreButton.setOnClickListener {
            if (window.isShowing) {
                window.dismiss()
            } else {
                val offsetY = -moreButton.height / 2
                window.showAsDropDown(moreButton, offsetX, offsetY, Gravity.START)
            }
        }
    }

    fun initialLoad() {
        val result = dbHelper.getMemeList()
        if (result.count() > 0) {
            loadFromDb(result)
        }
    }

    fun dispatchTouchEvent(ev: MotionEvent) {
        view?.let {
            if (window.isShowing) {
                val moreButton = it.findViewById<ImageView>(R.id.moreButton)
                val outRect = Rect()
                val location = IntArray(2)
                moreButton.getDrawingRect(outRect);
                moreButton.getLocationOnScreen(location);
                outRect.offset(location[0], location[1]);
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    window.dismiss()
                }
            }
        }
    }

    private fun makeDropDownMeasureSpec(measureSpec: Int): Int {
        val mode = if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            View.MeasureSpec.UNSPECIFIED
        } else {
            View.MeasureSpec.EXACTLY
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode)
    }

    fun favoriteClickListener(v: View, panel: Panel) {
        panel.isFavorite = !panel.isFavorite
        v.isSelected = panel.isFavorite
        dbHelper.updateFavorite(panel.id, panel.isFavorite)
    }

    fun shareClickListener(view: View, panel: Panel) {
        TODO("share")
    }

    fun panelClickListener(view: View, panel: Panel) {
        val intent = Intent(context, DetailedActivity::class.java)
        intent.putExtra("panel", Gson().toJson(panel))
        startActivity(intent)
    }

    private fun loadFromDb(result: List<Meme>) {
        //val darkView = requireView().findViewById<View>(R.id.darkView)
        //val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
        //darkView.visibility = View.VISIBLE
        //spinner.visibility = View.VISIBLE
        val panels = result.map { Panel(it) }
        val dataAdapter = PanelDataAdapter(
            view!!.context,
            panels,
            PanelDataAdapter.PanelDataAdapterListener(
                ::favoriteClickListener,
                ::shareClickListener,
                ::panelClickListener
            )
        )
        val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = dataAdapter
        //darkView.visibility = View.GONE
        //spinner.visibility = View.GONE
    }
}