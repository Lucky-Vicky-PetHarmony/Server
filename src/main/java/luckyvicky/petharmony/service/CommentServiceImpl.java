package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.comment.CommentPostDTO;
import luckyvicky.petharmony.dto.comment.CommentUpdateDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.CommentRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글 작성
     *
     * @param commentPostDTO 작성자 id, 게시물 id, 댓글 내용
     * @throws IllegalArgumentException boardId가 없거나 userId가 없을때
     */
    @Override
    public void addComment(CommentPostDTO commentPostDTO) {
        User user = userRepository.findById(commentPostDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("유효하지않은 user ID"));
        Board board = boardRepository.findById(commentPostDTO.getBoardId()).orElseThrow(() -> new IllegalArgumentException("유효하지않은 board ID"));

        Comment comment = Comment.builder()
                .user(user)
                .board(board)
                .commContent(commentPostDTO.getCommContent())
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     *
     * @param commentUpdateDTO 댓글을 삭제하려는 사람 Id, 삭제하려는 댓글의 Id, 수정할 내용
     * @throws IllegalArgumentException commId가 없거나 댓글 작성자가 아닐때
     */
    @Override
    public void updateComment(CommentUpdateDTO commentUpdateDTO) {
        Comment comment = commentRepository.findById(commentUpdateDTO.getCommId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 comm ID"));

        if(comment.getUser().getUserId().equals(commentUpdateDTO.getUserId())) {
            Comment updateComment = comment.toBuilder()
                    .commContent(commentUpdateDTO.getCommContent())
                    .build();
            commentRepository.save(updateComment);
        }else{
            throw new IllegalArgumentException("댓글 작성자만 수정가능합니다.");
        }
    }

    /**
     * 댓글 삭제
     *
     * @param commId 삭제하려는 댓글 id
     * @param userId 삭제를 시도하는 유저 id
     * @throws IllegalArgumentException commId가 없거나 userId가 없거나 댓글 작성자가 아닐때
     */
    @Override
    public void deleteComment(Long userId, Long commId) {
        Comment comment = commentRepository.findById(commId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 comm ID"));

        if(comment.getUser().getUserId().equals(userId)) {
            commentRepository.deleteById(commId);
        }else{
            throw new IllegalArgumentException("댓글 작성자만 삭제가 가능합니다.");
        }


    }
}
