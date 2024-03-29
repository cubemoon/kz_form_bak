package com.kuaizhan.form.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import spark.route.RouteMatch;

public class SurveysHandlerTest {
	
	private static String surveyPostJson = null;
	
	SurveysHandler surveysHandler = new SurveysHandler();

	@BeforeClass
	public static void loadJsonFiles(){
		try {
			surveyPostJson = FileUtils.readFileToString(new File(SurveysHandlerTest.class.getClassLoader().getResource("survey.json").getPath()), "UTF-8");
			System.out.println(String.format("survey json = \n%s\n", surveyPostJson));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_postSurvey() {
	    RouteMatch match =  new RouteMatch(HttpMethod.post,null,"/surveys","/surveys", "text/json");
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("DATA", new String[] {surveyPostJson});
		
		HttpServletRequest servletRequest = new MockedHttpServletRequest(params);
		Request request = null;// = new Request(match, servletRequest);
		Response response = null;
		
		surveysHandler.post.handle(request,response);
		
//		String name = request.queryParams("name");
//		assertEquals("Invalid name in query string", "Federico", name);
	}

	
    public static class MockedHttpServletRequest implements HttpServletRequest {
        private Map<String, String[]> params;

        public MockedHttpServletRequest(Map<String, String[]> params) {
            this.params = params;
        }

        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public Cookie[] getCookies() {
            return null;
        }

        @Override
        public long getDateHeader(String name) {
            return 0;
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            return null;
        }

        @Override
        public int getIntHeader(String name) {
            return 0;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return null;
        }

        @Override
        public StringBuffer getRequestURL() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getServletPath() {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        @Deprecated
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return this.params.get(name)[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return this.params;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        @Deprecated
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public void removeAttribute(String name) {
            // do nothing
        }

        @Override
        public void setAttribute(String name, Object o) {
        	// do nothing
        }

        @Override
        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        	// do nothing
        }

		@Override
		public ServletContext getServletContext() {
			return null;
		}

		@Override
		public AsyncContext startAsync() throws IllegalStateException {
			return null;
		}

		@Override
		public AsyncContext startAsync(ServletRequest servletRequest,
				ServletResponse servletResponse) throws IllegalStateException {
			return null;
		}

		@Override
		public boolean isAsyncStarted() {
			return false;
		}

		@Override
		public boolean isAsyncSupported() {
			return false;
		}

		@Override
		public AsyncContext getAsyncContext() {
			return null;
		}

		@Override
		public DispatcherType getDispatcherType() {
			return null;
		}

		@Override
		public boolean authenticate(HttpServletResponse response)
				throws IOException, ServletException {
			return false;
		}

		@Override
		public void login(String username, String password)
				throws ServletException {
			// do nothing
		}

		@Override
		public void logout() throws ServletException {
			// do nothing
		}

		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			return null;
		}

		@Override
		public Part getPart(String name) throws IOException, ServletException {
			return null;
		}
        
    }
}
