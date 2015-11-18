package com.kuaizhan.form.test;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

class AB {
	Long a;
	String b;
}

public class JsonTest {

	private Gson gson;

	@Before
	public void init() {
		gson = new Gson();
	}

	@Test
	public void fromJson() {
		String str = "{\"a\":1, \"b\":\"aaa\", \"c\":12}";
		AB ab = gson.fromJson(str, AB.class);

		System.out.println(ab);
		System.out.println(ab.a);
		System.out.println(ab.b);
	}

}
