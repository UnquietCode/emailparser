package com.book.util;

import java.lang.reflect.Field;

/**
 * 
 * @author zhangzuoqiang
 */
public class Utils {

	/**
	 * @do 根据一个model里的值更新另一个model里的值，如果newFieldModel的某个字段为null就不更新，表示以src的值为准 。
	 *     典型的应用是在保存一个POJO时，从页面传过来的POJO只包含了要更新的值，而我们的DAO的更新语句一般都是把字段都带上了的
	 *     所以这时候就需要从数据库查询出model，然后将页面的POJO的不为null值赋值给数据库的POJO，然后保存至数据库。
	 * @Modify
	 * @param src
	 * @param newFieldModel
	 */
	public static <T> void updateModel(final T src, final T newFieldModel) {
		final Field[] fields = src.getClass().getDeclaredFields();
		for (final Field f : fields) {
			f.setAccessible(true);
			try {
				final Object value = f.get(newFieldModel);
				if (value != null) {
					f.set(src, value);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
