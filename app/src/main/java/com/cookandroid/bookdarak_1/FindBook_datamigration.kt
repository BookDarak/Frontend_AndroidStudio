package com.cookandroid.bookdarak_1


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dao.FindBook_historyDAO
import dao.FindBook_reviewDAO
import model.FindBook_historymodel
import model.FindBook_reviewmodel

@Database(entities = [FindBook_historymodel::class, FindBook_reviewmodel::class], version = 1)
abstract class FindBook_datamigration : RoomDatabase(){
    abstract fun historyDao(): FindBook_historyDAO
    abstract fun reviewDao(): FindBook_reviewDAO

}

fun getAppDatabase(context: Context): FindBook_datamigration {

    val migration_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("CREATE TABLE `REVIEW` ('id' INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
        }

    }

    return Room.databaseBuilder(
        context,
        FindBook_datamigration::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}