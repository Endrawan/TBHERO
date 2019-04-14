package com.tbhero.application.activities

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tbhero.application.R
import com.tbhero.application.components.AppCompatActivity
import com.tbhero.application.components.Extension
import com.tbhero.application.models.Config
import com.tbhero.application.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val TAG = "SignUpActivity"
    private val pmos = mutableListOf<User>()
    private val pmosSpinnerVal = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initToolbar()
        initView()

        signUp.setOnClickListener {
            verifAndCreate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val sab = supportActionBar
        sab?.setDisplayHomeAsUpEnabled(true)
        sab?.setDisplayShowHomeEnabled(true)
        sab?.setHomeAsUpIndicator(R.drawable.ic_close_grey_20dp)
        sab?.setDisplayShowTitleEnabled(false)
    }

    private fun initView() {
        // Initialize pmo value
        getPMO()

        // Initialize Category Spinner
        val categoriesAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            User.USER_CATEGORIES
        )
        categoriesAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        category.getSpinner().adapter = categoriesAdapter
        category.getSpinner().onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                when (position) {
                    User.USER_CATEGORY_PASIEN -> {
                        pasienOptionalForms.visibility = View.VISIBLE
                    }
                    User.USER_CATEGORY_PMO -> {
                        pasienOptionalForms.visibility = View.GONE
                    }
                    User.USER_CATEGORY_SUPERVISI -> {
                        pasienOptionalForms.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }

        // Initialize Puskesmas Spinner
        val puskesmasAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            Config.PUSKESMAS_LIST
        )
        puskesmasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        puskesmas.getSpinner().adapter = puskesmasAdapter

    }

    private fun verifAndCreate() {
        val passwordConfirm = confirmPassword.getEditText().text.toString().trim()
        user.category = category.getSpinner().selectedItemPosition
        user.name = name.getEditText().text.toString().trim()
        user.dateBorn = born.getTimeMills()
        user.weight = weight.getEditText().text.toString().toFloatOrNull()
        user.puskesmas = Config.PUSKESMAS_LIST[puskesmas.getSpinner().selectedItemPosition]
        user.phone = phone.getEditText().text.toString().trim()
        user.email = email.getEditText().text.toString().trim()
        user.password = password.getEditText().text.toString().trim()

        if (user.category == User.USER_CATEGORY_PASIEN) {
            user.pmoId = pmos[pmo.getSpinner().selectedItemPosition].id

            if (user.dateBorn == null) {
                toast("Tolong masukkan tanggal lahir anda!")
                return
            }

            if (user.weight == null) {
                toast("Tolong masukkan berat badan anda!")
                return
            }
        }

        if (user.name!!.length < Config.MIN_LENGTH_NAME) {
            toast("Nama minimal ${Config.MIN_LENGTH_NAME} karakter!")
            return
        }

        if (!Extension.isPhoneValid(user.phone)) {
            toast("Tolong masukkan nomer telepon dengan benar!")
            return
        }

        if (!Extension.isEmailValid(user.email)) {
            toast("Tolong masukkan email dengan benar!")
            return
        }

        if (user.password != passwordConfirm) {
            toast("Password dan konfirmasi password harus sama!")
            return
        }

        if (user.password!!.length < Config.MIN_LENGTH_PASSWORD) {
            toast("Password minimal ${Config.MIN_LENGTH_NAME} karakter!")
            return
        }

        signUp.showProgress()
        auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnSuccessListener {
                user.id = auth.currentUser?.uid
                if (user.category == User.USER_CATEGORY_PASIEN)
                    db.users.child(user.pmoId!!).child("pasienId").setValue(user.id)

                db.users.child(user.id!!).setValue(user)
                    .addOnSuccessListener {
                        signUp.hideProgress()
                        writeUserToSP(user)
                        toast("Berhasil membuat user")
                        setResult(Activity.RESULT_OK)
                        finish()
                    }.addOnFailureListener {
                        signUp.hideProgress()
                        toast("Gagal menambah ke database!")
                    }
            }.addOnFailureListener {
                toast("Gagal membuat akun! ${it.message}")
                signUp.hideProgress()
            }
    }

    private fun getPMO() {
        val pmoAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            pmosSpinnerVal
        )
        pmoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pmo.getSpinner().adapter = pmoAdapter
        pmo.showProgress()

        db.pmos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                pmos.clear()
                pmosSpinnerVal.clear()
                for (snapshot in data.children) {
                    val pmo = snapshot.getValue(User::class.java)!!
                    if (pmo.pasienId == null) {
                        pmos.add(pmo)
                        pmosSpinnerVal.add(pmo.name!!)
                    }
                }
                pmoAdapter.notifyDataSetChanged()
                pmo.hideProgress()
            }

            override fun onCancelled(error: DatabaseError) {
                pmo.hideProgress()
            }
        })
    }
}
