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
            val row = sheet.createRow(4 + index)
            row.createCell(0).setCellValue(post.postId?.toDouble() ?: 0.0) // postId를 사용
            row.createCell(1).setCellValue(post.postTitle ?: "")
            row.createCell(2).setCellValue(post.postCreateDate?.toString() ?: "")
            row.createCell(3).setCellValue(post.postModifyDate?.toString() ?: "")
            row.createCell(4).setCellValue(post.postHit?.toDouble() ?: 0.0)
            row.createCell(5).setCellValue(post.postAuthorId ?: "")
            row.createCell(6).setCellValue(post.commentId?.toDouble() ?: 0.0)
            row.createCell(7).setCellValue(post.commentAuthorId ?: "")
            row.createCell(8).setCellValue(post.commentCreateDate?.toString() ?: "")
            row.createCell(9).setCellValue(post.likeMemberId ?: "")
            row.createCell(10).setCellValue(post.likeCreateDate?.toString() ?: "")
            // 다른 필요한 데이터들도 추가
        }
    }
}