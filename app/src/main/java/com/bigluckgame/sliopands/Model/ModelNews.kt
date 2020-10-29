package com.bigluckgame.sliopands.Model

import java.util.*

/*
 c сервера возвращается объект "ModelNews",внутри которого список обьектов Results
  по этому в модели данных пришлось делать вложенный data класс Results
  */
data class ModelNews(var results: ArrayList<Results>) {

    data class Results(
        var title: String,//заголовок новости
        var abstract: String,//описание
        var url: String,//ссылка на источник
        //ссылка на изображение
        var thumbnail_standard: String) {

    }
}


