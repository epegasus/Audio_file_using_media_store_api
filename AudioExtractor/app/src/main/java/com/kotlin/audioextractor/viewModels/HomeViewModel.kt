package com.kotlin.audioextractor.viewModels

import android.app.Application
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kotlin.audioextractor.models.FileItem
import com.kotlin.audioextractor.models.Folder
import com.kotlin.audioextractor.utils.ConstantUtils.Companion.TAG
import com.kotlin.audioextractor.utils.GeneralUtils.getDate
import com.kotlin.audioextractor.utils.GeneralUtils.getFileSize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var fileList = ArrayList<FileItem>()
    private var folderList = ArrayList<Folder>()
    val fileListMutableLiveData: MutableLiveData<ArrayList<FileItem>> = MutableLiveData<ArrayList<FileItem>>()
    val folderListMutableLiveData: MutableLiveData<ArrayList<Folder>> = MutableLiveData<ArrayList<Folder>>()

    private var exceptionHandler = CoroutineExceptionHandler { contextCoroutine, throwable ->
        Log.d(TAG, "Exception: HomeViewModel: (Context: $contextCoroutine): $throwable")
    }

    fun fetchAudioFiles() {
        CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val projection = arrayOf(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DATE_MODIFIED,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DATA,
                )

                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                else
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val orderBy = MediaStore.Audio.Media.SIZE + " DESC"

                val cursor: Cursor? = getApplication<Application>().contentResolver.query(
                    collection,
                    projection,
                    null,
                    null,
                    orderBy
                )

                cursor?.let {
                    val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val addedCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                    val modifiedCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
                    val sizeCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

                    if (it.moveToFirst()) {
                        do {
                            val data = it.getString(dataCol)
                            val dateAdded = it.getLong(addedCol)
                            val dateModified = it.getLong(modifiedCol)
                            val size = it.getLong(sizeCol)
                            val file = File(data)
                            val fileItem = FileItem(
                                file,
                                file.name,
                                file.parentFile?.name ?: "root",
                                getDate(dateAdded * 1000),
                                getDate(dateModified * 1000),
                                getFileSize(size),
                                dateAdded,
                                dateModified,
                                size,
                                false
                            )
                            fileList.add(fileItem)
                            Log.d(TAG, "filePath: ${fileItem.pdfFilePath}")

                            file.parentFile?.let {
                                val folderName: String = file.parentFile?.name ?: "none"
                                val folder = Folder(folderName)
                                if (!folderList.contains(folder))
                                    folderList.add(folder)
                            }
                        } while (it.moveToNext())
                    }
                    it.close()
                }
            }.join()
            fileListMutableLiveData.value = fileList
            folderListMutableLiveData.value = folderList
        }
    }

}

