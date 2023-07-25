package br.com.example.criticaltechworks.challenge.ui.home

import android.R.attr.value
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.criticaltechworks.challenge.R
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.databinding.FragmentHomeBinding
import br.com.example.criticaltechworks.challenge.model.Result
import br.com.example.criticaltechworks.challenge.util.AppConstants
import br.com.example.criticaltechworks.challenge.util.SecurityPreferences
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), ArticleAdapter.IClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    private val list = ArrayList<ArticleEntity>()
    private lateinit var articleAdapter: ArticleAdapter

    private var listSources: List<SourceEntity> = listOf()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        init()
        observe()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerArticles.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            layoutManager.orientation
        )

        binding.recyclerArticles.addItemDecoration(dividerItemDecoration)
        articleAdapter = ArticleAdapter(list)
        binding.recyclerArticles.adapter = articleAdapter
        articleAdapter.setListener(this)

    }


    private fun observe() {
        homeViewModel.articleList.observe(viewLifecycleOwner) { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { list ->
                        articleAdapter.updateData(list)
                        if (list.isEmpty())
                            binding.noDataTextView.visibility = View.VISIBLE
                        else
                            binding.noDataTextView.visibility = View.GONE
                    }
                    binding.loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    binding.loading.visibility = View.GONE
                    binding.noDataTextView.visibility = View.VISIBLE

                }

                Result.Status.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.noDataTextView.visibility = View.GONE

                }
            }

        }


        homeViewModel.sourceList.observe(viewLifecycleOwner) { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { sources ->
                        setSpnnerSource(sources)
                    }
                    binding.loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    binding.loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onClickItem(id: Int) {
        val args = Bundle()
        args.putInt(AppConstants.BUNDLE.FILTER_ID, id)
        val navController = findNavController()
        navController.navigate(R.id.nav_details, args)
    }

    private fun showError(msg: String) {
        make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun setSpnnerSource(sources : List<SourceEntity>){

        listSources = sources

        val list = mutableListOf<String>()

        for (p in sources) {
            list.add(p.id!!)
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, list )

        binding.spinnerSource?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var source = listSources[position].id
                homeViewModel.loadArticleBySource(source!!)
            }

        }
        binding.spinnerSource.adapter = adapter

        val position = adapter.getPosition(SecurityPreferences(requireContext()).get(AppConstants.SHARED.ID_KEY)) // get position of value

        binding.spinnerSource.setSelection(position) // set selected item by position
    }
}