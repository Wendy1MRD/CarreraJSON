package com.example.carrerajson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.IOException

class MainActivity : AppCompatActivity() {

    //var estudiante = ArrayList<Oferta>()

        var estudiante = ArrayList<Oferta>()

    var id = ArrayList<String>()
    var carrera = ArrayList<String>()
    var universidad = ArrayList<String>()

    fun prueba(array:ArrayList<Oferta>){
        estudiante[0].id
        estudiante[0].carrera
        estudiante[0].universidad
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bDatos = findViewById<Button>(R.id.btnObtener)
      //  val bVer = findViewById<Button>(R.id.btnVer)

        bDatos.setOnClickListener {
            if(Network.hayRed(this)){

               //solicitudRed("https://jsoncarreras.s3.us-east-2.amazonaws.com/carreras.txt")

               val intent = Intent(applicationContext, Lista::class.java)
              //  intent.putExtra("miLista", estudiante)
                startActivity(intent)

                Toast.makeText(this,"Datos Obtenidos", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Error: No hay Red", Toast.LENGTH_LONG).show()
            }
        }

      /*  bVer.setOnClickListener {
            val intent = Intent(applicationContext, Lista::class.java)
            intent.putExtra("miLista", estudiante)
            startActivity(intent)
        }*/
    }

    private fun SolictudHTTPVolley(url: String){
        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String>{
                response ->
            try {

                val gson = Gson()
                val res = gson.fromJson(response.toString(), Ofertas::class.java)
                Log.d("jsoncrudo",res.toString())

                for (i in 0..(res?.ofertas?.count().toString().toInt()) -1){
                    id.add(res.ofertas?.get(i)?.id.toString())
                    carrera.add(res.ofertas?.get(i)?.carrera.toString())
                    universidad.add(res.ofertas?.get(i)?.universidad.toString())
                }
                Log.d("GSON", res.ofertas?.count().toString())
            }catch (e: Exception){
                Log.e("error",e.toString())
            }
        }, Response.ErrorListener {  })
        queue.add(solicitud)
    }

    ///---------------------------------------------------------------------------------------------
    private fun agregar(id: String, carrera:String, universidad:String){
        estudiante.add(Oferta("$id", "$carrera", "$universidad"))
    }

    private fun solicitudRed(url:String){
        val cliente = OkHttpClient()
        val solicitud = okhttp3.Request.Builder().url(url).build()

        cliente.newCall(solicitud).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                //el error
                Log.e("El error ", "sin respuesta")
            }

            override fun onResponse(call: Call?, response: okhttp3.Response?) {
                var result = response?.body()?.string()
                //Log.d("Json crudo ", result)

                //con esto el código que vaya debajo, vuelvo a pasar al thread principal del aplicativo
                //y ejecute el código que defina
                this@MainActivity.runOnUiThread {
                    if (response!!.isSuccessful()){
                        try {

                            val gson = Gson()
                            val res = gson.fromJson(result, pruebajs::class.java)
                            Log.d("jsoncrudo",res.toString())

                           for (i in 0..(res.ofertas.size -1)){
                               agregar(
                                       "${res.ofertas.get(i).Carrera_id}",
                                       "${res.ofertas.get(i).Carrera}",
                                       "${res.ofertas.get(i).Universidad}"
                               )
                            }

                            Log.d("GSON", res.ofertas.get(0).Carrera)

                        }catch (e: Exception){
                            Log.e("error",e.toString())
                        }
                    } else{ Log.e("El error ", "No se pudo conectar") }
                }
            }
        })
    }
}
