package com.example.mobileappproject

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


class RecyclerViewAdapter(private val context: Context, private val taskList: MutableList<Task>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val _inflater: LayoutInflater = LayoutInflater.from(context)
    private var _taskList = taskList
    private var _rowListener: TaskRowListener = context as TaskRowListener

    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener { fun onItemClick(position: Int) }
    fun setOnItemClickListener(listener: OnItemClickListener) { mListener = listener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_rows, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val desc: String = _taskList[position].taskDesc.toString()
        val done: Boolean = _taskList[position].done.toString().toBoolean()
        val objectId: String = _taskList[position].objectId.toString()

        holder.desc.text = desc
        holder.done.isChecked = done
        holder.done.setOnClickListener{ _rowListener.onTaskChange(objectId, !done) }
        holder.remove.setOnClickListener{ _rowListener.onTaskDelete(objectId) }
    }

    override fun getItemCount(): Int {
        return _taskList.size
    }

    class ViewHolder(ItemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val desc: TextView = itemView.findViewById(R.id.txtTaskDesc) as TextView
        val done: CheckBox = itemView.findViewById(R.id.chkDone) as CheckBox
        val remove: ImageButton = itemView.findViewById(R.id.btnRemove) as ImageButton

        init {
            itemView.setOnClickListener{ listener.onItemClick(bindingAdapterPosition) }
        }
    }

}
