package com.app.ichsanulalifwan.barani.ui.beranda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ichsanulalifwan.barani.core.model.Kkn
import com.app.ichsanulalifwan.barani.core.model.News
import com.app.ichsanulalifwan.barani.databinding.FragmentBerandaBinding
import com.app.ichsanulalifwan.barani.ui.adapter.DataAdapter
import com.app.ichsanulalifwan.barani.ui.adapter.KknAdapter
import com.app.ichsanulalifwan.barani.viewmodel.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BerandaFragment : Fragment() {

    private lateinit var berandaViewModel: BerandaViewModel
    private lateinit var newsAdapter: DataAdapter
    private lateinit var kknAdapter: KknAdapter
    private lateinit var mDatabase: DatabaseReference
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private var dataList = ArrayList<Kkn>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        berandaViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                application = requireActivity().application,
                isMock = false,
            )
        )[BerandaViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().getReference("Entry")

        if (activity != null) {
            kknAdapter = KknAdapter()
            newsAdapter = DataAdapter()

            setupNewsRecyclerView()
            setupKknRecyclerView()

            observeData()
            getData()

            with(binding) {
                cvItemInfo.setOnClickListener {
                    val urlIntent = Intent(Intent.ACTION_VIEW)
                    urlIntent.data = Uri.parse("https://makassarkota.go.id/makassar-recover/")
                    startActivity(urlIntent)
                }

                textAllNews.setOnClickListener {
                    val action = BerandaFragmentDirections.actionNavigationBerandaToListFragment(1)
                    action.let {
                        findNavController().navigate(it)
                    }
                }

                btnKkn.setOnClickListener {
                    val action = BerandaFragmentDirections.actionNavigationBerandaToListFragment(2)
                    action.let {
                        findNavController().navigate(it)
                    }
                }
            }

            initGarbageCollector()
        }
    }

    private fun initGarbageCollector() {
        binding.icCategory.setOnClickListener {
            Runtime.getRuntime().gc()
            System.gc()
        }
    }

    private fun setupNewsRecyclerView() {
        with(binding.rvNews) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun setupKknRecyclerView() {
        with(binding.rvKknReport) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = kknAdapter
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.children) {
                    val data = getdataSnapshot.getValue(Kkn::class.java)
                    if (data != null) {
                        dataList.add(data)
                    }
                    kknAdapter.setData(dataList)
//                    checkDataKkn()
                }
                Log.d(context.toString(), "Data : $dataList")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun observeData() {
        berandaViewModel.apply {
            getNews().observe(viewLifecycleOwner) { news ->
                newsAdapter.setData(news as ArrayList<News>)
            }

            getIsLoading().observe(viewLifecycleOwner) { isLoading ->
                renderShimmer(isLoading)
            }
        }
    }

    private fun renderShimmer(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                homeShimmer.visibility = View.VISIBLE
                rvKknReport.visibility = View.GONE
                tvNewsHeader.visibility = View.GONE
                textAllNews.visibility = View.GONE
                tvKknHeader.visibility = View.GONE
                btnKkn.visibility = View.GONE
            }
        } else {
            binding.apply {
                homeShimmer.visibility = View.GONE
                rvKknReport.visibility = View.VISIBLE
                tvNewsHeader.visibility = View.VISIBLE
                textAllNews.visibility = View.VISIBLE
                tvKknHeader.visibility = View.VISIBLE
                btnKkn.visibility = View.VISIBLE
                checkDataKkn()
            }
        }
    }

    private fun checkDataKkn() {
        if (dataList.size <= 3 ) {
            binding.btnKkn.visibility = View.GONE
        } else {
            binding.btnKkn.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}