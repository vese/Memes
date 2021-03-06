package com.example.memes.activities.tabs.fragments.panel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.memes.R
import com.example.memes.activities.detailed.DetailedActivity
import com.example.memes.db.DBHelper
import com.example.memes.db.Meme
import com.example.memes.network.NetworkService
import com.example.memes.network.models.MemeData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PanelsFragment : Fragment() {

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var darkView: View
    private lateinit var spinner: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorText1: TextView
    private lateinit var errorText2: TextView
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_panels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(view.context)
        refreshLayout = view.findViewById(R.id.refreshLayout)
        darkView = view.findViewById(R.id.darkView)
        spinner = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        errorText1 = view.findViewById(R.id.error_text1)
        errorText2 = view.findViewById(R.id.error_text2)

        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.active)
        refreshLayout.setColorSchemeResources(R.color.background)

        refreshLayout.setOnRefreshListener {
            refresh()
            refreshLayout.isRefreshing = false
        }

        initialLoad()
    }

    fun initialLoad() {
        val result = dbHelper.getMemeList()
        if (result.count() > 0) {
            loadFromDb(result)
        } else {
            refresh()
        }
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

    private fun refresh() {
        darkView.visibility = View.VISIBLE
        spinner.visibility = View.VISIBLE

        val handler = Handler()
        handler.postDelayed({
            NetworkService.dataClient.getMemes().enqueue(object : Callback<List<MemeData>> {
                override fun onResponse(
                    call: Call<List<MemeData>>,
                    response: Response<List<MemeData>>
                ) {
                    val panels = ArrayList<Panel>()
                    if (response.isSuccessful) {
                        errorText1.visibility = View.GONE
                        errorText2.visibility = View.GONE
                        val result = response.body()
                        result?.let {
                            result.forEach { data ->
                                panels.add(Panel(data))
                            }

                            dbHelper.insertMemes(result.map { Meme(it) })
                        }
                    } else {
                        errorText1.visibility = View.VISIBLE
                        errorText2.visibility = View.VISIBLE
                    }
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

                override fun onFailure(call: Call<List<MemeData>?>, t: Throwable) {
                }
            })
        }, this.resources.getInteger(R.integer.delay).toLong())
    }
}