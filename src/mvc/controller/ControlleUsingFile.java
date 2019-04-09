package mvc.controller;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.corba.se.impl.activation.CommandHandler;

@WebServlet("/ControlleUsingFile")
public class ControlleUsingFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
	public void init() throws ServletException {
		String configFile = getInitParameter("configFile");
		Properties prop = new Properties();
		String configFilePath = getServletContext().getRealPath(configFile);
		try (FileInputStream fis = new FileReader(configFilePath)) {
			prop.load(fis);
		} catch (IOException e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		Iterator keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			String command = (String)keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				Class<?> hanlClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance =  (CommandHandler)handlerClassName.newInstace();
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationError | IllegalAccessError e) {
				throw new ServletException(e);
			}
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("cmd");
		CommandHandler handler = commandHandlerMap.get(command);
		if(handler == null) {
			handler = new NullHandler();
		}
		
		String viewPage = null;
		try {
			viewPage = handler.process(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if(viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}


}
