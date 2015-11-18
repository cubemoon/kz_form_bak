package com.kuaizhan.form.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.kuaizhan.form.biz.SohuCloudService;

public class SohuCloudTest {

	private SohuCloudService service = new SohuCloudService();

	//	@Test
	public void test() throws FileNotFoundException {
		File file = new File("/home/shengzhong/images/1.jpg");
		InputStream is = new FileInputStream(file);
		String key = service.uploadPic(is, file.length(), "image/jpeg");
		System.out.println(key);
	}
}
