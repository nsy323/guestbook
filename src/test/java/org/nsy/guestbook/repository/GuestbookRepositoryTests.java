package org.nsy.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.nsy.guestbook.entity.Guestbook;
import org.nsy.guestbook.entity.QGuestbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    /**
     * guestbook 테이블에 300개 데이터 넣기
     */
    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1, 300).forEach( i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..........." + i)
                    .content("Content......."+ i)
                    .writer("uesr" + i % 10)
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });

    }

    /**
     * guestbook 테이블에 제목,내용 update
     */
    @Test
    public void updateTest(){
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){     //객체가 null이 아니면
           Guestbook guestbook = result.get();

           System.out.println(guestbook);
           guestbook.changeTitle("Change Title.......");
           guestbook.changeContent("Change Content.......");

           guestbookRepository.save(guestbook);
        }
    }

    /**
     * querydsl 단일항목 검색
     */
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;   //Q도메인 클래스 얻어오기

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();  //where 절에 들어가는 조건들을 넣어주는 컨테이너

        BooleanExpression expression = qGuestbook.title.contains(keyword);  //원하는 조건은 필드 값과 같이 결합해서 생성

        builder.and(expression);    //만들어진 조건은 where문에 and나 or같은 키워드와 결합

        //BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor인터페이스의 findAll()을 사용 가능함
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);    

        System.out.println("==============================================");
        System.out.println(result);
        System.out.println("==============================================");

    }

    /**
     * querydsl 다중항목 검색
     */
    @Test
    public void testQuery2(){
        Pageable pageAble = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));     //gno가 0보다 크다.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageAble);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }


}
