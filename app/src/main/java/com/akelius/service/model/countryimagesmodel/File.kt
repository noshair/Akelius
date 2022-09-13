package com.akelius.service.model.countryimagesmodel

import androidx.room.Entity
import androidx.room.PrimaryKey

data class File(
    val path: String,
    val stats: Stats
)
data class FileCheck(
    val status:String,
    val file: File
)
@Entity(tableName = "file")
data class FileData(
    val path: String,
    val atime: String,
    val atimeMs: Long,
    val birthtime: String,
    val birthtimeMs: Double,
    val blksize: Int,
    val blocks: Int,
    val ctime: String,
    val ctimeMs: Double,
    val dev: Int,
    val gid: Int,
    @PrimaryKey(autoGenerate = false)
    val ino: Int,
    val mode: Int,
    val mtime: String,
    val mtimeMs: Long,
    val nlink: Int,
    val rdev: Int,
    val size: Int,
    val uid: Int
    )