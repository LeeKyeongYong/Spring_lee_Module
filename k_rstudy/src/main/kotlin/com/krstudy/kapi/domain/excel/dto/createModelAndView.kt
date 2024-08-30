package com.krstudy.kapi.domain.excel.dto

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun createModelAndView(
    view: View,
    title: String,
    headerTitles: List<String>,
    startDay: String,
    endDay: String,
    sheetName: String,
    fileName: String
): ModelAndView {
    val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
    return ModelAndView(view).apply {
        addObject("title", title)
        addObject("headerTitles", headerTitles)
        addObject("startDate", startDay)
        addObject("endDate", endDay)
        addObject("sheetName", sheetName)
        addObject("fileName", "$encodedFileName.xls")
    }
}
