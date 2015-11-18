package com.kuaizhan.form.migrate;

import java.util.List;

import com.kuaizhan.form.dao.AnswerDao;
import com.kuaizhan.form.model.Answer;

public class FormDataImport {

	DataExportDao dao = null;
	AnswerDao aDao = null;
	
	public FormDataImport(){
		if(dao==null){
			dao = new DataExportDao();
		}
		if(aDao == null){
			aDao = new AnswerDao();
		}
	}
	
	public void migrate(){
		List<AnswerOld>list = dao.getAllAnswer();
		List<Answer> answers = ObjectConvertHelper.insertAnswers(list);
		if(answers == null || answers.isEmpty()){
			System.out.println("Error");
			return;
		}
		for(Answer a:answers){
			aDao.save(a);
		}
		System.out.println("Finish");
	}
	
	public static void main(String[] args) {
		FormDataImport formDataImport = new FormDataImport();
		formDataImport.migrate();
	}
}
