package com.kuaizhan.form.controller;

import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

import com.kuaizhan.form.biz.SohuCloudService;
import com.kuaizhan.form.common.IOUtil;
import com.kuaizhan.form.common.KZUtil;
import com.kuaizhan.form.view.CodeMsg;

public class ImageUploadHandler implements Route {

	static final Logger log = LoggerFactory.getLogger(ImageUploadHandler.class);

	private SohuCloudService cloudService = new SohuCloudService();

	private String DEFAULT_TYPE = "image/png";
	private long SIZE_LIMIT = 5 * 1024 * 1024; // 10M
	
	@Override
	public Object handle(Request request, Response response) {
		@SuppressWarnings("unused")
		long siteId = KZUtil.getSiteId(request);

		try {
			Part imagePart = request.raw().getPart("image");
			byte[] imageBuf = IOUtil.toBytes(imagePart.getInputStream());
			if (imageBuf.length > SIZE_LIMIT) {
				return new CodeMsg(-1, String.format("图片大小限制为%dMB", SIZE_LIMIT / 1048576), null);
			}
			
			String key = cloudService.uploadPic(IOUtil.fromBytes(imageBuf), imageBuf.length, DEFAULT_TYPE);
			String url =  cloudService.getPicURL(key);
			return new CodeMsg(0, "OK", url);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return CodeMsg.failure("Bad file uploaded, error = " + e.getMessage());
		}

//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("url", cloudService.getPicURL(key));

	}

}
