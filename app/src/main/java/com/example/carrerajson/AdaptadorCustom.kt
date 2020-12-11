package com.example.carrerajson

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCustom (var contexto: Context, items:ArrayList<Oferta>):
    RecyclerView.Adapter<AdaptadorCustom.ViewHolder> () {


    var items:ArrayList<Oferta>?=null

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista  = LayoutInflater.from(contexto).inflate(R.layout.template_ofertas, parent, false)
        val viewHolder = ViewHolder(vista)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(holder: AdaptadorCustom.ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.id?.text = item?.id
        holder.carrera?.text = item?.carrera
        holder.universidad?.text = item?.universidad
        Log.d("onBindViewHolder",item?.carrera)
    }

    class ViewHolder(vista: View):RecyclerView.ViewHolder(vista){
            var vista = vista
            var id: TextView? = null
            var universidad: TextView? = null
            var carrera: TextView? = null

            init {
                id = vista.findViewById(R.id.txtId)
                carrera = vista.findViewById(R.id.txtCarrera)
                universidad = vista.findViewById(R.id.txtEscuela)
            }
        }
    }