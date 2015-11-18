package com.kuaizhan.form.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Route;

import com.kuaizhan.form.common.IOUtil;
import com.kuaizhan.form.common.ImageUtil;
import com.kuaizhan.form.conf.Config;

@Slf4j
public class QRCodeHandler implements Route {
	
	@SuppressWarnings("serial")
	LinkedHashMap<Long, byte[]> imageCache = new LinkedHashMap<Long, byte[]>(1000,0.75f, true){
	    protected boolean removeEldestEntry(Map.Entry<Long,byte[]> eldest) {
	        return true;
	    }
	};
	
	@Override
	public Object handle(Request request, Response response) {
		long surveyId = Long.parseLong(request.params(":id"));
		response.type("image/png");
		try {
			byte[] qrcodeImage = getImage(surveyId);
			OutputStream out = response.raw().getOutputStream();
			IOUtil.copy(IOUtil.fromBytes(qrcodeImage), out);
			out.close();
		} catch (IOException e) {
			log.error("Response QRCode error", e);
		}
		return null;
	}
	
	private byte[] getImage(long surveyId) throws IOException{
		byte[] data = imageCache.get(surveyId);
		if(data != null){
			return  data;
		}
		
		String surveyUrl = String.format("http://form.%s/forms/%s", Config.get("domain"), surveyId);
//		BufferedImage icon = ImageIO.read(new URL(surveyUrl));
		String qrcodeData = surveyUrl;
		data = ImageUtil.buildQrcode(null, qrcodeData);
		imageCache.put(surveyId, data);
		return data;
	}

}
