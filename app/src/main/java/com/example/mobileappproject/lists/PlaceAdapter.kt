package com.example.mobileappproject.lists

import android.content.Context;
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView;
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileappproject.R
import com.squareup.picasso.Picasso

class PlaceAdapter(private val context: Context, private val placeList: MutableList<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    private var _placeList = placeList
    private var _rowListener: PlaceRowListener = context as PlaceRowListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_rows, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name: String = _placeList[position].placeName.toString()
        val desc: String = _placeList[position].placeDesc.toString()
        val address: String = _placeList[position].placeAddress.toString()
        val isFav: Boolean = _placeList[position].isFav.toString().toBoolean()
        val objectId: String = _placeList[position].objectId.toString()
        val imageUrl: String = _placeList[position].imageUrl.toString()

        if (imageUrl.isNotEmpty()){
            Picasso.get().load(imageUrl).into(holder.image)
        } else {
            Picasso.get().load(R.drawable.placeholder_image_square).into(holder.image)
        }

        holder.name.text = name
        holder.desc.text = desc
        holder.address.text = address
        holder.isFav.isChecked = isFav
        holder.isFav.setOnClickListener{ _rowListener.onFavClick(objectId, !isFav) }
        holder.removeBtn.setOnClickListener{ _rowListener.onPlaceDelete(objectId, name) }
        holder.itemView.setOnClickListener{ _rowListener.onItemSelect(objectId) }
    }

    override fun getItemCount(): Int {
        return _placeList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = itemView.findViewById(R.id.tvPlaceName) as TextView
        val desc: TextView = itemView.findViewById(R.id.tvPlaceDesc) as TextView
        val address: TextView = itemView.findViewById(R.id.tvAddress) as TextView
        val isFav: CheckBox = itemView.findViewById(R.id.imgPlaceFav) as CheckBox
        val removeBtn: ImageButton = itemView.findViewById(R.id.btnRemove) as ImageButton
        val image: ImageView = itemView.findViewById(R.id.ivPlaceImage) as ImageView

    }

}
