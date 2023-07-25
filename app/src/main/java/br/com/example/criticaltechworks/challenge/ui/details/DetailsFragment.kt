package br.com.example.criticaltechworks.challenge.ui.details

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.example.criticaltechworks.challenge.databinding.FragmentDetailsBinding
import br.com.example.criticaltechworks.challenge.model.Result
import br.com.example.criticaltechworks.challenge.util.AppConstants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private lateinit var detailsViewModel: DetailsViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        detailsViewModel =
            ViewModelProvider(this)[DetailsViewModel::class.java]

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val itemId = requireArguments().getInt(AppConstants.BUNDLE.FILTER_ID)

        detailsViewModel.getArticleDetail(itemId)

        observe()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        detailsViewModel.article.observe(viewLifecycleOwner) { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { article ->


                        val date = SimpleDateFormat("yyyy-MM-dd").parse(article.publishedAt)

                        binding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(date)
                        binding.titleTextView.text = article.title
                        binding.nameTextView.text = article.author
                        binding.descriptionTextView.text = article.description
                        Glide.with(binding.root).load(article.urlToImage).apply(
                            RequestOptions()
                                .placeholder(R.drawable.ic_menu_camera)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)

                        ).into(binding.imageView)
                    }
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                }

                Result.Status.LOADING -> {}
            }

        }
    }

    private fun showError(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }
}