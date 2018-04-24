package com.aqrlei.graduation.yueting.model.local.infotool

import com.aqrlei.graduation.yueting.model.local.BookInfo


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/20 Time: 9:06
 */
/*
* 书籍信息共享单例
* */
enum class ShareBookInfo {
    BookInfoTool;

    companion object {
        private var bookInfoList = ArrayList<BookInfo>()
    }

    private var position: Int = 0

    fun getInfo(position: Int) = bookInfoList[position]
    fun getSize() = bookInfoList.size
    fun getInfoS() = bookInfoList
    fun setInfoS(infoS: ArrayList<BookInfo>) {
        clearBookInfo()
        bookInfoList.addAll(infoS)
    }

    fun addInfo(bookInfo: BookInfo) {
        bookInfoList.add(bookInfo)
    }

    fun removeInfo(info: BookInfo) {
        if (!bookInfoList.isEmpty()) {
            bookInfoList.remove(info)
        }
    }

    fun removeInfo(position: Int) {
        if (!bookInfoList.isEmpty()) {
            bookInfoList.removeAt(position)
        }
    }

    fun clearBookInfo() {
        bookInfoList.clear()
    }

    fun has(other: BookInfo): Boolean {
        if (bookInfoList.isNotEmpty()) {
            bookInfoList.forEachIndexed { _, bookInfo ->
                if (bookInfo == other) {
                    return true
                }
            }
        }
        return false
    }

    fun has(path: String): Int {
        if (bookInfoList.isNotEmpty()) {
            bookInfoList.forEachIndexed { intex, bookInfo ->
                if (bookInfo.path == path) {
                    return intex
                }
            }
        }
        return -1
    }

    fun same(path: String): Boolean {
        if (bookInfoList.isNotEmpty()) {
            bookInfoList.forEachIndexed { _, bookInfo ->
                if (bookInfo.path == path) {
                    return true
                }
            }
        }
        return false
    }

    fun setBookInfoIndex(path: String, begin: Int, end: Int) {
        if (has(path) != -1) {
            val p = has(path)
            bookInfoList[p].indexBegin = begin
            bookInfoList[p].indexEnd = end
        }
    }

    fun setPosition(p: Int) {
        position = p
    }
}