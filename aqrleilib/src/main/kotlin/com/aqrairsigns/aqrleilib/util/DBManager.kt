package com.aqrairsigns.aqrleilib.util

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aqrairsigns.aqrleilib.info.DataTableInfo

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 15:00
 */
object DBManager {
    private var db: SQLiteDatabase? = null
    private var mContext: Context? = null
    private var mDBName: String = " "
    /*invoke first*/
    fun initDBHelper(context: Context, dbName: String, version: Int): DBManager {
        mContext = context
        mDBName = dbName
        DBHelper.init(context, dbName, version)
        return this
    }

    /*invoke after {@link initDBHelper(context: Context, dbName: String, version: Int)}*/
    fun addTable(name: String, fileId: Array<String>, fileType: Array<String>): DBManager {
        DBHelper.addTable(name, fileId, fileType)
        return this
    }

    fun createDB() {
        db = DBHelper.dbHelper.writableDatabase
    }

    fun removeTable(name: String) {
        DBHelper.dbHelper.remove(name)
    }

    fun removeAllTable() {
        DBHelper.dbHelper.clear()
    }

    fun closeDB() {
        db?.close()
    }

    fun deleteDB() = mContext?.deleteDatabase(mDBName)
    fun deleteDB(name: String) = mContext?.deleteDatabase(name)
    fun deleteDB(context: Context) = context.deleteDatabase(mDBName)
    fun deleteDB(context: Context, name: String) = context.deleteDatabase(name)

    fun insertData(sql: String, data: Array<Any>): DBManager {
        db?.beginTransaction()
        try {
            db?.execSQL(sql, data)
            db?.setTransactionSuccessful()

        } finally {
            db?.endTransaction()
        }
        return this
    }

    fun deleteData(sql: String, dataWhere: Array<String>?): DBManager {
        db?.beginTransaction()
        try {
            db?.execSQL(sql, dataWhere)
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
        return this
    }

    fun updateData(sql: String, data: Array<String>): DBManager {
        db?.beginTransaction()
        try {
            db?.execSQL(sql, data)
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
        return this
    }

    fun queryData(sql: String, dataWhere: Array<String>? = null) =
            db?.rawQuery(sql, dataWhere)

    private class DBHelper private constructor(
            context: Context?,
            name: String?,
            factory: SQLiteDatabase.CursorFactory? = null,
            version: Int,
            errorHandler: DatabaseErrorHandler? = null
    ) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {
        private var mDeleteTableInfoList: ArrayList<DataTableInfo>? = null

        companion object {

            val dbHelper: DBHelper
                get() = Singleton.INSTANCE.getInstance(mContext, mName, mVersion)
            private var mTableInfoList = ArrayList<DataTableInfo>()
            private var mContext: Context? = null
            private var mName: String? = null
            private var mVersion: Int = 0
            fun addTable(name: String, fileId: Array<String>, fileType: Array<String>) {
                if (fileId.size != fileType.size) {
                    throw IllegalArgumentException("fileId and fileType arrays must be of equal length")
                }
                val temp = DataTableInfo()
                temp.name = name
                temp.fileId = fileId
                temp.fileType = fileType
                if (!mTableInfoList.contains(temp))
                    mTableInfoList.add(temp)
            }

            fun init(context: Context, name: String, version: Int) {
                mContext = context
                mName = name
                mVersion = version
            }

        }

        init {
            mDeleteTableInfoList = ArrayList()
        }


        fun remove(name: String): Boolean {
            if (!mTableInfoList.isEmpty()) {
                if (mTableInfoList.remove(DataTableInfo(name))) {
                    mDeleteTableInfoList?.add(DataTableInfo(name))
                    return true
                }
                return false
            } else {
                return false
            }
        }

        fun clear() {
            if (!mTableInfoList.isEmpty()) {
                mDeleteTableInfoList?.addAll(mTableInfoList)
                mTableInfoList.clear()
            }
        }

        override fun onCreate(db: SQLiteDatabase?) {
            mTableInfoList.forEach {
                var sql = "create table ${it.name}( _id integer primary key autoincrement"
                for (i in it.fileId!!.indices) {
                    sql += ", "
                    sql += (it.fileId!![i] + " " + it.fileType!![i])
                }
                sql += ")"
                db?.execSQL(sql)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            mDeleteTableInfoList?.forEach {
                db?.execSQL("drop table if exists " + it.name)
            }
            mTableInfoList.forEach {
                db?.execSQL("drop table if exists " + it.name)
            }
            onCreate(db)
        }

        private enum class Singleton {
            INSTANCE;

            fun getInstance(context: Context?, name: String?, version: Int): DBHelper {
                if (context == null) {
                    throw RuntimeException("must invoke init ahead")
                }
                if (mTableInfoList.isEmpty()) {
                    throw RuntimeException("must invoke addTab ahead")
                }
                return DBHelper(context.applicationContext, name, null, version, null)
            }

        }
    }

    object SqlFormat {

        fun insertSqlFormat(tableName: String, fileIdList: Array<String>): String {
            var sql = "insert into $tableName( "
            fileIdList.indices
                    .filter { it != fileIdList.lastIndex }
                    .forEach { sql += "${fileIdList[it]}, " }
            sql += "${fileIdList.last()}) values("
            fileIdList.indices
                    .filter { it != fileIdList.lastIndex }
                    .forEach { sql += "?, " }
            sql += "?)"

            return sql
        }

        fun deleteSqlFormat(tableName: String, fileId: String, where: String) =
                "delete from $tableName where $fileId $where ?"

        fun updateSqlFormat(tableName: String, fileId: String, where: String = "") =
                "update $tableName  set $fileId = ?" +
                        if (where == "") where
                        else " where $where = ?"


        fun selectSqlFormat(tableName: String, fileId: String = "", where: String = "") =
                "select" +
                        if (fileId == "") " * from $tableName"
                        else {
                            " $fileId from $tableName" +
                                    if (where == "") where
                                    else " where $where = ?"
                        }
    }
}