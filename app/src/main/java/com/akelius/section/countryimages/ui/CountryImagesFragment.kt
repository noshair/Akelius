package com.akelius.section.countryimages.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akelius.R
import com.akelius.section.countryimages.adapter.CountryImagesAdapter
import com.akelius.service.extensions.Resource
import com.akelius.service.model.countryimagesmodel.File
import com.akelius.service.model.countryimagesmodel.FileCheck
import com.akelius.service.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.coroutines.flow.onStart
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ImagesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var countryImageViewModel: CountryImageViewModel
    private var factory: CountryImagesAdapter? = null
    var hashMap = HashMap<Int, File>()
    var treeMap = TreeMap<Int, FileCheck>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        countryImageViewModel = ViewModelProvider(this).get(
            CountryImageViewModel::class.java
        )
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        countryImageViewModel = ViewModelProvider(this).get(
            CountryImageViewModel::class.java
        )
        countryImageViewModel.getImageRemotelyData(requireContext())
        countryImageViewModel.getImageLocallyData()

        lifecycleScope.launchWhenStarted {
            var count = 0
            countryImageViewModel.countryimageslist.collect {
                initRecyclerview()

                when (it) {
                    is Resource.onFailure -> {
                        Log.d("Noshairam", "Failure")
                    }
                    is Resource.onSuccess -> {
                        treeMap.clear()
                        Log.d("Noshairam", "Success")
                        it.data?.files
                        if (it.data?.files != null) {
                            Log.d("Noshairam", it.data.files.toString())
                            var j = 0
                            it.data.files.forEach {
                                if (Utils.remote.get(count) == it.stats.ino) {

                                    treeMap.set(
                                        j, FileCheck(
                                            "Added R Missing L",
                                            it
                                        )
                                    )
                                    if (Utils.remote.size - 1 != count)
                                        count++
                                } else {
                                    treeMap.set(
                                        j, FileCheck(
                                            "",
                                            it
                                        )
                                    )
                                }
                                j++
                            }
                            countryImageViewModel.countryimageslocallylist.collect { local ->
                                count = 0
                                local.data?.files?.forEach { data ->
                                    if (Utils.local.get(count) == data.stats.ino) {

                                        treeMap.set(
                                            j, FileCheck(
                                                "Mssing R Added L",
                                                data
                                            )
                                        )
                                        if (Utils.remote.size - 1 != count)
                                            count++
                                        j++
                                    }

                                }
                                factory?.update(treeMap)
                            }
                        }
                    }
                    is Resource.onLoading -> {
                        Log.d("Noshairam", "Loading")
                    }
                }
            }
        }

    }

    private fun initRecyclerview() {
        rc_img_list.apply {
            layoutManager = LinearLayoutManager(context)
            factory = CountryImagesAdapter(context)
            adapter = factory
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}