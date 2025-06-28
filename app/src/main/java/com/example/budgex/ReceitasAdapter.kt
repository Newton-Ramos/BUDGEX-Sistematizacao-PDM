package com.example.budgex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ReceitaAdapter(
    private val lista: MutableList<Receita>,
    private val onItemClick: (Receita) -> Unit,
    private val onItemDelete: (Receita) -> Unit
) : RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder>() {

    class ReceitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descricao: TextView = view.findViewById(R.id.tvDescricao)
        val valor: TextView = view.findViewById(R.id.tvValor)
        val data: TextView = view.findViewById(R.id.tvData)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteReceita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receita, parent, false)
        return ReceitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {
        val receita = lista[position]
        holder.descricao.text = receita.descricao
        holder.valor.text = holder.itemView.context.getString(R.string.formato_valor, receita.valor)
        holder.data.text = receita.data

        holder.itemView.setOnClickListener {
            onItemClick(receita)
        }
        holder.btnDelete.setOnClickListener {
            onItemDelete(receita)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun atualizarLista(novaLista: List<Receita>) {
        val diffCallback = ReceitaDiffCallback(lista, novaLista)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        lista.clear()
        lista.addAll(novaLista)
        diffResult.dispatchUpdatesTo(this)
    }

    class ReceitaDiffCallback(
        private val oldList: List<Receita>,
        private val newList: List<Receita>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
