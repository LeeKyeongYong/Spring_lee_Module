package com.krstudy.kapi.domain.excel.view

import com.krstudy.kapi.domain.excel.entity.PostDetails
import com.krstudy.kapi.domain.excel.repository.PostDetailsRepository
import com.krstudy.kapi.standard.base.AbstractExcelView
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PostExcelView : AbstractExcelView<PostDetails>() {

    @Autowired
    private lateinit var postDetailsRepository: PostDetailsRepository

    override fun getData(request: HttpServletRequest): List<PostDetails> {
        return postDetailsRepository.findAll() // 실제 필요한 데이터 조회 로직으로 변경
    }

    override fun fillData(sheet: Sheet, items: List<PostDetails>, bodyStyle: CellStyle) {
        items.forEachIndexed { index, post ->
            post?.let {
                val row = sheet.createRow(4 + index)

                row.createCell(0).setCellValue(it.postId?.toDouble() ?: 0.0)
                row.createCell(1).setCellValue(it.postTitle ?: "")
                row.createCell(2).setCellValue(it.postCreateDate?.toString() ?: "")
                row.createCell(3).setCellValue(it.postModifyDate?.toString() ?: "")
                row.createCell(4).setCellValue(it.postHit?.toDouble() ?: 0.0)
                row.createCell(5).setCellValue(it.postAuthorId ?: "")
                row.createCell(6).setCellValue(it.commentId?.toDouble() ?: 0.0)
                row.createCell(7).setCellValue(it.commentAuthorId ?: "")
                row.createCell(8).setCellValue(it.commentCreateDate?.toString() ?: "")
                row.createCell(9).setCellValue(it.likeMemberId ?: "")
                row.createCell(10).setCellValue(it.likeCreateDate?.toString() ?: "")
                // 다른 필요한 데이터들도 추가
            } ?: run {
                // post가 null일 경우를 처리
                println("Warning: PostDetails object is null.")
            }
        }
    }
}