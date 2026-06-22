package pe.edu.cibertec.medic_salud.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SessionDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "medic_salud_session.db"
        const val DATABASE_VERSION = 1

        const val TABLE_SESSION = "session"
        const val COL_ID = "id"
        const val COL_ACCESS_TOKEN = "access_token"
        const val COL_REFRESH_TOKEN = "refresh_token"
        const val COL_EXPIRES_AT = "expires_at"
        const val COL_PATIENT_CODE = "patient_code"
        const val COL_NAMES = "names"
        const val COL_SURNAMES = "surnames"
        const val COL_EMAIL = "email"
        const val COL_MEDICAL_RECORD_NUMBER = "medical_record_number"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_SESSION (
                $COL_ID INTEGER PRIMARY KEY,
                $COL_ACCESS_TOKEN TEXT NOT NULL,
                $COL_REFRESH_TOKEN TEXT NOT NULL,
                $COL_EXPIRES_AT TEXT NOT NULL,
                $COL_PATIENT_CODE TEXT NOT NULL,
                $COL_NAMES TEXT NOT NULL,
                $COL_SURNAMES TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL,
                $COL_MEDICAL_RECORD_NUMBER TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SESSION")
        onCreate(db)
    }
}
