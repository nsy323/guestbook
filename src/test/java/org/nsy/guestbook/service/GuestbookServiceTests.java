package org.nsy.guestbook.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.jupiter.api.Test;
import org.nsy.guestbook.dto.GuestBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestBookService service;

    @Test
    public void testRegister(){
        GuestBookDTO guestBookDTO = GuestBookDTO.builder()
                .title("sample title............")
                .content("sample content..........")
                .writer("user0")
                .build();

        System.out.println(service.register(guestBookDTO));
    }
}
