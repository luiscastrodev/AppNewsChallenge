package br.com.example.criticaltechworks.challenge.ui.home

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.criticaltechworks.challenge.data.entity.ArticleEntity
import br.com.example.criticaltechworks.challenge.databinding.ListItemArticleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat


class ArticleAdapter(private val list: ArrayList<ArticleEntity>) :
    RecyclerView.Adapter<ArticleViewHolder>() {

    private lateinit var listener :  IClickListener;

    interface IClickListener{
        fun onClickItem(id: Int) : Unit
    }

    public fun setListener(listener : IClickListener ){
        this.listener = listener;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemArticleBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(itemBinding,this.listener)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindData(list[position])
    }
    override fun getItemCount(): Int {
        return list.count()
    }

    fun updateData(articleList: List<ArticleEntity>) {
        list.clear()
        val sortedList = articleList.sortedBy { article -> article.publishedAt }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }
}

class ArticleViewHolder(private val itemBinding: ListItemArticleBinding,val listener : ArticleAdapter.IClickListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(article: ArticleEntity) {

        itemBinding.titleTextView.text =  article.title
        itemBinding.nameTextView.text = article.author
        itemBinding.descriptionTextView.text = article.description

        val date = SimpleDateFormat("yyyy-MM-dd").parse(article.publishedAt)
        itemBinding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(date)

        Glide.with(itemBinding.root).load(article.urlToImage).apply(
            RequestOptions()
                .placeholder(R.drawable.ic_menu_camera)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        ) .into(itemBinding.imageView)

        itemBinding.root.setOnClickListener {
            if (listener != null) {
                listener.onClickItem(article.id)
            }
        }
    }
}