package com.kuaizhan.form.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuaizhan.form.common.StringUtil;
import com.kuaizhan.form.dao.AnswerDao;
import com.kuaizhan.form.dao.SurveyDao;
import com.kuaizhan.form.model.Answer;
import com.kuaizhan.form.model.Survey;

@Slf4j
public class ExportUtils {

	static AnswerDao answerDao = new AnswerDao();

	static SurveyDao surveyDao = new SurveyDao();
	

	public static void exportSurvey(long siteId, long surveyId, Request request, Response response) throws IOException {
		try{
			HttpServletResponse resp = response.raw();
			Survey survey = surveyDao.getSimple(surveyId);

			String userAgent = request.headers("User-Agent");
			boolean isInternetExplorer = false;
			if (userAgent != null) {
				isInternetExplorer = (userAgent.indexOf("MSIE") > -1)  || (userAgent.indexOf("Trident") > -1);
			}
			log.info("export excel User-Agent " + userAgent);

			String encodeFileName = null;
			if (isInternetExplorer) {
				encodeFileName = URLEncoder.encode(survey.getTitle(), "UTF-8");			
			}else{
				encodeFileName = new String(survey.getTitle().getBytes("UTF-8"), "ISO-8859-1"); 
			}
			
			resp.addHeader("Content-Disposition", "attachment; filename=" +encodeFileName +  ".xls");
			resp.setContentType("application/vnd.ms-excel");
		
			OutputStream out = resp.getOutputStream();
			exportSurvey(siteId, surveyId, out);
			out.flush();
			out.close();
		}catch(WriteException e){
			throw new RuntimeException("export error " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void exportSurvey(long siteId, long surveyId, OutputStream out) throws IOException, RowsExceededException, WriteException {

		Survey survey = surveyDao.get(siteId, surveyId);
		List<Answer> list = answerDao.getFullBySurveyId(siteId, survey.getId());


		// 打开文件
		WritableWorkbook book = Workbook.createWorkbook(out);
		// 生成名为“第一页”的工作表，参数0表示这是第一页
		WritableSheet sheet = book.createSheet("第一页", 0);

		Label label = new Label(0, 0, "序号");
		sheet.addCell(label);

		//TODO 通过所有表单的answer,来收集fields，因为保存在survey中的fields 是可变的
		List<String> fieldIds = Lists.newArrayList();
		int ci = 0;
		for (Object object : survey.getFields()) {
			Map<String, Object> fieldMap = (Map<String, Object>) object;
			String fieldId = String.valueOf(fieldMap.get("id")).trim();
			String fieldName = String.valueOf(fieldMap.get("name"));
			String title = String.valueOf(fieldMap.get("title"));

			if ("id_section".equals(fieldName) || "id_picture".equals(fieldName)) {
				continue;
			}
			fieldIds.add(fieldId);

			ci++;
			label = new Label(ci, 0, title);
			sheet.addCell(label);

		}

		List<Map<String, String>> fieldList = getFieldData(list);
		int ri = 0;
		for (Map<String, String> fieldData : fieldList) {
			ri++;
			label = new Label(0, ri, String.valueOf(ri));
			sheet.addCell(label);

			ci = 0;
			for (String fieldId : fieldIds) {
				ci++;
				String value = fieldData.get(fieldId);
				if (value != null) {
					label = new Label(ci, ri, value);
					sheet.addCell(label);
				}
			}
		}

		// 写入数据并关闭文件
		book.write();
		book.close();
	}

	@SuppressWarnings("unchecked")
	private static List<Map<String, String>> getFieldData(List<Answer> list) {
		List<Map<String, String>> fieldList = Lists.newArrayList();
		for (Answer item : list) {
			Map<String, String> fieldData = Maps.newHashMap();
			fieldData.put("no", String.valueOf(item.getNo()));

			for (Object object : item.getFields()) {
				Map<String, Object> fieldMap = (Map<String, Object>) object;
				String fieldId = String.valueOf(fieldMap.get("id")).trim();
				String fieldName = String.valueOf(fieldMap.get("name"));
				if ("id_section".equals(fieldName) || "id_picture".equals(fieldName)) {
					continue;
				}

				String fieldValue = getFieldValue(fieldName, fieldMap);
				if (fieldValue != null) {
					fieldData.put(fieldId, fieldValue);
				}
			}
			fieldList.add(fieldData);
		}
		return fieldList;
	}

	@SuppressWarnings("unchecked")
	private static String getFieldValue(String fieldName,
			Map<String, Object> fieldMap) {

		if ("id_text".equals(fieldName) || "id_textarea".equals(fieldName)|| "id_image".equals(fieldName)) {
			// increase answer count;
			String value = (String) fieldMap.get("value");
			if (StringUtil.isNotEmpty(value)) {
				return value;
			}

		} else if ("id_radio".equals(fieldName)
				|| "id_checkbox".equals(fieldName)
				|| "id_dropdown".equals(fieldName)
				|| "id_image_radio".equals(fieldName)
				|| "id_image_checkbox".equals(fieldName)) {

			List<Map<String, Object>> values = (List<Map<String, Object>>) fieldMap.get("value");
			
			List<String> selectedValues = new ArrayList<String>();
			for (Map<String, Object> value : values) {
				int lid = Integer.parseInt(String.valueOf(value.get("lid")));
				boolean selected = (Boolean) value.get("selected");
				String valueName = (String) value.get("name");
				if (lid != -1 && selected) {
					selectedValues.add(valueName);
				}
			}

			if (!selectedValues.isEmpty()) {
				return Joiner.on(",").join(selectedValues);
			}

		} else if ("id_section".equals(fieldName) || "id_picture".equals(fieldName)) {
			// do nothing
		}

		return null;
	}

	public static void main(String[] args) throws RowsExceededException, WriteException, IOException {
		long siteId = 4748350670L;
		long surveyId = 340214647128854528L;
		Survey survey = surveyDao.getSimple(surveyId);
		ExportUtils.exportSurvey(siteId, surveyId, new BufferedOutputStream( new FileOutputStream(survey.getTitle() + ".xls")));
		System.out.println("*********************************************");
	}
}
