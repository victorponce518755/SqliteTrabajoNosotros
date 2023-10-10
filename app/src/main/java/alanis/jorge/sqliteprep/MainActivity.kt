package alanis.jorge.sqliteprep
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var txtResults: TextView


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtUserId = findViewById<EditText>(R.id.edtUserId)
        txtResults = findViewById(R.id.txtResults)

        val btnInsert = findViewById<Button>(R.id.btnInsert)
        val btnRetrieve = findViewById<Button>(R.id.btnRetrieve)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnInsert.setOnClickListener {
            val name = edtName.text.toString()
            val phoneText = edtPhone.text.toString()
            val email = edtEmail.text.toString()

            Log.d("MyApp", "Name: $name, Phone: $phoneText, Email: $email")


            if (name.isNotBlank() && phoneText.isNotBlank() && email.isNotBlank()) {

                val phone = phoneText.toLongOrNull()
                if (phone != null) {
                    val success = databaseHelper.addUser(name, phone, email)
                    showToast(success)
                }
                else
                {
                    Toast.makeText(this, "Favor de introducir un numero de telefono valido!", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRetrieve.setOnClickListener {
            val cursor = databaseHelper.getAllUsers()
            val output = StringBuilder()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val phone = cursor.getInt(cursor.getColumnIndex("phone"))
                val email = cursor.getString(cursor.getColumnIndex("email"))

                output.append("ID: $id, Nombre: $name, Telefono: $phone, Email: $email\n")
            }
            txtResults.text = output.toString()
            cursor.close()
        }

        btnUpdate.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()
            val name = edtName.text.toString()
            val phoneText = edtPhone.text.toString()
            val email = edtEmail.text.toString()


            if (id != null && name.isNotBlank() && phoneText.isNotBlank() && email.isNotBlank()) {
                val phone = phoneText.toLongOrNull() // Cambiado a Long

                if (phone != null) {
                    val success = databaseHelper.updateUser(id, name, phone, email)
                    showToast(success)
                } else {
                    Toast.makeText(this, "El valor del teléfono no es un número válido.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()

            if (id != null) {
                val success = databaseHelper.deleteUser(id)
                showToast(success)
            } else {
                Toast.makeText(this, "Favor de introducir un id valido!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(success: Boolean) {
        if (success) {
            Toast.makeText(this, "Operacion exitosa", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Operacion fallida!", Toast.LENGTH_SHORT).show()
        }
    }
}
