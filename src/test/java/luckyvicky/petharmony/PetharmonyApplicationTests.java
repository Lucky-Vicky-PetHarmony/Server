package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Category;
import luckyvicky.petharmony.entity.board.Comment;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PetHarmonyApplicationTests {

	@Autowired
	private PetInfoRepository petInfoRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ImageRepository imageRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testBoardCreation() {

		User user = User.builder()
				.userName("Test User")
				.email("test@example.com")
				.password("password")
				.phone("1234567890")
				.address("Test Address")
				.build();
		userRepository.save(user);

		Board board = Board.builder()
				.boardTitle("Test Title")
				.boardContent("This is a test content.")
				.category(Category.ADOPT)
				.user(user)
				.build();
		boardRepository.save(board);

		Comment comment = Comment.builder()
				.board(board)
				.user(user)
				.commContent("댓글1")
				.build();
		commentRepository.save(comment);

		Image image = Image.builder()
				.imageName("이미지.png")
				.imageUuid("UUID1")
				.imageUrl("URL1")
				.board(board)
				.build();
		imageRepository.save(image);
	}
}
