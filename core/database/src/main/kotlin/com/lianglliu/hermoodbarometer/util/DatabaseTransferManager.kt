package com.lianglliu.hermoodbarometer.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.system.exitProcess
import net.lingala.zip4j.ZipFile
import timber.log.Timber

class DatabaseTransferManager
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val databaseOpenHelper: SupportSQLiteOpenHelper,
) {

    fun export(backupFileUri: Uri) {
        try {
            val dbFiles = dbFiles()
            checkpoint()

            ZipFile(dbFiles.backupZipFile)
                .addFiles(listOf(dbFiles.dbFile, dbFiles.walFile, dbFiles.shmFile))

            val outputStream = context.contentResolver.openOutputStream(backupFileUri)!!
            Files.copy(dbFiles.backupZipFile?.toPath(), outputStream)
        } catch (e: IOException) {
            Timber.e(e, "Failed to export the database.")
        }
    }

    fun import(backupFileUri: Uri, restart: Boolean = true) {
        try {
            val dbFiles = dbFiles()

            dbFiles.backupZipFile?.delete()

            val inputStream = context.contentResolver.openInputStream(backupFileUri)!!
            Files.copy(inputStream, dbFiles.backupZipFile?.toPath())

            dbFiles.dbFile.delete()
            dbFiles.walFile?.delete()
            dbFiles.shmFile?.delete()

            ZipFile(dbFiles.backupZipFile).extractAll(dbFiles.dbFile.parent)
            checkpoint()
        } catch (e: IOException) {
            Timber.e(e, "Failed to import the database.")
        }

        if (restart) {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            exitProcess(0)
        }
    }

    private fun checkpoint() {
        val db = databaseOpenHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);")
        db.query("PRAGMA wal_checkpoint(TRUNCATE);")
    }

    private fun dbFiles() =
        DbFiles(
            dbFile = File(databaseOpenHelper.readableDatabase.path!!),
            walFile = File(databaseOpenHelper.readableDatabase.path + "-wal"),
            shmFile = File(databaseOpenHelper.readableDatabase.path + "-shm"),
            backupZipFile = File(databaseOpenHelper.readableDatabase.path + ".zip"),
        )
}

data class DbFiles(
    val dbFile: File,
    val walFile: File?,
    val shmFile: File?,
    val backupZipFile: File?,
)
