package com.raman.project.openinapp.fragments

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.internal.ViewUtils.dpToPx
import com.raman.project.openinapp.R
import com.raman.project.openinapp.adapters.LinksAdapter
import com.raman.project.openinapp.databinding.FragmentLinksBinding
import com.raman.project.openinapp.models.Link
import com.raman.project.openinapp.viewmodels.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Calendar

@AndroidEntryPoint
class LinksFragment : Fragment() {

    private lateinit var binding:FragmentLinksBinding
    private lateinit var adapter:LinksAdapter
    private lateinit var recentList: List<Link>
    private lateinit var topList: List<Link>

    private val viewModel: LinkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLinksBinding.inflate(layoutInflater, container, false)


        initialize()
        adapter()
        graphSetUp()
        observer()
        clickListeners()

        return binding.root
    }

    private fun graphSetUp() {


        val xvalues = listOf("","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug")

        val xAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xvalues)
        xAxis.setLabelCount(8)
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.grey_500)


        val yAxisLeft = binding.chart.axisLeft
        yAxisLeft.axisMaximum = 100f
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.setLabelCount(5, true) // Set the number of labels on Y-axis
        yAxisLeft.axisLineWidth = 2f
        yAxisLeft.setDrawAxisLine(false)

        yAxisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.grey_500)



        binding.chart.axisRight.isEnabled = false // Disable the right-side Y-axis

        val entries: ArrayList<Entry> = ArrayList()
        entries.add(Entry(0f, 20f))
        entries.add(Entry(1f, 25f))
        entries.add(Entry(2f, 30f))
        entries.add(Entry(2f, 50f))
        entries.add(Entry(3f, 80f))
        entries.add(Entry(4f, 75f))
        entries.add(Entry(5f, 100f))
        entries.add(Entry(6f, 50f))
        entries.add(Entry(7f, 25f))
        entries.add(Entry(8f, 100f))
        entries.add(Entry(8.5f, 80f))


        val dataSet = LineDataSet(entries, "Clicks")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
        dataSet.lineWidth = 2f


        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        // Enable filling
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.grad) // Use a drawable resource for fill color

        binding.chart.legend.isEnabled = false
        binding.chart.description.isEnabled = false
        binding.chart.data = LineData(dataSet)
        binding.chart.invalidate()
    }

    private fun clickListeners() {
        binding.apply {
            topLinks.setOnClickListener {
                setButton(topLinks)
                unsetButton(recentLinks)
                adapter.updateList(topList)
                adapter.notifyDataSetChanged()

            }
            recentLinks.setOnClickListener {
                unsetButton(topLinks)
                setButton(recentLinks)
                adapter.updateList(recentList)
                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun adapter() {
        adapter = LinksAdapter(requireContext())

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initialize() {
        setStatusBarColor()
        viewModel.getData()
        val handler = Handler(Looper.getMainLooper())
        val updateGreetingTask = object : Runnable {
            override fun run() {
                // Update greeting message
                binding.greeting.text = getGreetingMessage()
                // Schedule next update in 1 minute
                handler.postDelayed(this, 60000)
            }
        }
        handler.post(updateGreetingTask)
    }

    private fun observer(){
        viewModel.apiData.observe(requireActivity()) { apiData ->
            apiData?.let {
                topList = it.data.top_links
                recentList = it.data.recent_links
                adapter.updateList(topList)
                adapter.notifyDataSetChanged()

                addCardView(R.drawable.group, it.total_clicks.toString(), "Today's clicks")
                addCardView(R.drawable.pin, it.top_location.toString(), "Top Location")
                addCardView(R.drawable.globe, it.top_source.toString(), "Top Source")


            }
        }
    }

    private fun setButton(button: Button){
        val blueColor = ContextCompat.getColor(requireContext(), R.color.blue)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        button.backgroundTintList = ColorStateList.valueOf(blueColor)
        button.setTextColor(whiteColor)
    }

    private fun unsetButton(button: Button){
        val textGreyColor = ContextCompat.getColor(requireContext(), R.color.text_grey)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.transparent)
        button.setTextColor(textGreyColor)
        button.backgroundTintList= ColorStateList.valueOf(transparentColor)
    }

    private fun addCardView(img: Int, mainText: String, sub_text: String) {

        val cardView = layoutInflater.inflate(R.layout.card_item, null)
        cardView.findViewById<ImageView>(R.id.card_icon).setImageResource(img)
        cardView.findViewById<TextView>(R.id.main_text).text = mainText.ifEmpty { "- - -" }
        cardView.findViewById<TextView>(R.id.sub_text).text = sub_text

         binding.ll.addView(cardView)


    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    private fun setStatusBarColor() {
        val color = ContextCompat.getColor(requireContext(), R.color.blue)

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor = color
        }

        // Optionally set the status bar icons to be dark or light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}