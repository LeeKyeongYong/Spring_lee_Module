package com.krstudy.kapi.standard.base

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellUtil
import org.springframework.web.servlet.view.document.AbstractXlsView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class AbstractExcelView<T> : AbstractXlsView() {
    abstract fun getData(request: HttpServletRequest): List<T>
    abstract fun fillData(sheet: Sheet, items: List<T>, bodyStyle: CellStyle)

    @Throws(Exception::class)
    override fun buildExcelDocument(model: Map<String, Any>, workbook: Workbook, request: HttpServletRequest, response: HttpServletResponse) {
        val title = model["title"] as String
        val headerTitles = model["headerTitles"] as List<String>
        val startDateStr = model["startDate"] as String
        val endDateStr = model["endDate"] as String
        val sheetName = model["sheetName"] as String? ?: "Sheet1"
        val fileName = model["fileName"] as String? ?: "$title_${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}.xls"
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
        addTotalRow(sheet, items.size, bodyStyle)
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
            createCell(0).setCellValue("조회기간 : ${formatDate(startDateStr)} - ${formatDate(endDateStr)}")
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

    private fun formatDate(dateStr: String): String {
        return try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    private fun addTotalRow(sheet: Sheet, itemCount: Int, bodyStyle: CellStyle) {
        val rowEnd = sheet.createRow(4 + itemCount)
        rowEnd.createCell(0).apply {
            setCellValue("총계")
            cellStyle = bodyStyle
        }
        rowEnd.createCell(1).apply {
            setCellValue(sheet.rowIterator().asSequence()
                .drop(4)
                .take(itemCount)
                .sumOf { row -> row.getCell(1).numericCellValue })
            cellStyle = bodyStyle
        }
    }
}