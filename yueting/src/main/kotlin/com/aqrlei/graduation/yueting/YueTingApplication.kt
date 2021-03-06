package com.aqrlei.graduation.yueting

import android.content.Intent
import android.os.StrictMode
import android.text.TextUtils
import com.aqrlei.graduation.yueting.basemvp.BaseApplication
import com.aqrlei.graduation.yueting.constant.CacheConstant
import com.aqrlei.graduation.yueting.constant.DataConstant
import com.aqrlei.graduation.yueting.model.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.service.MusicService
import com.aqrlei.graduation.yueting.util.AppCache
import com.aqrlei.graduation.yueting.util.DBManager
import com.aqrlei.graduation.yueting.util.DisposableHolder

/**
 * @Author: AqrLei
 * @CreateTime: Date: 2017/9/14 Time: 16:17
 */
class YueTingApplication : BaseApplication() {
    private val musicIntent: Intent
            by lazy {
                Intent(this, MusicService::class.java)
            }
    private var isSameProcess = false

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog().build())
        }
        super.onCreate()
        val processName = getProcessName(this)
        isSameProcess = !TextUtils.isEmpty(processName) && processName == this.packageName
        if (isSameProcess) {
            AppCache.init(this, CacheConstant.SF_NAME)
            DBManager
                    .initDBHelper(this, DataConstant.DB_NAME, 1)
                    .addTable(DataConstant.MUSIC_TABLE_NAME,
                            arrayOf(
                                    DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.MUSIC_TABLE_C1_TYPE_NAME
                            ),
                            arrayOf(
                                    DataConstant.MUSIC_TABLE_C0_DEF,
                                    DataConstant.MUSIC_TABLE_C1_DEF
                            )
                    )
                    .addTable(DataConstant.BOOK_TABLE_NAME,
                            arrayOf(
                                    DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.BOOK_TABLE_C1_TYPE,
                                    DataConstant.BOOK_TABLE_C2_INDEX_BEGIN,
                                    DataConstant.BOOK_TABLE_C3_INDEX_END,
                                    DataConstant.BOOK_TABLE_C4_TYPE_NAME
                            ),
                            arrayOf(
                                    DataConstant.BOOK_TABLE_C0_DEF,
                                    DataConstant.BOOK_TABLE_C1_DEF,
                                    DataConstant.BOOK_TABLE_C2_DEF,
                                    DataConstant.BOOK_TABLE_C3_DEF,
                                    DataConstant.BOOK_TABLE_C4_DEF
                            )
                    )
                    .addTable(DataConstant.CATALOG_TABLE_NAME,
                            arrayOf(
                                    DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.CATALOG_TABLE_C1_CATALOG_NAME,
                                    DataConstant.CATALOG_TABLE_C2_CATALOG_POSITION
                            ),
                            arrayOf(
                                    DataConstant.CATALOG_TABLE_C0_DEF,
                                    DataConstant.CATALOG_TABLE_C1_DEF,
                                    DataConstant.CATALOG_TABLE_C2_DEF
                            )
                    )
                    .addTable(DataConstant.MARK_TABLE_NAME,
                            arrayOf(
                                    DataConstant.COMMON_COLUMN_PATH,
                                    DataConstant.MARK_TABLE_C1_MARK_POSITION
                            ),
                            arrayOf(
                                    DataConstant.MARK_TABLE_C0_DEF,
                                    DataConstant.MARK_TABLE_C1_DEF
                            )
                    )
                    .addTable(DataConstant.TYPE_TABLE_NAME,
                            arrayOf(
                                    DataConstant.TYPE_TABLE_C0_NAME,
                                    DataConstant.TYPE_TABLE_C1_TYPE
                            ),
                            arrayOf(
                                    DataConstant.TYPE_TABLE_C0_DEF,
                                    DataConstant.TYPE_TABLE_C1_DEF
                            )
                    )
                    .createDB()
        }

    }

    override fun onTerminate() {
        if (isSameProcess) {
            DBManager.closeDB()
            unbindService(ShareMusicInfo.MusicInfoTool.conn)
            ShareMusicInfo.MusicInfoTool.clear()
            ShareMusicInfo.MusicInfoTool.clearMusicInfo()
            ShareBookInfo.BookInfoTool.clearBookInfo()
            DisposableHolder.dispose()
        }
        super.onTerminate()
    }

    fun getServiceIntent() = musicIntent
}