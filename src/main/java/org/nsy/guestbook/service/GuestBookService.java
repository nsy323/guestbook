package org.nsy.guestbook.service;

import org.nsy.guestbook.dto.GuestBookDTO;
import org.nsy.guestbook.dto.PageRequestDTO;
import org.nsy.guestbook.dto.PageResultDTO;
import org.nsy.guestbook.entity.Guestbook;

public interface GuestBookService {

    Long register(GuestBookDTO dto);

    PageResultDTO<GuestBookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO);

    /**
     * dto객체를 entity객체로 변환
     * @param dto
     * @return
     */
    default Guestbook dtoToEntity(GuestBookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    /**
     * entity 객체를 dto로 변환
     * @param entity
     * @return
     */
    default GuestBookDTO entityToDto(Guestbook entity){
        GuestBookDTO dto = GuestBookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }

    /**
     * 방명록 선택한 건 조회
     * @param gno
     * @return
     */
    GuestBookDTO read(Long gno);

    /**
     * 방명록 수정
     * @param dto
     */
    void modify(GuestBookDTO dto);

    /**
     * 방명록 삭제
     * @param gno
     */
    void remove(Long gno);

}
