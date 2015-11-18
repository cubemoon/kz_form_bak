package com.kuaizhan.form.test;

import java.util.List;

import org.junit.Test;

import com.kuaizhan.form.migrate.AnswerOld;
import com.kuaizhan.form.migrate.DataExportDao;

public class DataExportDaoTest {

	DataExportDao dao = new DataExportDao();
	
	@Test
	public void getAllOldAnswerTest(){
		List<AnswerOld>list = dao.getAllAnswer();
		System.out.println(list.size());
	}
}
