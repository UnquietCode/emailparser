/**
 * 拦截器放在controllers包下，称为局部拦截器，局部拦截器只作用于所在目录以及子目录的控制器
 */
package com.book.controllers;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.LogDAO;
import com.book.model.Log;
import com.book.model.User;

/**
 * @do 通过扩展 ControllerInterceptorAdapter 实现一个拦截器类；
 * @Modify
 * @author zhangzuoqiang
 */
public class AutoLogInterceptor extends ControllerInterceptorAdapter {

	@Autowired
	private LogDAO logDao;

	/**
	 * afterCompletion 在整个页面渲染完毕或者因异常导致流程被中断后执行
	 * 无论请求正常或异常地被处理，每一个拦截器的afterCompletion都会被执行
	 */
	@Override
	public void afterCompletion(final Invocation inv, final Throwable ex)
			throws Exception {
		final String uid = inv.getResourceId();
		final String uri = inv.getRequestPath().getUri();
		final boolean success = (ex == null);
		final String remarks = success ? "suceess" : ex.getMessage();

		final Log log = new Log();
		log.setResourceId(uri);
		log.setResourcePattern(uid);
		log.setSuccess(success);
		log.setRemarks(remarks);

		final User loginUser = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		if (loginUser != null) {
			log.setUserName(loginUser.getName());
		} else {
			log.setUserName(inv.getRequest().getRemoteAddr());
		}

		// 定义任务
		final Runnable task = new Runnable() {

			public void run() {
				AutoLogInterceptor.this.logDao.save(log);
			}
		};

		// 将插入到数据库的操作提交executorService做异步更新
		// 在实际场景中，这种方式要注意webapp shutdown的时候，还未执行的Task的处理问题
		final Thread saveLog = new Thread(task);
		saveLog.start();
	}
}
