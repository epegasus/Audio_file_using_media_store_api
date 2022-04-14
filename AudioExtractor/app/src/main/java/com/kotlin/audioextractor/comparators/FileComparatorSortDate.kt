package com.kotlin.audioextractor.comparators

import com.kotlin.audioextractor.models.FileItem

class FileComparatorSortDate : Comparator<FileItem> {
    override fun compare(p0: FileItem?, p1: FileItem?): Int {
        return (p0!!.exactDateModified).compareTo(p1!!.exactDateModified)
    }
}