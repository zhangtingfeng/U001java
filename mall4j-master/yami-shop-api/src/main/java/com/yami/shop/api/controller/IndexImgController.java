/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.yami.shop.bean.app.dto.IndexImgDto;
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.service.IndexImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "首页轮播图接口")
public class IndexImgController {
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private IndexImgService indexImgService;

    /**
     * 首页轮播图接口
     http://localhost:8086/indexImgs
     http://miyaapi.oa.eggsoft.cn/indexImgs
     [{
     "imgUrl": "http://img-test.gz-yami.com/2019/04/52b0082c60c04fc99dd03288548f2841.dpg",
     "seq": 120,
     "uploadTime": "2019-04-17 17:12:37",
     "type": 0,
     "relation": 76
     }, {
     "imgUrl": "http://img-test.gz-yami.com/2019/04/c2d830afc56d469f96825e91e464d155.jpg",
     "seq": 10,
     "uploadTime": "2019-04-22 09:57:34",
     "type": 0,
     "relation": 76
     }, {
     "imgUrl": "http://img-test.gz-yami.com/2019/04/f676a8a8dbcb4e5c9bc907ef059aedb9.jpg",
     "seq": 1,
     "uploadTime": "2018-12-20 15:22:19",
     "type": 0,
     "relation": 73
     }]


     */
    @GetMapping("/indexImgs")
    @ApiOperation(value = "首页轮播图", notes = "获取首页轮播图列表信息")
    public ResponseEntity<List<IndexImgDto>> indexImgs() {
        List<IndexImg> indexImgList = indexImgService.listIndexImgs();
        List<IndexImgDto> indexImgDtos = mapperFacade.mapAsList(indexImgList, IndexImgDto.class);
        return ResponseEntity.ok(indexImgDtos);
    }
}
