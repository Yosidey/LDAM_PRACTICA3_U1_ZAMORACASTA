package mx.edu.ittepic.ldam_practica3_u1_zamoracastaeda

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
 var arreglo = arrayOf(0,0,0,0,0,0,0,0,0,0)
 var CadenaNumeros = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Verificar Permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){ //preguntar si esta solicitado el servicio Manifest android
            //ENTRA SI EL PERMISO ESTA DENEGADO
            //El siguiente codigo los solicita
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),0)

        }else
            mensaje("PERMISOS YA OTORGADOS")
//Boton asiganr
        btnAsignar.setOnClickListener {

            var p = Integer.parseInt(posicion.text.toString())
            var v =Integer.parseInt(valor.text.toString())
            if (p<0 || p>9) {
                mensaje("Estas fuera del rango")
                return@setOnClickListener
            }
                for (i in 0..9) {
                    arreglo[p] = v
                    CadenaNumeros += "," + arreglo[i].toString()
                    AlertDialog.Builder(this).setTitle("Verificado: ")
                        .setMessage("Se inserto: " + v + " en la posicion " + (p)).show()
                    valor.setText("")
                    posicion.setText("")
                }
                //  AlertDialog.Builder(this).setTitle("Numeros: ").setMessage(numeros).show()
            CadenaNumeros = ""

        }
//**************************Mostrar
        btnMostrar.setOnClickListener {
            for (i in 0..9){
                CadenaNumeros += ","+arreglo[i].toString()
            }
            AlertDialog.Builder(this).setTitle("Datos: ").setMessage("{"+CadenaNumeros+"}").show()
            CadenaNumeros = ""
        }
//***************************Guardar sd
        btnSD.setOnClickListener {
            guardarArchivoSD()
        }
//*******************************Leer
        btnLeerSD.setOnClickListener {
            leerArchivoSD()
        }
    }


    //Mensaje
    fun mensaje(m : String){
        AlertDialog.Builder(this)
            .setTitle("Atencion").setMessage(m).setPositiveButton("OK"){d,i->}.show()
    }
    //Verificar SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
    //*************Guardar Archivo SD
    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var nDirectorio = nomArchivo.text.toString()
            var datosArchivo = File(rutaSD.absolutePath, nDirectorio)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data:String = ""

            for (i in 0..9){
                 data += arreglo[i].toString()+"&"
            }


            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo correctamente ")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }
    //Leer archivo SD******************
    fun leerArchivoSD(){
        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }


        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var nDirectorio = leerArchivo.text.toString()
            var datosArchivo = File(rutaSD.absolutePath,nDirectorio)


            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()  //lee linia completa hsat q encuentra un enter o
            var vector = data.split("&") //lee hasta que encuentra &

            for (i in 0..9){
                CadenaNumeros += ","+arreglo[i].toString()
            }
             AlertDialog.Builder(this).setTitle("Numeros: ").setMessage(CadenaNumeros).show()
           // ponerTextos(vector[0],vector[1],vector[2])
            flujoEntrada.close()

        }catch (error:IOException){
            mensaje(error.message.toString())
        }

    }

}
