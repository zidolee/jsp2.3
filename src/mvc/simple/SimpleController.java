package mvc.simple;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebServlet("/simple")
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1단계 - 클라이언트로부터 요청을 받음
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1단계 - 클라이언트로부터 요청을 받음
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 2단계 - 요청 파악
		String type = request.getParameter("tpye");
		
		// 3단계 - 요청한 기능을 수행한다.(보통은 모델을 통해서 수행)
		Object resultObject = null;
		if(type == null || type.equals("greeting")) {
			resultObject = "안녕하세요";
		} else if(type.equals("date")) {
			resultObject = new java.util.Date();
		} else {
			resultObject = "Invlid Type";
		}
		
		// 4단계 - request나 session에 처리결과를 저장
		request.setAttribute("result", resultObject);
		
		// 5단계 - RequestDispatcher를 사용하여 알맞은 뷰로 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/simpleView.jsp");
		dispatcher.forward(request, response);
	}
}
