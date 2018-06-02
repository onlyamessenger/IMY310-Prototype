package co.ascended.waiterio.managers

import android.support.percent.PercentRelativeLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.ascended.waiterio.R
import co.ascended.waiterio.entity.Item
import co.ascended.waiterio.widget.IconButton
import co.ascended.waiterio.widget.setMargins

class ItemsAdapter constructor(val height: Float, val update: (Item, Boolean) -> Unit):
        RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var items: ArrayList<Item> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_manager, parent, false)
        val layoutParams = itemView.layoutParams
        layoutParams.height = height.toInt()
        itemView.layoutParams = layoutParams
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(items: List<Item>) {
        if (this.items.isEmpty()) {
            this.items.addAll(items)
            notifyDataSetChanged()
        }
        else {
            items.forEach { o ->
                // Update existing instances of the item.
                val query = this.items.filter { item -> item.id == o.id }
                if (query.size == 1) {
                    val index = this.items.indexOf(query[0])
                    this.items.removeAt(index)
                    this.items.add(index, o)
                    notifyItemChanged(index)
                }
                // Or just insert a new item.
                else {
                    this.items.add(0, o)
                    notifyItemInserted(0)
                }
            }
        }
    }

    fun removeItem(item: Item) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }

    fun clear() {
        items = arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {

        private lateinit var item: Item

        private var root: PercentRelativeLayout = view.findViewById(R.id.root)
        private var name: TextView = view.findViewById(R.id.textViewName)
        private var edit: IconButton = view.findViewById(R.id.buttonEdit)
        private var delete: IconButton = view.findViewById(R.id.buttonRemove)

        fun bind(item: Item) {
            setMargins(root)
            this.item = item

            name.text = this.item.name
            edit.setOnClickListener { update(this.item, false) }
            delete.setOnClickListener { update(this.item, true) }
        }
    }
}