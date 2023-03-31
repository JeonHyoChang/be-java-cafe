package kr.codesqaud.cafe.controller;

import kr.codesqaud.cafe.domain.Reply;
import kr.codesqaud.cafe.domain.User;
import kr.codesqaud.cafe.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static kr.codesqaud.cafe.service.SessionUtil.getUserInfo;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/articles/{articleId}")
public class RestController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ReplyService replyService;

    @Autowired
    public RestController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("")
    public Reply create(@ModelAttribute Reply reply, @PathVariable long articleId, HttpSession session) {
        User sessionUser = (User) getUserInfo(session);

        if (sessionUser == null) {
            return null;
        }

        reply.setArticleId(articleId);
        reply.setWriter(sessionUser.getName());
        return replyService.write(reply);
    }
}