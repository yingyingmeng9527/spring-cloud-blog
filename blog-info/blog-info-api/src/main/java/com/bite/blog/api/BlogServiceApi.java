package com.bite.blog.api;


import com.bite.blog.api.pojo.AddBlogInfoRequest;
import com.bite.blog.api.pojo.BlogInfoResponse;
import com.bite.blog.api.pojo.UpBlogRequest;
import com.bite.common.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "blog-service", path = "/blog")
public interface BlogServiceApi {
    @RequestMapping("/getList")
    Result<List<BlogInfoResponse>> getList();

    @RequestMapping("/getBlogDetail")
    Result<BlogInfoResponse> getBlogDeatail(@RequestParam("blogId") Integer blogId);
    @RequestMapping("/add")
    Result<Boolean> addBlog(@RequestBody AddBlogInfoRequest addBlogInfoRequest);

    @RequestMapping("/update")
    Result<Boolean> updateBlog(@RequestBody UpBlogRequest upBlogRequest);

    @RequestMapping("/delete")
    Result<Boolean> deleteBlog(@RequestParam("blogId") Integer blogId);

}
