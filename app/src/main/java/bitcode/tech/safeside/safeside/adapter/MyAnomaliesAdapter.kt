package com.bitcodetech.safeside

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bitcode.tech.safeside.safeside.R
import bitcode.tech.safeside.safeside.databinding.MyAnomaliesFragmentBinding
import bitcode.tech.safeside.safeside.databinding.MyAnomalyViewBinding

import bitcode.tech.safeside.safeside.models.MyAnomaly


class MyAnomaliesAdapter(

    private val myAnomalyList: ArrayList<MyAnomaly>

) : RecyclerView.Adapter<MyAnomaliesAdapter.MyAnomaliesViewHolder>() {

    interface OnMyAnomalyClickListener {

        fun onMyAnomalyClick(myAnomaly: MyAnomaly, position: Int)
    }

    var onMyAnomalyClickListener : OnMyAnomalyClickListener? = null

    inner class MyAnomaliesViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(
        view
    ) {
        var binding: MyAnomalyViewBinding

        init {
            binding = MyAnomalyViewBinding.bind(view)

            binding.root.setOnClickListener {
                Toast.makeText(it.context, "Clicked $adapterPosition", Toast.LENGTH_SHORT).show()
                onMyAnomalyClickListener?.onMyAnomalyClick(
                    myAnomalyList[adapterPosition],
                    adapterPosition
                )
            }
        }
    }

    override fun getItemCount() = myAnomalyList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAnomaliesViewHolder {
        return MyAnomaliesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_anomaly_view, null)
        )
    }

    override fun onBindViewHolder(holder: MyAnomaliesViewHolder, position: Int) {
        holder.binding.myAnomalyTitle.text = myAnomalyList[position].title
        holder.binding.myAnomalyReportedOn.text = myAnomalyList[position].reportedOn
        holder.binding.thumbsUpCount.text = myAnomalyList[position].countThumbsUp.toString()
        holder.binding.thumbsDownCount.text = myAnomalyList[position].countThumbsDown.toString()

    }

}