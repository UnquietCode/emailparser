package com.book.controllers;

import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

/**
 * 
 * @author xuze(cantellow)
 * @Email ze.xu@renren-inc.com
 * @time Aug 12, 2011 2:02:25 PM
 * @do 主页控制器，使用自定义路径映射@Path
 * @Modify
 */
@Path("")
public class HomeController {

	@Get("")
	public String redirect() {
		return "r:/Booklib/book";
	}
}
