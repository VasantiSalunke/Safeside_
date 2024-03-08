package bitcode.tech.safeside.safeside.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bitcode.tech.safeside.safeside.databinding.ReportAnomaliesFragmentBinding
import bitcode.tech.safeside.safeside.factory.AnomaliesViewModelFactory
import bitcode.tech.safeside.safeside.models.ReportAnomaly
import bitcode.tech.safeside.safeside.repositories.ReportAnomalyRepository
import bitcode.tech.safeside.safeside.viewmodels.ReportAnomalyViewModel
import com.google.android.gms.maps.model.LatLng

class ReportAnomalyFragment : Fragment() {

    private lateinit var binding: ReportAnomaliesFragmentBinding
    private lateinit var reportAnomalyViewModel: ReportAnomalyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReportAnomaliesFragmentBinding.inflate(layoutInflater, container, false)


        initViewModels()
        initListeners()
        initObservers()


        retrieveLatLng()

        return binding.root

    }

    private fun retrieveLatLng(): LatLng? {
        val arguments = arguments
        if (arguments != null && arguments.containsKey("latitude") && arguments.containsKey("longitude")) {
            val latitude = arguments.getDouble("latitude", 0.0)
            val longitude = arguments.getDouble("longitude", 0.0)


            return LatLng(latitude, longitude)
        }
        return null
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initListeners() {

        binding.seekBarCriticalLevel.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.criticalLevelTextViewTitle.text = "$progress%"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.btnSaveAnomaly.setOnClickListener {
            val unsafeRating = binding.seekBarCriticalLevel.progress.toString()
            val latLng = retrieveLatLng()

            latLng?.let {
                Log.d("tag", " saveAnomaly ${it.latitude},${it.longitude}")

                reportAnomalyViewModel.addAnomaly(
                    ReportAnomaly(
                        binding.txtAddedOn.text.toString(),
                        binding.edtAnomalyDescription.text.toString(),
                        it.latitude,
                        it.longitude,
                        binding.edtAnomalyTitle.text.toString(),
                        unsafeRating

                    )
                )
            }
        }
    }

    private fun initObservers() {
        reportAnomalyViewModel.reportPostedLiveData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("tag", "Success: ${response.message}")

            } else {
                Log.e("tag", "Error: ${response?.message}")

            }
        }
    }


    private fun initViewModels() {
        reportAnomalyViewModel = ViewModelProvider(
            this,
            AnomaliesViewModelFactory(
                ReportAnomalyRepository()

            )
        )[ReportAnomalyViewModel::class.java]
    }

}