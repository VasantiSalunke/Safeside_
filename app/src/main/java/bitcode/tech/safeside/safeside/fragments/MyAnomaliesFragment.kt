package bitcode.tech.safeside.safeside.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bitcode.tech.safeside.safeside.AnomalyDetailsFragment
import bitcode.tech.safeside.safeside.R
import bitcode.tech.safeside.safeside.databinding.MyAnomaliesFragmentBinding

import bitcode.tech.safeside.safeside.factory.AnomaliesViewModelFactory
import bitcode.tech.safeside.safeside.models.MyAnomaly
import bitcode.tech.safeside.safeside.repositories.MyAnomaliesRepository
import bitcode.tech.safeside.safeside.viewmodels.MyAnomaliesViewModel
import com.bitcodetech.safeside.MyAnomaliesAdapter


class MyAnomaliesFragment : Fragment() {

    private lateinit var binding: MyAnomaliesFragmentBinding

    private lateinit var myAnomaliesViewModel: MyAnomaliesViewModel

    private lateinit var myAnomaliesAdapter: MyAnomaliesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MyAnomaliesFragmentBinding.inflate(inflater)

        initViewModels()
        initViews()
        initObservers()
        initListener()

        myAnomaliesViewModel.getMyAnomalies()


        return binding.root
    }

    private fun initViews(){

        binding.myAnomaliesRecyclerView.layoutManager =
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        myAnomaliesAdapter = MyAnomaliesAdapter(myAnomaliesViewModel.myAnomaliesList)
        binding.myAnomaliesRecyclerView.adapter = myAnomaliesAdapter

    }

    private fun initViewModels(){

        myAnomaliesViewModel = ViewModelProvider(
            this,
            AnomaliesViewModelFactory(
                MyAnomaliesRepository()
            )
        )[MyAnomaliesViewModel :: class.java]

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers(){
        myAnomaliesViewModel.myAnomaliesLoadedLiveData.observe(
            viewLifecycleOwner
        ){
            if(it){
                myAnomaliesAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun initListener() {
        myAnomaliesAdapter.onMyAnomalyClickListener =
            object : MyAnomaliesAdapter.OnMyAnomalyClickListener {
                override fun onMyAnomalyClick(myAnomaly: MyAnomaly, position: Int) {
                    addAnomalyDetailsFragment(myAnomaly)
                    Toast.makeText(context, "$myAnomaly.id", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addAnomalyDetailsFragment(myAnomaly: MyAnomaly) {

        val anomalyDetailsFragment = AnomalyDetailsFragment().apply {
            arguments = Bundle().apply {
                putSerializable("myAnomaly", myAnomaly)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentcontainer, anomalyDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}