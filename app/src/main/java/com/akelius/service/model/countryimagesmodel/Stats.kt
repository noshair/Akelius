package com.akelius.service.model.countryimagesmodel

data class Stats(
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
    val ino: Int,
    val mode: Int,
    val mtime: String,
    val mtimeMs: Long,
    val nlink: Int,
    val rdev: Int,
    val size: Int,
    val uid: Int
)