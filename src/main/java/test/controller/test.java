package test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @GetMapping(path="/fix", produces = "application/json")
    public String testIssues(@RequestParam(value="issueId") Integer issueId, @RequestParam(value="sandBoxUrl") String sandBoxUrl) {
        return "";
    }
}
