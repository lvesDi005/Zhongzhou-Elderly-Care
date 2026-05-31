package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.LoginDto;
import com.zzyl.entity.User;
import com.zzyl.mapper.UserMapper;
import com.zzyl.utils.JwtUtil;
import com.zzyl.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sjqn
 */
@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/security/login")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        if (username == null || username.isEmpty()) {
            return ResponseResult.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return ResponseResult.error("密码不能为空");
        }

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResponseResult.error("用户不存在");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseResult.error("密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());

        String token = JwtUtil.createJWT("itheima", 300000, claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("avatar", user.getAvatar());
        result.put("nickName", user.getNickName());

        return ResponseResult.success(result);
    }

    @GetMapping("/getInfo")
    public ResponseResult<Map<String, Object>> getInfo(@RequestHeader(name = "Authorization", required = false) String token) {
        String username = null;
        if (token != null && !token.isEmpty()) {
            try {
                String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
                Claims claims = JwtUtil.parseJWT("itheima", jwt);
                username = (String) claims.get("username");
            } catch (Exception e) {
                // token parse failed
                throw new RuntimeException("系统繁忙，请稍后再试！");
            }
        }
        if (username == null) {
            Map<String, ? extends Serializable> stringMap = Map.of(
                    "avatar", "", "realName", "", "name", "",
                    "icon", "", "id", null, "roleName", "",
                    "type", 0, "requestId", ""
            );
            return ResponseResult.success(stringMap);
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            Map<String, ? extends Serializable> stringMap = Map.of(
                    "avatar", "", "realName", "", "name", "",
                    "icon", "", "id", null, "roleName", "",
                    "type", 0, "requestId", ""
            );
            return ResponseResult.success(stringMap);
        }
        Map<String, ? extends Serializable> stringMap = Map.of(
                "avatar", user.getAvatar() != null ? user.getAvatar() : "",
                "realName", user.getRealName() != null ? user.getRealName() : "",
                "name", user.getRealName() != null ? user.getRealName() : "",
                "icon", user.getAvatar() != null ? user.getAvatar() : "",
                "id", user.getId() != null ? String.valueOf(user.getId()) : "",
                "roleName", "超级管理员",
                "type", 0,
                "requestId", ""
        );
        return ResponseResult.success(stringMap);
    }

    @GetMapping("/resource/menus")
    public String menus(){
        return menu;

    }

    static String menu = "{\n" +
            "    \"code\": 200,\n" +
            "    \"msg\": \"操作成功\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"name\": \"工作台\",\n" +
            "            \"path\": \"dashboard\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/工作台\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"工作台\",\n" +
            "                    \"path\": \"/dashboard/base\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/工作台/工作台\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": null,\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"工作台\",\n" +
            "                        \"icon\": \"hlrw\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"工作台\",\n" +
            "                \"icon\": \"hlrw\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"来访管理\",\n" +
            "            \"path\": \"appointment\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/来访管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"预约来访\",\n" +
            "                    \"path\": \"/appointment/yylf\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/来访管理/预约来访\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"预约登记\",\n" +
            "                            \"path\": \"/appointment/subscribe\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/预约来访/预约登记\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"预约登记\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"来访登记\",\n" +
            "                            \"path\": \"/appointment/comeVisit\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/预约来访/来访登记\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"来访登记\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"预约来访\",\n" +
            "                        \"icon\": \"cwgl-sei\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"来访管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"入退管理\",\n" +
            "            \"path\": \"enterQuit\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/入退管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"入住管理\",\n" +
            "                    \"path\": \"/enterQuit/enter\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/入退管理/入住管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"入住办理\",\n" +
            "                            \"path\": \"/enterQuit/enterManage\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/入住管理/入住办理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"入住办理\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"入住管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"退住管理\",\n" +
            "                    \"path\": \"/enterQuit/quit\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/入退管理/退住管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"退住办理\",\n" +
            "                            \"path\": \"/enterQuit/quitManage\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/退住管理/退住办理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"退住办理\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"退住管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"入退管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"在住管理\",\n" +
            "            \"path\": \"liveIn\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/在住管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"合同管理\",\n" +
            "                    \"path\": \"/liveIn/htgl\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/在住管理/合同管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"合同跟踪\",\n" +
            "                            \"path\": \"/liveIn/trackAfter\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/合同管理/合同跟踪\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"合同跟踪\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"合同管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"床位管理\",\n" +
            "                    \"path\": \"/liveIn/cwgl\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/在住管理/床位管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"床位房型\",\n" +
            "                            \"path\": \"/liveIn/houseType\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/床位管理/床位房型\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"床位房型\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"智能床位\",\n" +
            "                            \"path\": \"/liveIn/smartBed\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/床位管理/智能床位\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"智能床位\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"房型设置\",\n" +
            "                            \"path\": \"/liveIn/houseSet\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/床位管理/房型设置\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"房型设置\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"床位管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"请假管理\",\n" +
            "                    \"path\": \"/liveIn/qjgl\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/在住管理/请假管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"请假管理\",\n" +
            "                            \"path\": \"/liveIn/qjgl\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/请假管理/请假管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"请假管理\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"请假管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"在住管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"服务管理\",\n" +
            "            \"path\": \"serve\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/服务管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"护理计划\",\n" +
            "                    \"path\": \"/serve/hljh-1\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/服务管理/护理计划\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"护理等级\",\n" +
            "                            \"path\": \"/serve/grade\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/护理计划/护理等级\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"护理等级\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"护理计划\",\n" +
            "                            \"path\": \"/serve/nursePlan\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/护理计划/护理计划\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"护理计划\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"护理项目\",\n" +
            "                            \"path\": \"/serve/nurseProject\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/护理计划/护理项目\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"护理项目\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"护理计划\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"护理任务\",\n" +
            "                    \"path\": \"/serve/hlrw\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/服务管理/护理任务\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"负责老人\",\n" +
            "                            \"path\": \"/serve/fzlr\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/护理任务/负责老人\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"负责老人\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"任务安排\",\n" +
            "                            \"path\": \"/serve/rwap\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/护理任务/任务安排\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"任务安排\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"护理任务\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"服务管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"订单管理\",\n" +
            "            \"path\": \"order\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/订单管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"订单管理\",\n" +
            "                    \"path\": \"/order/order\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/订单管理/订单管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"订单管理\",\n" +
            "                            \"path\": \"/order/olist\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/订单管理/订单管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"订单管理\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"退款管理\",\n" +
            "                            \"path\": \"/order/refund\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/订单管理/退款管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"退款管理\",\n" +
            "                                \"icon\": \"cwgl-sei\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"订单管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"订单管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"财务管理\",\n" +
            "            \"path\": \"finance\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/财务管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"账单管理\",\n" +
            "                    \"path\": \"/finance/bill\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/财务管理/账单管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"入账列表\",\n" +
            "                            \"path\": \"/finance/enterAccount\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/账单管理/入账列表\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"入账列表\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"欠费老人\",\n" +
            "                            \"path\": \"/finance/arrearage\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/账单管理/欠费老人\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"欠费老人\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"账单管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"预存管理\",\n" +
            "                    \"path\": \"/finance/prestore\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/财务管理/预存管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"预缴款充值\",\n" +
            "                            \"path\": \"/finance/topUp\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/预存管理/预缴款充值\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"预缴款充值\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"余额查询\",\n" +
            "                            \"path\": \"/finance/balance\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/预存管理/余额查询\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"余额查询\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"预存管理\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"财务管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"客户管理\",\n" +
            "            \"path\": \"client\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/客户管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"客户信息\",\n" +
            "                    \"path\": \"/client/khxx\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/客户管理/客户信息\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"客户信息\",\n" +
            "                            \"path\": \"/client/list\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/客户信息/客户信息\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"客户信息\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"客户信息\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"客户管理\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"权限管理\",\n" +
            "            \"path\": \"permission\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/权限管理\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"用户管理\",\n" +
            "                    \"path\": \"/permission/user\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/权限管理/用户管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"用户信息\",\n" +
            "                            \"path\": \"/permission/user\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/用户管理/用户信息\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"用户信息\",\n" +
            "                                \"icon\": \"user\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"用户管理\",\n" +
            "                        \"icon\": \"\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"权限配置\",\n" +
            "                    \"path\": \"/permission/permission\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/权限管理/权限配置\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"角色管理\",\n" +
            "                            \"path\": \"/permission/role\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/权限配置/角色管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"角色管理\",\n" +
            "                                \"icon\": \"component\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"菜单管理\",\n" +
            "                            \"path\": \"/permission/menu\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/权限配置/菜单管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"菜单管理\",\n" +
            "                                \"icon\": \"user\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"部门管理\",\n" +
            "                            \"path\": \"/permission/dept\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/权限配置/部门管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"部门管理\",\n" +
            "                                \"icon\": \"form\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"职位管理\",\n" +
            "                            \"path\": \"/permission/post\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/权限配置/职位管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"职位管理\",\n" +
            "                                \"icon\": \"chart\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"权限配置\",\n" +
            "                        \"icon\": \"component\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"权限管理\",\n" +
            "                \"icon\": \"user\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"协同工作\",\n" +
            "            \"path\": \"active\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/协同工作\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"协同工作\",\n" +
            "                    \"path\": \"/active/xtgz\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/协同工作/协同工作\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"我的待办\",\n" +
            "                            \"path\": \"/active/backlogAfter\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/协同工作/我的待办\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"我的待办\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"我的申请\",\n" +
            "                            \"path\": \"/active/apply\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/协同工作/我的申请\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"我的申请\",\n" +
            "                                \"icon\": \"icon\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"协同工作\",\n" +
            "                        \"icon\": \"icon\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"协同工作\",\n" +
            "                \"icon\": \"icon\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"智能监测\",\n" +
            "            \"path\": \"intelligence\",\n" +
            "            \"component\": null,\n" +
            "            \"redirect\": \"/智能监测\",\n" +
            "            \"hidden\": null,\n" +
            "            \"children\": [\n" +
            "                {\n" +
            "                    \"name\": \"设备管理\",\n" +
            "                    \"path\": \"/intelligence/equi\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/智能监测/设备管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"设备管理\",\n" +
            "                            \"path\": \"/intelligence/equipment\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/设备管理/设备管理\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"设备管理\",\n" +
            "                                \"icon\": \"hlrw\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"设备管理\",\n" +
            "                        \"icon\": \"hlrw\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"报警管理\",\n" +
            "                    \"path\": \"/intelligence/warn\",\n" +
            "                    \"component\": null,\n" +
            "                    \"redirect\": \"/智能监测/报警管理\",\n" +
            "                    \"hidden\": null,\n" +
            "                    \"children\": [\n" +
            "                        {\n" +
            "                            \"name\": \"报警数据\",\n" +
            "                            \"path\": \"/intelligence/facility\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/报警管理/报警数据\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"报警数据\",\n" +
            "                                \"icon\": \"\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"name\": \"报警规则\",\n" +
            "                            \"path\": \"/intelligence/rule\",\n" +
            "                            \"component\": null,\n" +
            "                            \"redirect\": \"/报警管理/报警规则\",\n" +
            "                            \"hidden\": null,\n" +
            "                            \"children\": null,\n" +
            "                            \"meta\": {\n" +
            "                                \"title\": \"报警规则\",\n" +
            "                                \"icon\": \"\",\n" +
            "                                \"roles\": null\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"meta\": {\n" +
            "                        \"title\": \"报警管理\",\n" +
            "                        \"icon\": \"cwgl-sei\",\n" +
            "                        \"roles\": null\n" +
            "                    }\n" +
            "                }\n" +
            "            ],\n" +
            "            \"meta\": {\n" +
            "                \"title\": \"智能监测\",\n" +
            "                \"icon\": \"hlrw\",\n" +
            "                \"roles\": null\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"operationTime\": null\n" +
            "}";
}
