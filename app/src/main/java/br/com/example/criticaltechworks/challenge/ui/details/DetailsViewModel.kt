package br.com.example.criticaltechworks.challenge.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import br.com.example.criticaltechworks.challenge.data.ArticleRepository
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private var _id = MutableLiveData<Int>()
    private val _article: LiveData<br.com.example.criticaltechworks.challenge.model.Result<ArticleEntity>> = _id.distinctUntilChanged().switchMap {
        liveData {
            articleRepository.fetchArticle(it).onStart {
                emit(br.com.example.criticaltechworks.challenge.model.Result.loading())
            }.collect {
                emit(it)
            }
        }
    }
    val article = _article

    fun getArticleDetail(id: Int) {
        if(id == null || id <= 0 )
            throw IllegalArgumentException("invalid id")
        _id.value = id
    }
}