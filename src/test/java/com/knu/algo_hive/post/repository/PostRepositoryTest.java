package com.knu.algo_hive.post.repository;

import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.post.dto.PostSummaryResponse;
import com.knu.algo_hive.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member member1;
    private Member member2;

    private Post post1;
    private Post post2;
    private Post post3;
    private Post post4;

    @BeforeEach
    void setUp() {
        member1 = new Member("nickname1", "email1", "pwd");
        member2 = new Member("nickname2", "email2", "pwd");
        entityManager.persist(member1);
        entityManager.persist(member2);
        post1 = new Post("content1", "sumarry1", "thum", "title1", member1);
        post2 = new Post("content2", "sumarry2", "thum", "title2", member1);
        post3 = new Post("content3", "sumarry3", "thum", "title3", member2);
        post4 = new Post("content4", "sumarry4", "thum", "title4", member2);
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);
        entityManager.persist(post4);

        entityManager.flush();
    }

    @Test
    @DisplayName("모든 Post 요약 조회 테스트")
    void findPostSummariesPagedTest() {
        //givne
        Pageable pageable = PageRequest.of(0, 2);

        //when
        Page<PostSummaryResponse> result = postRepository.findPostSummariesPaged(pageable);

        //then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("특정 Post ID로 Post 상세 조회 테스트")
    void findByPostIdTest() {
        //given
        Long postId = post1.getId();

        //when
        Optional<Post> result = postRepository.findByPostId(postId);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(postId);
        assertThat(result.get().getTitle()).isEqualTo("title1");
        assertThat(result.get().getMember().getNickName()).isEqualTo("nickname1");
    }

    @Test
    @DisplayName("특정 작성자의 Post 요약 조회 테스트")
    void findPostSummariesByAuthorPagedTest() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        String nickname = "nickname1"; //member1 지칭

        //when
        Page<PostSummaryResponse> result = postRepository.findPostSummariesByAuthorPaged(pageable, nickname);

        //then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).author()).isEqualTo(nickname);
        assertThat(result.getContent().get(1).author()).isEqualTo(nickname);
        assertThat(result.getContent().get(0).title()).isEqualTo(post1.getTitle());
        assertThat(result.getContent().get(1).title()).isEqualTo(post2.getTitle());
    }
}