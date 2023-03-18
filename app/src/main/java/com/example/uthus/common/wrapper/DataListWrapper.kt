package com.example.uthus.common.wrapper

class  DataListWrapper <T>{
    var allowLoadMore : Boolean=false
    var dataList: MutableList<T> = mutableListOf()
    var currentPage : Int=1

    fun reset(){
        allowLoadMore =false
        dataList.clear()
        currentPage = 1
    }
}
