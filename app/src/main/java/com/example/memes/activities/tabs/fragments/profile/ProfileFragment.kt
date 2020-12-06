package com.example.memes.activities.tabs.fragments.profile

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.memes.R
import com.example.memes.activities.auth.AuthActivity
import com.example.memes.activities.detailed.DetailedActivity
import com.example.memes.activities.tabs.fragments.panel.Panel
import com.example.memes.activities.tabs.fragments.panel.PanelDataAdapter
import com.example.memes.activities.tabs.fragments.profile.Consts.PROFILE_PHOTO_URL
import com.example.memes.db.DBHelper
import com.example.memes.db.Meme
import com.example.memes.network.NetworkService
import com.example.memes.network.models.ErrorResult
import com.example.memes.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileDescription: TextView
    private lateinit var moreButton: ImageView
    private lateinit var darkView: View
    private lateinit var spinner: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var window: PopupWindow
    private lateinit var dbHelper: DBHelper
    private lateinit var repository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(view.context)
        repository = UserRepository(view.context)
        profileImage = view.findViewById(R.id.profileImage)
        profileName = view.findViewById(R.id.profileName)
        profileDescription = view.findViewById(R.id.profileDescription)
        moreButton = view.findViewById(R.id.moreButton)
        moreButton = view.findViewById(R.id.moreButton)
        darkView = view.findViewById(R.id.darkView)
        spinner = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)

        Glide.with(this).load(PROFILE_PHOTO_URL).transform(CircleCrop()).into(profileImage)

        val user = repository.getUser()
        profileName.text = user?.username
        profileDescription.text = user?.userDescription

        initialLoad()

        initMoreMenu()
    }

    fun initialLoad() {
        val result = dbHelper.getLocalMemeList()
        if (result.count() > 0) {
            loadFromDb(result)
        }
    }

    private fun initMoreMenu() {
        window = PopupWindow(requireView().context, null, R.style.Theme_Memes_PopupWindow)
        val popupView = layoutInflater.inflate(R.layout.layout_popup, null)

        val aboutAppOptionButton = popupView.findViewById<Button>(R.id.aboutAppOptionButton)
        aboutAppOptionButton.setOnClickListener {
            window.dismiss()
            TODO("about")
        }

        val builder = AlertDialog.Builder(requireView().context, R.style.Theme_Memes_AlertDialog)
        builder.setMessage(R.string.logout_message)
            .setNegativeButton(
                R.string.cancel
            ) { _, _ -> }
            .setPositiveButton(
                R.string.logout
            ) { _, _ -> logout() }
        val logoutDialog = builder.create()

        val logoutOptionButton = popupView.findViewById<Button>(R.id.logoutOptionButton)
        logoutOptionButton.setOnClickListener {
            window.dismiss()
            logoutDialog.show()
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

    private fun logout() {
        NetworkService.authClient.logout().enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    repository.removeUser()
                    startActivity(Intent(requireView().context, AuthActivity::class.java))
                } else {
                    val errorResponse: ErrorResult? = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        object : TypeToken<ErrorResult>() {}.type
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

    fun dispatchTouchEvent(ev: MotionEvent) {
        if (window.isShowing) {
            val outRect = Rect()
            val location = IntArray(2)
            moreButton.getDrawingRect(outRect)
            moreButton.getLocationOnScreen(location)
            outRect.offset(location[0], location[1])
            if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                window.dismiss()
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

    private fun favoriteClickListener(v: View, panel: Panel) {
        panel.isFavorite = !panel.isFavorite
        v.isSelected = panel.isFavorite
        dbHelper.updateFavorite(panel.id, panel.isFavorite)
    }

    private fun shareClickListener(view: View, panel: Panel) {
        TODO("share")
    }

    private fun panelClickListener(view: View, panel: Panel) {
        val intent = Intent(context, DetailedActivity::class.java)
        intent.putExtra("panel", Gson().toJson(panel))
        startActivity(intent)
    }

    private fun loadFromDb(result: List<Meme>) {
        darkView.visibility = View.VISIBLE
        spinner.visibility = View.VISIBLE
        val panels = result.map { Panel(it) }
        val dataAdapter = PanelDataAdapter(
            requireView().context,
            panels,
            PanelDataAdapter.PanelDataAdapterListener(
                ::favoriteClickListener,
                ::shareClickListener,
                ::panelClickListener
            )
        )
        recyclerView.adapter = dataAdapter
        darkView.visibility = View.GONE
        spinner.visibility = View.GONE
    }
}