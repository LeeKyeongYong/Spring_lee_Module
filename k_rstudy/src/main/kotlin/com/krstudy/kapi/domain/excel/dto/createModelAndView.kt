package com.krstudy.kapi.domain.excel.dto

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View

fun createModelAndView(
    view: View,
    title: String,
    headerTitles: List<String>,
    startDay: String,
    endDay: String,
    sheetName: String,
    fileName: String
): ModelAndView {
    return ModelAndView(view).apply {
        addObject("title", title)
        addObject("headerTitles", headerTitles)
        addObject("startDate", startDay)
        addObject("endDate", endDay)
        addObject("sheetName", sheetName)
        addObject("fileName", fileName)
    }
}