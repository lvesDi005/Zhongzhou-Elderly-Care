package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.Dept;
import com.zzyl.entity.Post;
import com.zzyl.entity.User;
import com.zzyl.mapper.DeptMapper;
import com.zzyl.mapper.PostMapper;
import com.zzyl.mapper.UserMapper;
import com.zzyl.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息")
public class UserController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private PostMapper postMapper;

    @GetMapping("/current-user")
    @ApiOperation("获取当前用户信息")
    public ResponseResult<Map<String, Object>> currentUser(@RequestHeader(name = "Authorization", required = false) String token) {
        String username = "admin";
        if (token != null && !token.isEmpty()) {
            try {
                String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
                Claims claims = JwtUtil.parseJWT("itheima", jwt);
                username = (String) claims.get("username");
            } catch (Exception e) {
                // fallback to default
            }
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return success(Map.of(
                "realName", "", "avatar", "", "email", "",
                "mobile", "", "sex", "0", "id", null,
                "deptName", "", "postName", "", "roleNames", new ArrayList<>()
            ));
        }

        String deptName = "";
        if (user.getDeptNo() != null) {
            Dept dept = deptMapper.selectByDeptNo(user.getDeptNo());
            if (dept != null) deptName = dept.getDeptName();
        }
        String postName = "";
        if (user.getPostNo() != null) {
            Post post = postMapper.selectByPostNo(user.getPostNo());
            if (post != null) postName = post.getPostName();
        }

        List<String> roleNames = new ArrayList<>();
        roleNames.add("超级管理员");

        return success(Map.of(
            "realName", user.getRealName() != null ? user.getRealName() : "",
            "avatar", user.getAvatar() != null ? user.getAvatar() : "",
            "email", user.getEmail() != null ? user.getEmail() : "",
            "mobile", user.getMobile() != null ? user.getMobile() : "",
            "sex", user.getSex() != null ? user.getSex() : "0",
            "id", user.getId(),
            "deptName", deptName,
            "postName", postName,
            "roleNames", roleNames
        ));
    }

    @PatchMapping
    @ApiOperation("更新个人信息")
    public ResponseResult updatePersonal(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return error("用户不存在");
        }
        if (body.containsKey("avatar")) {
            user.setAvatar((String) body.get("avatar"));
        }
        if (body.containsKey("realName")) {
            user.setRealName((String) body.get("realName"));
        }
        if (body.containsKey("mobile")) {
            user.setMobile((String) body.get("mobile"));
        }
        if (body.containsKey("sex")) {
            user.setSex(String.valueOf(body.get("sex")));
        }
        userMapper.updateByPrimaryKeySelective(user);
        return success();
    }

    @PostMapping("/list")
    public ResponseResult<PageResponse<User>> queryUserList(@RequestParam(required = false) String deptNo){
        List<User> userList;
        if (deptNo != null && !deptNo.isEmpty()) {
            userList = userMapper.selectListByDeptNo(deptNo);
        } else {
            userList = List.of(userMapper.selectList());
        }
        return ResponseResult.success(PageResponse.of(userList));
    }
}
