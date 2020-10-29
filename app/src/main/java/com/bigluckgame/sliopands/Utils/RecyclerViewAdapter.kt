package com.bigluckgame.sliopands.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigluckgame.sliopands.Model.ModelNews
import com.bigluckgame.sliopands.R
import com.bigluckgame.sliopands.view.AlertDialog

class RecyclerViewAdapter(var listNews: ModelNews): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    var mainList = listNews.results

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(mainList.get(position).title)
        Glide.with(holder.itemView.context)
                 .load(mainList.get(position).thumbnail_standard).into(holder.titleImage);
        //подобная обработка клика на item,допустима только в случае работы с несложной логикой,
        // в противном случае,клик лучше обрабатывает в классе Holder'a
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //транзакцию необходимо при каждом нажатии создавать снова,иначе при нажатии будет ошибка из разряда "эта транзакция уже вызвана"
//                var transaction: FragmentTransaction = fragmentManager.beginTransaction()
//                var alertDialog = AlertDialog(mainList.get(position).title,mainList.get(position).abstract)
//                alertDialog.show(transaction,"ff")
            }
        })
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title:TextView = itemView.findViewById(R.id.title_news)

        var titleImage:ImageView = itemView.findViewById(R.id.image_title)
    }
}