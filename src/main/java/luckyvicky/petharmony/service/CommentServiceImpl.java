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
     * @param commentPostDTO
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
     * @param commentUpdateDTO
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
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }
    }
}
