
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java"
	import="java.sql.*,java.util.*,java.text.SimpleDateFormat" errorPage=""
	session="false"%>

<jsp:useBean id="bAdministrarPublicaciones"
	class="beans.AdministrarPublicaciones" scope="page" />


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	String registros = request.getParameter("registros");
	


	String encuesta = "";
	String pregunta = "";
	String opcion = "";
	
	int exito = 0; 
	if(registros!=null){
		
		for(int i=1; i<=Integer.parseInt(registros); i++){
			
			 encuesta = request.getParameter("encuesta"+i);
			 pregunta = request.getParameter("pregunta"+i);
			 opcion = request.getParameter("opcion"+i);
			
			 bAdministrarPublicaciones.crearRespuesta(encuesta, pregunta, opcion);
		}
		
		exito = 1; 
		
		
	}





%>

<input name="hdnExito" type="hidden" value="<%=exito%>"
	id="hdnExito" />







