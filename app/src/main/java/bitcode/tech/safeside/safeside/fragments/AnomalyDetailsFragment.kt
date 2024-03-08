package bitcode.tech.safeside.safeside

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bitcode.tech.safeside.safeside.MainActivity
import bitcode.tech.safeside.safeside.databinding.AnomalyDetailsFragmentBinding

import bitcode.tech.safeside.safeside.factory.AnomaliesViewModelFactory
import bitcode.tech.safeside.safeside.models.Anomaly
import bitcode.tech.safeside.safeside.repositories.AnomalyDetailsRepository
import bitcode.tech.safeside.safeside.viewmodels.AnomalyDetailsViewModel

class AnomalyDetailsFragment : Fragment(){

    private lateinit var binding: AnomalyDetailsFragmentBinding
    private lateinit var anomalyDetailsViewModel : AnomalyDetailsViewModel
    private var anomalyId : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding = AnomalyDetailsFragmentBinding.inflate(inflater)
//        binding.scrollView.setBackgroundColor(Color.WHITE)


        initViewModels()
        initObservers()

        anomalyId = (requireArguments().getSerializable("anomaly") as Anomaly)!!.id
        anomalyDetailsViewModel.getAnomalyDetails(anomalyId)
        return binding.root
    }

    private fun initViewModels(){

        anomalyDetailsViewModel =
            ViewModelProvider(
            this,
            AnomaliesViewModelFactory(
                AnomalyDetailsRepository()
            )
        )[AnomalyDetailsViewModel :: class.java]

    }


    private fun initObservers(){

        anomalyDetailsViewModel.anomalyDetailsLiveData.observe(viewLifecycleOwner){

            binding.anomalyTitle.text = it.title
            binding.webviewDescription.loadData(it.description,"text/html", "UTF-8")
            binding.txtAddedOn.text = it.added_on
            binding.UnsafeRating.progress = it.unsafe_rating

            val anomalyLat = String.format("%.6f", it.lat)
            val anomalyLng = String.format("%.6f", it.lng)

            binding.anomalyLat.text = "Latitude: $anomalyLat"
            binding.anomalyLng.text = "Longitude: $anomalyLng"

// Disable the drawer layout when anomaly details are loaded
            disableDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        disableDrawer()
    }
    private fun disableDrawer() {
        // Check if the hosting activity is MainActivity
        if (requireActivity() is MainActivity) {
            // Access the MainActivity and call a function to disable the drawer layout
            (requireActivity() as MainActivity).disableDrawer()
        }
    }
}

