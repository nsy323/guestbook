package org.nsy.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nsy.guestbook.dto.GuestBookDTO;
import org.nsy.guestbook.dto.PageRequestDTO;
import org.nsy.guestbook.entity.Guestbook;
import org.nsy.guestbook.service.GuestBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestBookService service; //final로 선언

    @GetMapping("/")
    public String index(){

        return "redirect:/guestbook/list";
    }

    /**
     * 방명록 list 조회
     * @param pageRequestDTO
     * @param model
     */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list....................." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }
    
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    /**
     * 방명록 등록
     * @param dto
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String registerPost(GuestBookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto......." + dto);
        
        //새로 추가된 엔티티 번호
        Long gno = service.register(dto);
        
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
        
    }

    /**
     * 방명록 상세 조회
     * @param gno
     * @param requestDTO
     * @param model
     */
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno : " + gno);

        GuestBookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    /**
     * 방명록 삭제
     * @param gno
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes){
        log.info("gno : " + gno);

        service.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(
            GuestBookDTO dto,
            @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
            RedirectAttributes redirectAttributes) {

        log.info("post modify......................");
        log.info("dto : " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getType());
        redirectAttributes.addAttribute("gno",dto.getGno());

        return "redirect:/guestbook/read";
    }
}
