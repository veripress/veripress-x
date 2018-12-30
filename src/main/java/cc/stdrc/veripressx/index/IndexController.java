package cc.stdrc.veripressx.index;

import cc.stdrc.veripressx.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final PostRepository postRepository;

    @Autowired
    public IndexController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String indexGET(@RequestParam(required = false) Integer page, Model model) {
        if (page != null && page <= 0) {
            return "redirect:/";
        } else if (page == null) {
            page = 0;
        }
        PageRequest pageRequest = PageRequest.of(page, 2);
        model.addAttribute("posts", postRepository.findByOrderByPublishDateDesc(pageRequest));
        if (pageRequest.hasPrevious()) {
            int prevPage = pageRequest.previous().getPageNumber();
            String prevPageUrl = "/?page=" + prevPage;
            if (prevPage == 0) {
                prevPageUrl = "/";
            }
            model.addAttribute("prevPageUrl", prevPageUrl);
        }
        if (pageRequest.next().getOffset() < postRepository.count()) {
            model.addAttribute("nextPageUrl", "/?page=" + pageRequest.next().getPageNumber());
        }
        return "index";
    }
}
