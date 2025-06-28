package com.example.budgex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class DespesaAdapter(
    private val lista: MutableList<Despesa>,
    private val onItemClick: (Despesa) -> Unit,
    private val onItemDelete: (Despesa) -> Unit
) : RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder>() {

    class DespesaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descricao: TextView = view.findViewById(R.id.tvDescricao)
        val valor: TextView = view.findViewById(R.id.tvValor)
        val data: TextView = view.findViewById(R.id.tvData)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteDespesa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_despesa, parent, false)
        return DespesaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val despesa = lista[position]
        holder.descricao.text = despesa.descricao
        holder.valor.text = holder.itemView.context.getString(R.string.formato_valor, despesa.valor)
        holder.data.text = despesa.data

        holder.itemView.setOnClickListener {
            onItemClick(despesa)
        }
        holder.btnDelete.setOnClickListener {
            onItemDelete(despesa)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun atualizarLista(novaLista: List<Despesa>) {
        val diffCallback = DespesaDiffCallback(lista, novaLista)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        lista.clear()
        lista.addAll(novaLista)
        diffResult.dispatchUpdatesTo(this)
    }

    class DespesaDiffCallback(
        private val oldList: List<Despesa>,
        private val newList: List<Despesa>
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
