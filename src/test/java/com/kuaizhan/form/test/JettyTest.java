package com.kuaizhan.form.test;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JettyTest {

	static class MyHandler extends AbstractHandler {
		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, new MultipartConfigElement(
					System.getProperty("java.io.tmpdir")));
			Part f = request.getPart("f");
			InputStream in = f.getInputStream();
			byte[] bytes = new byte[1024];
			int size = in.read(bytes);
			System.out.write(bytes, 0, size);
			System.out.println();
			response.setStatus(200);
			response.getWriter().close();
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		server.setHandler(new MyHandler());
		server.start();
		server.join();
	}
}
