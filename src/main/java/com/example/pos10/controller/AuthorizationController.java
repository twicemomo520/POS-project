package com.example.pos10.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.entity.Authorization;
import com.example.pos10.service.ifs.AuthorizationService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.InsertAuthorizationReq;
import com.example.pos10.vo.UpdateAuthorizationReq;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {
    
    @Autowired
    private AuthorizationService authorizationService;
    
    // 新增權限
    @PostMapping("/insert")
    public BasicRes insertAuthorization(@RequestBody InsertAuthorizationReq req) {
        return authorizationService.insertAuthorization(req);
    }
    
    // 查詢所有權限
    @GetMapping("/all")
    public List<Authorization> findAllAuthorizations() {
        return authorizationService.findAllAuthorizations();
    }

    // 更新權限
    @PostMapping("/update")
    public BasicRes updateAuthorization(@RequestBody UpdateAuthorizationReq req) {
        return authorizationService.updateAuthorization(req);
    }

    // 刪除權限
    @DeleteMapping("/delete/{id}")
    public BasicRes deleteAuthorization(@PathVariable int id) {
        return authorizationService.deleteAuthorization(id);
    }
}
