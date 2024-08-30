package com.krstudy.kapi.standard.base

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import org.springframework.web.servlet.view.document.AbstractXlsView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

abstract class AbstractExcelView<T> : AbstractXlsView() {
    abstract fun getData(request: HttpServletRequest): List<T>
    abstract fun fillData(sheet: Sheet, items: List<T>, bodyStyle: CellStyle)

    @Throws(Exception::class)
    override fun buildExcelDocument(model: Map<String, Any>, workbook: Workbook, request: HttpServletRequest, response: HttpServletResponse) {
        val title = model["title"] as String? ?: "Excel Report"
        val headerTitles = model["headerTitles"] as List<String>
        val startDateStr = model["startDate"] as String
        val endDateStr = model["endDate"] as String
        val sheetName = model["sheetName"] as String? ?: "Sheet1"
        val fileName = (model["fileName"] as String?)?.let {
            if (it.endsWith(".xls")) it else "$it.xls"
        } ?: "${title}_${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}.xls"

        val items = getData(request)

        response.setHeader("Content-Disposition", "attachment; filename=\"$fileName\"")

        val titleStyle = createTitleStyle(workbook)
        val headStyle = createHeadStyle(workbook)
        val bodyStyle = createBodyStyle(workbook)

        val sheet = workbook.createSheet(sheetName).apply {
            isDisplayGridlines = false
            autoSizeColumn(0)
        }

        createHeader(sheet, title, headerTitles, startDateStr, endDateStr, titleStyle, headStyle)
        fillData(sheet, items, bodyStyle)
        //addTotalRow(sheet, headerTitles.size, bodyStyle)
    }

    private fun createTitleStyle(workbook: Workbook): CellStyle {
        return workbook.createCellStyle().apply {
            val font = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 16
            }
            setFont(font)
        }
    }

    private fun createHeadStyle(workbook: Workbook): CellStyle {
        return workbook.createCellStyle().apply {
            val font = workbook.createFont().apply {
                color = IndexedColors.WHITE.index
            }
            alignment = HorizontalAlignment.CENTER
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
            fillForegroundColor = HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(font)
        }
    }

    private fun createBodyStyle(workbook: Workbook): CellStyle {
        return workbook.createCellStyle().apply {
            dataFormat = workbook.createDataFormat().getFormat("#,##0")
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }
    }

    private fun createHeader(sheet: Sheet, title: String, headerTitles: List<String>, startDateStr: String, endDateStr: String, titleStyle: CellStyle, headStyle: CellStyle) {
        val row0 = sheet.createRow(0).apply {
            createCell(0).apply {
                setCellValue(title)
                cellStyle = titleStyle
            }
        }

        val row1 = sheet.createRow(1).apply {
            val startDate = parseDate(startDateStr)
            val endDate = parseDate(endDateStr)
            createCell(0).setCellValue("조회기간 : ${startDate} ~ ${endDate}")
        }

        val row3 = sheet.createRow(3).apply {
            headerTitles.forEachIndexed { index, headerTitle ->
                createCell(index).apply {
                    setCellValue(headerTitle)
                    cellStyle = headStyle
                }
            }
        }
    }

    private fun parseDate(dateStr: String): String {

        val localDateFormats = listOf(
            DateTimeFormatter.ofPattern("yyyyMMdd"), // "20240801"
            DateTimeFormatter.ofPattern("yyyy-MM-dd") // "2024-08-01"
        )

        val localDateTimeFormats = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // "2024-08-01T19:29:54"
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX") // "2024-08-01T19:29:54.082+09:00"
        )

        for (format in localDateFormats) {
            try {
                val date = LocalDate.parse(dateStr, format)
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: DateTimeParseException) {
                logger.warn("DateTimeParseException1 value: $e")
            }
        }

        for (format in localDateTimeFormats) {
            try {
                val dateTime = LocalDateTime.parse(dateStr, format)
                return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: DateTimeParseException) {
                logger.warn("DateTimeParseException2 value: $e")
            }
        }

        logger.warn("Date parsing failed for value: $dateStr")
        return "Invalid Date"
    }

    private fun addTotalRow(sheet: Sheet, numberOfColumns: Int, bodyStyle: CellStyle) {
        val row = sheet.createRow(sheet.lastRowNum + 1)
        val cell = row.createCell(0).apply {
            setCellValue("Total")
            cellStyle = bodyStyle
        }
        for (i in 1 until numberOfColumns) {
            val cell = row.createCell(i)
            cell.cellStyle = bodyStyle
        }
    }
}