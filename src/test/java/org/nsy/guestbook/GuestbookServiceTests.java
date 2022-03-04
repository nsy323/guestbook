package org.nsy.guestbook;

import org.junit.jupiter.api.Test;
import org.nsy.guestbook.dto.GuestBookDTO;
import org.nsy.guestbook.dto.PageRequestDTO;
import org.nsy.guestbook.dto.PageResultDTO;
import org.nsy.guestbook.entity.Guestbook;
import org.nsy.guestbook.service.GuestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuestbookServiceTests {
    @Autowired
    GuestBookService guestBookService;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<GuestBookDTO, Guestbook> resultDTO = guestBookService.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("-------------------------------------------------------");

        for(GuestBookDTO dto : resultDTO.getDtoList()){
            System.out.println(dto);
        }

        System.out.println("========================================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") //t,c,w,tc,tcw...
                .keyword("title")
                .build();

        PageResultDTO<GuestBookDTO, Guestbook> resultDTO = guestBookService.getList(pageRequestDTO);

        System.out.println("PREV : " + resultDTO.isPrev());
        System.out.println("NEXT : " + resultDTO.isNext());
        System.out.println("TOTAL : " + resultDTO.getTotalPage());

        System.out.println("----------------------------------------------");

        for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()){
            System.out.println(guestBookDTO);
        }

        System.out.println("----------------------------------------------");

        resultDTO.getPageList().forEach(i -> System.out.println(i));


    }


}
