package com.example.mycoffee

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalTime
import java.util.Date

class BaseDatos(context: Context): SQLiteOpenHelper(context,BaseDatos.NOMBRE_BASE_DATOS,null,BaseDatos.VERSION_BASE_DATOS) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREAR_TABLA_LUGARES = "CREATE TABLE $NOMBRE_TABLA ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$NOMBRE TEXT,$DESCRIPCION TEXT,$LATITUD FLOAT,$LONGITUD FLOAT);"
        val CREAR_TABLA_RESERVS = "CREATE TABLE $NOMBRE_TABLA_RESERVS ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$NOMBRE TEXT,$NUMBERPHONE TEXT,$DATE TEXT,$HOUR TEXT);"
        val CREAR_TABLA_PRODUCTS = "CREATE TABLE $NOMBRE_TABLA_PRODUCTS ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$NOMBRE TEXT,$DESCRIPCION TEXT,$PRECIO FLOAT,$STATUS INTEGER);"
        val CREAR_TABLA_PROMOTIONS = "CREATE TABLE $NOMBRE_TABLA_PROMOTIONS ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$ID_PRODUCTO INTEGER,$PORCENTAJE_DESCUENTO FLOAT, FOREIGN KEY($ID_PRODUCTO) REFERENCES $NOMBRE_TABLA_PRODUCTS($ID));"
        val CREAR_TABLA_HOURS = "CREATE TABLE $NOMBRE_TABLA_HOURS ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$DIA TEXT,$HORA_INI TEXT,$HORA_FIN TEXT,$STATUS INTEGER);"

        db?.execSQL(CREAR_TABLA_LUGARES)
        db?.execSQL(CREAR_TABLA_RESERVS)
        db?.execSQL(CREAR_TABLA_PRODUCTS)
        db?.execSQL(CREAR_TABLA_PROMOTIONS)
        db?.execSQL(CREAR_TABLA_HOURS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val BORRAR_TABLA_LUGARES = "DROP TABLE IF EXISTS $NOMBRE_TABLA"
        val BORRAR_TABLA_RESERVS = "DROP TABLE IF EXISTS $NOMBRE_TABLA_RESERVS"
        val BORRAR_TABLA_PRODUCTS = "DROP TABLE IF EXISTS $NOMBRE_TABLA_PRODUCTS"
        val BORRAR_TABLA_PROMOTIONS = "DROP TABLE IF EXISTS $NOMBRE_TABLA_PROMOTIONS"
        val BORRAR_TABLA_HOURS = "DROP TABLE IF EXISTS $NOMBRE_TABLA_HOURS"

        db?.execSQL(BORRAR_TABLA_LUGARES)
        db?.execSQL(BORRAR_TABLA_RESERVS)
        db?.execSQL(BORRAR_TABLA_PRODUCTS)
        db?.execSQL(BORRAR_TABLA_PROMOTIONS)
        db?.execSQL(BORRAR_TABLA_HOURS)

        onCreate(db)
    }

    /*--------------------------------Lugares--------------------------------*/
    fun addLugar(lugares: Lugares):Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE,lugares.nombre)
        values.put(DESCRIPCION,lugares.descripcion)
        values.put(LATITUD,lugares.latitud)
        values.put(LONGITUD,lugares.longitud)
        val _success = db.insert(NOMBRE_TABLA,null,values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    @SuppressLint("Range")
    fun getLugar(_id: Integer): Lugares{
        val lugares = Lugares()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            cursor.moveToFirst()
            while(cursor.moveToNext()){
                lugares.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                lugares.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                lugares.descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION))
                lugares.latitud =  cursor.getString(cursor.getColumnIndex(LATITUD)).toDouble()
                lugares.longitud = cursor.getString(cursor.getColumnIndex(LONGITUD)).toDouble()
            }
        }
        cursor.close()
        return lugares
    }

    val lugar: List<Lugares> @SuppressLint("Range")
    get(){
        val lugaresList = ArrayList<Lugares>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            cursor.moveToFirst()
            while(cursor.moveToNext()){
                val lugares = Lugares()
                lugares.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                lugares.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                lugares.descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION))
                lugares.latitud = cursor.getString(cursor.getColumnIndex(LATITUD)).toDouble()
                lugares.longitud = cursor.getString(cursor.getColumnIndex(LONGITUD)).toDouble()
                lugaresList.add(lugares)
            }
        }
        cursor.close()
        return lugaresList
    }

    fun updateLugar(lugares: Lugares): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE,lugares.nombre)
        values.put(DESCRIPCION,lugares.descripcion)
        values.put(LATITUD,lugares.latitud)
        values.put(LONGITUD,lugares.longitud)
        val _success = db.update(NOMBRE_TABLA,values,ID+"=?",arrayOf(lugares.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteLugar(_id: Int): Boolean{
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA,ID+"=?",arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllLugares(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
    /*--------------------------------Productos--------------------------------*/
    fun addProduct(product: Products): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, product.nombre)
        values.put(DESCRIPCION, product.descripcion)
        values.put(PRECIO, product.precio)
        values.put(STATUS, product.status)
        val _success = db.insert(NOMBRE_TABLA_PRODUCTS, null, values)
        db.close()
        return _success != -1L
    }

    @SuppressLint("Range")
    fun getProduct(_id: Int): Products {
        val product = Products()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA_PRODUCTS WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                product.id = cursor.getInt(cursor.getColumnIndex(ID))
                product.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                product.descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION))
                product.precio = cursor.getFloat(cursor.getColumnIndex(PRECIO))
                product.status = cursor.getInt(cursor.getColumnIndex(STATUS))
            }
            cursor.close()
        }
        return product
    }

    val productos: List<Products>
        @SuppressLint("Range")
        get() {
            val productList = ArrayList<Products>()
            val db = this.readableDatabase
            val selectQuery = "SELECT * FROM $NOMBRE_TABLA_PRODUCTS"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val product = Products()
                    product.id = cursor.getInt(cursor.getColumnIndex(ID))
                    product.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                    product.descripcion = cursor.getString(cursor.getColumnIndex(DESCRIPCION))
                    product.precio = cursor.getFloat(cursor.getColumnIndex(PRECIO))
                    product.status = cursor.getInt(cursor.getColumnIndex(STATUS))
                    productList.add(product)
                }
                cursor.close()
            }
            return productList
        }

    fun updateProduct(product: Products): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, product.nombre)
        values.put(DESCRIPCION, product.descripcion)
        values.put(PRECIO, product.precio)
        values.put(STATUS, product.status)
        val _success = db.update(NOMBRE_TABLA_PRODUCTS, values, "$ID=?", arrayOf(product.id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteProduct(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_PRODUCTS, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteAllProducts(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_PRODUCTS, null, null).toLong()
        db.close()
        return _success != -1L
    }

    /*--------------------------------Promociones--------------------------------*/
    fun addPromotion(promotion: Promotions): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_PRODUCTO, promotion.idProducto)
        values.put(PORCENTAJE_DESCUENTO, promotion.porcentajeDescuento)
        val _success = db.insert(NOMBRE_TABLA_PROMOTIONS, null, values)
        db.close()
        return _success != -1L
    }

    @SuppressLint("Range")
    fun getPromotion(_id: Int): Promotions {
        val promotion = Promotions()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA_PROMOTIONS WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                promotion.id = cursor.getInt(cursor.getColumnIndex(ID))
                promotion.idProducto = cursor.getInt(cursor.getColumnIndex(ID_PRODUCTO))
                promotion.porcentajeDescuento = cursor.getFloat(cursor.getColumnIndex(PORCENTAJE_DESCUENTO))
            }
            cursor.close()
        }
        return promotion
    }

    val promotions: List<Promotions>
        @SuppressLint("Range")
        get() {
            val promotionList = ArrayList<Promotions>()
            val db = this.readableDatabase
            val selectQuery = "SELECT * FROM $NOMBRE_TABLA_PROMOTIONS"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val promotion = Promotions()
                    promotion.id = cursor.getInt(cursor.getColumnIndex(ID))
                    promotion.idProducto = cursor.getInt(cursor.getColumnIndex(ID_PRODUCTO))
                    promotion.porcentajeDescuento = cursor.getFloat(cursor.getColumnIndex(PORCENTAJE_DESCUENTO))
                    promotionList.add(promotion)
                }
                cursor.close()
            }
            return promotionList
        }

    fun updatePromotion(promotion: Promotions): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_PRODUCTO, promotion.idProducto)
        values.put(PORCENTAJE_DESCUENTO, promotion.porcentajeDescuento)
        val _success = db.update(NOMBRE_TABLA_PROMOTIONS, values, "$ID=?", arrayOf(promotion.id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deletePromotion(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_PROMOTIONS, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteAllPromotions(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_PROMOTIONS, null, null).toLong()
        db.close()
        return _success != -1L
    }

    /*--------------------------------Reservas--------------------------------*/
    fun addReserv(reserv: Reservs): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, reserv.nombre)
        values.put(NUMBERPHONE, reserv.numberphone)
        values.put(DATE, reserv.date.toString())
        values.put(HOUR, reserv.hour.toString())
        val _success = db.insert(NOMBRE_TABLA_RESERVS, null, values)
        db.close()
        return _success != -1L
    }

    @SuppressLint("Range")
    fun getReserv(_id: Int): Reservs {
        val reserv = Reservs()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA_RESERVS WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                reserv.id = cursor.getInt(cursor.getColumnIndex(ID))
                reserv.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                reserv.numberphone = cursor.getString(cursor.getColumnIndex(NUMBERPHONE))
                reserv.date = Date(cursor.getString(cursor.getColumnIndex(DATE)))
                reserv.hour = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HOUR)))
            }
            cursor.close()
        }
        return reserv
    }

    val reservs: List<Reservs>
        @SuppressLint("Range")
        get() {
            val reservList = ArrayList<Reservs>()
            val db = this.readableDatabase
            val selectQuery = "SELECT * FROM $NOMBRE_TABLA_RESERVS"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val reserv = Reservs()
                    reserv.id = cursor.getInt(cursor.getColumnIndex(ID))
                    reserv.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                    reserv.numberphone = cursor.getString(cursor.getColumnIndex(NUMBERPHONE))
                    reserv.date = Date(cursor.getString(cursor.getColumnIndex(DATE)))
                    reserv.hour = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HOUR)))
                    reservList.add(reserv)
                }
                cursor.close()
            }
            return reservList
        }

    fun updateReserv(reserv: Reservs): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, reserv.nombre)
        values.put(NUMBERPHONE, reserv.numberphone)
        values.put(DATE, reserv.date.toString())
        values.put(HOUR, reserv.hour.toString())
        val _success = db.update(NOMBRE_TABLA_RESERVS, values, "$ID=?", arrayOf(reserv.id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteReserv(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_RESERVS, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteAllReservs(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_RESERVS, null, null).toLong()
        db.close()
        return _success != -1L
    }

    /*--------------------------------Horarios--------------------------------*/
    fun addHour(hour: Hours): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DIA, hour.dia)
        values.put(HORA_INI, hour.horaIni.toString())
        values.put(HORA_FIN, hour.horaFin.toString())
        values.put(STATUS, hour.status)
        val _success = db.insert(NOMBRE_TABLA_HOURS, null, values)
        db.close()
        return _success != -1L
    }

    @SuppressLint("Range")
    fun getHour(_id: Int): Hours {
        val hour = Hours()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $NOMBRE_TABLA_HOURS WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                hour.id = cursor.getInt(cursor.getColumnIndex(ID))
                hour.dia = cursor.getString(cursor.getColumnIndex(DIA))
                hour.horaIni = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HORA_INI)))
                hour.horaFin = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HORA_FIN)))
                hour.status = cursor.getInt(cursor.getColumnIndex(STATUS))
            }
            cursor.close()
        }
        return hour
    }

    val hours: List<Hours>
        @SuppressLint("Range")
        get() {
            val hourList = ArrayList<Hours>()
            val db = this.readableDatabase
            val selectQuery = "SELECT * FROM $NOMBRE_TABLA_HOURS"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val hour = Hours()
                    hour.id = cursor.getInt(cursor.getColumnIndex(ID))
                    hour.dia = cursor.getString(cursor.getColumnIndex(DIA))
                    hour.horaIni = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HORA_INI)))
                    hour.horaFin = LocalTime.parse(cursor.getString(cursor.getColumnIndex(HORA_FIN)))
                    hour.status = cursor.getInt(cursor.getColumnIndex(STATUS))
                    hourList.add(hour)
                }
                cursor.close()
            }
            return hourList
        }

    fun updateHour(hour: Hours): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DIA, hour.dia)
        values.put(HORA_INI, hour.horaIni.toString())
        values.put(HORA_FIN, hour.horaFin.toString())
        values.put(STATUS, hour.status)
        val _success = db.update(NOMBRE_TABLA_HOURS, values, "$ID=?", arrayOf(hour.id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteHour(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_HOURS, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return _success != -1L
    }

    fun deleteAllHours(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(NOMBRE_TABLA_HOURS, null, null).toLong()
        db.close()
        return _success != -1L
    }

    companion object {
        private val VERSION_BASE_DATOS = 1
        private val NOMBRE_BASE_DATOS = "dbsis104"
        private val NOMBRE_TABLA = "lugares"
        private val NOMBRE_TABLA_RESERVS = "reservs"
        private val NOMBRE_TABLA_PRODUCTS = "products"
        private val NOMBRE_TABLA_PROMOTIONS = "promotions"
        private val NOMBRE_TABLA_HOURS = "hours"
        private val ID = "id"
        private val NOMBRE = "nombre"
        private val DESCRIPCION = "descripcion"
        private val LATITUD = "latitud"
        private val LONGITUD = "longitud"
        private val NUMBERPHONE = "numberphone"
        private val DATE = "date"
        private val HOUR = "hour"
        private val PRECIO = "precio"
        private val STATUS = "status"
        private val ID_PRODUCTO = "id_producto"
        private val PORCENTAJE_DESCUENTO = "porcentaje_descuento"
        private val DIA = "dia"
        private val HORA_INI = "hora_ini"
        private val HORA_FIN = "hora_fin"
    }
}
