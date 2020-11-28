package com.example.memes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.memes.network.NetworkService
import com.example.memes.network.models.MemeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PanelsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_panels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkView = view.findViewById<View>(R.id.darkView)
        val spinner = view.findViewById<ProgressBar>(R.id.progressBar)

        darkView.visibility = View.VISIBLE
        spinner.visibility = View.VISIBLE

        NetworkService.dataClient.getMemes()?.enqueue(object : Callback<List<MemeData>?> {
            override fun onResponse(
                call: Call<List<MemeData>?>,
                response: Response<List<MemeData>?>
            ) {
                val errorText1 = view.findViewById<TextView>(R.id.error_text1)
                val errorText2 = view.findViewById<TextView>(R.id.error_text2)
                if (response.isSuccessful) {
                    errorText1.visibility = View.GONE
                    errorText2.visibility = View.GONE
                    val result = response.body()
                    val panels = ArrayList<Panel>()
                    result!!.forEach { data ->
                        panels.add(
                            Panel(
                                data.photoUrl!!,
                                data.description!!
                            )
                        )
                    }
                    val dataAdapter = PanelDataAdapter(view.context, panels)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
                    recyclerView.adapter = dataAdapter
                } else {
                    errorText1.visibility = View.VISIBLE
                    errorText2.visibility = View.VISIBLE
                }
                darkView.visibility = View.GONE
                spinner.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<MemeData>?>, t: Throwable) {
            }
        })
    }
}