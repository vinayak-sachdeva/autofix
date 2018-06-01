//package autofix.duplicate.Controller;
//
//import autofix.duplicate.categorizer.Categorizer;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AppController {
//    @PostMapping(value = "/testdup", produces = "application/json")
//    public String testDuplicate(
//            @RequestParam String boltURL,
//            @RequestParam String file1, @RequestParam Integer startLine1, @RequestParam Integer endLine1,
//            @RequestParam String file2, @RequestParam Integer startLine2, @RequestParam Integer endLine2
//    ) {
//        return Categorizer.builder()
//                .boltURL(boltURL)
//                .file1(file1)
//                .startLine1(startLine1)
//                .endLine1(endLine1)
//                .file2(file2)
//                .startLine2(startLine2)
//                .endLine2(endLine2)
//                .build()
//                .testMethod();
//    }
//}
