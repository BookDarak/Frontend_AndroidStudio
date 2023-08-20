package com.cookandroid.bookdarak_1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cookandroid.bookdarak_1.DAO.HistoryDao
import com.cookandroid.bookdarak_1.DAO.ReviewDao
import model.Find_History
import model.Find_Review

@Database(entities = [Find_History::class,Find_Review::class], version = 2)
abstract class FindBookDataBase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao

}

fun getAppDatabase(context: Context): FindBookDataBase {

    val migration_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("CREATE TABLE `FIND_REVIEW` ('isbn' TEXT, `Find_Review` TEXT," + "PRIMARY KEY(`isbn`))")
        }

    }

    return Room.databaseBuilder(
        context,
        FindBookDataBase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}