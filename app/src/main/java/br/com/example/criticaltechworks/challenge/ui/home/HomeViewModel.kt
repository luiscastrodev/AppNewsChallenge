package br.com.example.criticaltechworks.challenge.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.example.criticaltechworks.challenge.data.ArticleRepository
import br.com.example.criticaltechworks.challenge.data.SourceRepository
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import br.com.example.criticaltechworks.challenge.model.Result
import br.com.example.criticaltechworks.challenge.util.AppConstants
import br.com.example.criticaltechworks.challenge.util.SecurityPreferences

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val sourceRepository: SourceRepository,
    private val context: Context
) :
    ViewModel() {

    private val _articleList = MutableLiveData<Result<List<ArticleEntity>>>()
    val articleList = _articleList


    private val _sourceList = MutableLiveData<Result<List<SourceEntity>>>()
    val sourceList = _sourceList

    init {
        fechSources()
    }

    private fun fechSources() {
        viewModelScope.launch {
            sourceRepository.fetchSources().collect {
                _sourceList.value = it
            }
        }
    }

    private fun fetchArticles(source: String) {
        viewModelScope.launch {
            articleRepository.fetchArticles(source).collect {
                _articleList.value = it
            }
        }
    }

    fun loadArticleBySource(id: String ) {

        var filter = id

        if(filter.isEmpty()){
            filter = "bbc-news"//default
        }

        SecurityPreferences(this.context).store(AppConstants.SHARED.ID_KEY, filter)
        fetchArticles(filter)
    }
}