package com.example.carrerajson

import android.os.Bundle
import android.util.Log
import android.util.Log.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.IOException

class Lista : AppCompatActivity() {

   // var prueba = intent.getSerializableExtra("miLista") as ArrayList<Oferta>

   var lista: RecyclerView? = null
    var estudiante = ArrayList<Oferta>()
    var adaptador = AdaptadorCustom(this,estudiante)
    var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        solicitudRed("https://jsoncarreras.s3.us-east-2.amazonaws.com/carreras.txt")

        /*var id = intent.getStringArrayListExtra("id")
        var carrera = intent.getStringArrayListExtra("carrera")
        var universidad = intent.getStringArrayListExtra("universidad")*/

     /*  var recibir = intent.getStringArrayListExtra("rec")

        var num = carrera.size-1
        var empleados=ArrayList<Oferta>();
        for (i in 0..num){
            empleados.add(Oferta(id[i],carrera[i],universidad[i]))
        }*/


    }
    private fun presentar(){
        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true) //habilitar la vista de la lista

        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager = layoutManager //habilitar el manejador de la lista

        adaptador = AdaptadorCustom(this, estudiante) // enviar el contexto y el arrayList al adaptador
        lista?.adapter = adaptador //asiganar el adaptador a la lista
    }
    //---------------------------------------------

    private fun agregar(id: String, carrera:String, universidad:String){
        estudiante.add(Oferta("$id", "$carrera", "$universidad"))
    }

    private fun solicitudRed(url:String){

        val cliente = OkHttpClient()
        val solicitud = okhttp3.Request.Builder().url(url).build()

        cliente.newCall(solicitud).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                //el error
                e("El error ", "sin respuesta")
            }

            override fun onResponse(call: Call?, response: okhttp3.Response?) {
                var result = response?.body()?.string()
                //Log.d("Json crudo ", result)

                //con esto el código que vaya debajo, vuelvo a pasar al thread principal del aplicativo
                //y ejecute el código que defina
                this@Lista.runOnUiThread {
                    if (response!!.isSuccessful()){
                        try {

                            val gson = Gson()
                            val res = gson.fromJson(result, pruebajs::class.java)
                            d("jsoncrudo",res.toString())

                            for (i in 0..(res.ofertas.size -1)){
                                agregar(
                                        "${res.ofertas.get(i).Carrera_id}",
                                        "${res.ofertas.get(i).Carrera}",
                                        "${res.ofertas.get(i).Universidad}"
                                )
                            }
                            presentar()

                        }catch (e: Exception){
                            e("error",e.toString())
                        }
                    } else{ e("El error ", "No se pudo conectar") }
                }
            }
        })
    }
}
