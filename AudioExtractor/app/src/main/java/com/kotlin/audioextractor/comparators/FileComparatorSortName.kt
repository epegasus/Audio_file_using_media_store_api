package com.kotlin.audioextractor.comparators

import com.kotlin.audioextractor.models.FileItem

class FileComparatorSortName : Comparator<FileItem> {
    override fun compare(p0: FileItem?, p1: FileItem?): Int {
        return (p0!!.fileName).compareTo(p1!!.fileName)
    }
}