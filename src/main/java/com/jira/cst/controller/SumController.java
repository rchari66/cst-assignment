package com.jira.cst.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jira.cst.constants.Endpoints;
import com.jira.cst.service.SumService;

import io.micrometer.core.instrument.util.StringUtils;

@RestController
public class SumController {

    @Autowired
    private SumService sumService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from CST application!\n";
    }

    @GetMapping(Endpoints.Issue.SUM)
    public ResponseEntity<String> publishStoryPointsSum(@RequestParam(value = Endpoints.Issue.QParam.QUERY) String query,
                                                @RequestParam(value = Endpoints.Issue.QParam.NAME) String name) throws Exception {

        if (StringUtils.isEmpty(query) || StringUtils.isEmpty(name)) {
            return new ResponseEntity<>("Error: Query parameters can not be null/empty", HttpStatus.BAD_REQUEST);
        }

        this.sumService.processQuery(query, name);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
