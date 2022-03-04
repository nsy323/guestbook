package org.nsy.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.guestbook.dto.GuestBookDTO;
import org.nsy.guestbook.dto.PageRequestDTO;
import org.nsy.guestbook.dto.PageResultDTO;
import org.nsy.guestbook.entity.Guestbook;
import org.nsy.guestbook.entity.QGuestbook;
import org.nsy.guestbook.repository.GuestbookRepository;
import org.springframework.beans.propertyeditors.PathEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입(final필드나 @NotNull이 붙어 있는 필드만)
public class GuestBookServiceImpl implements GuestBookService {


    private final GuestbookRepository repository; //반드시 final로 선언

    @Override
    public Long register(GuestBookDTO dto) {

        log.info("DTO...................");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    /**
     * ㅓJPA의 처리 결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 List로 변환하고, 화면에 페이지 처리와 필요한 값들 생성
     *
     * @param requestDTO
     * @return
     */
    @Override
    public PageResultDTO<GuestBookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        Function<Guestbook, GuestBookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void modify(GuestBookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    /**
     * Querydsl 처리
     * @param requestDTO
     * @return
     */
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qGuestbook.gno.gt(0L);   // gno > 0 조건만 생성

        booleanBuilder.and(expression);

        if(type  == null || type.trim().length() == 0){ //검색 조건이 없는 경우
            return booleanBuilder;
        }
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;


    }
}
