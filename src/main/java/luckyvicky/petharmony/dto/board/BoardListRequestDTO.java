package luckyvicky.petharmony.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListRequestDTO {
    private String category;  // 필터링할 카테고리 (all 경우 모든 카테고리 포함)
    private String sortBy;    // 정렬 기준 (date, views, comments 등)
    private int page;         // 요청할 페이지 번호
    private int size;         // 페이지당 항목 수
}
