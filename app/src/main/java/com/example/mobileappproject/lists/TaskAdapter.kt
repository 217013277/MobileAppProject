package com.example.mobileappproject.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.mobileappproject.R

class TaskAdapter(context: Context, taskList: MutableList<Task>) : BaseAdapter() {
    private val _inflater: LayoutInflater = LayoutInflater.from(context)
    private var _taskList = taskList
    private var _rowListener: TaskRowListener = context as TaskRowListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = _taskList[position].objectId as String
        val itemText: String = _taskList[position].taskDesc as String
        val done: Boolean = _taskList[position].done as Boolean

        val view: View
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = _inflater.inflate(R.layout.task_rows, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.desc.text = itemText
        listRowHolder.done.isChecked = done
        listRowHolder.done.setOnClickListener {
            _rowListener.onTaskChange(objectId, !done)
        }
        listRowHolder.remove.setOnClickListener {
            _rowListener.onTaskDelete(objectId, itemText) }

        return view
    }

    override fun getItem(index: Int): Any {
        return _taskList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return _taskList.size
    }

    private class ListRowHolder(row: View?) {
        val desc: TextView = row!!.findViewById(R.id.txtTaskDesc) as TextView
        val done: CheckBox = row!!.findViewById(R.id.chkDone) as CheckBox
        val remove: ImageButton = row!!.findViewById(R.id.btnRemove) as ImageButton
    }
}
