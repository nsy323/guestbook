package org.nsy.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor     //모든필드값을 파라미터로 받는 생성자 만듬
@Data                   //@Getter,@Setter,@ToString,@EqualAndHashCode, @RequiredArgsConstructor를 합쳐놓은 어노테이션
public class PageRequestDTO {

    private int page;
    private int size;
    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }
}
