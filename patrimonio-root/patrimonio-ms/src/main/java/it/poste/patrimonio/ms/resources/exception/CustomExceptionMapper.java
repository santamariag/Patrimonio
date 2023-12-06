package it.poste.patrimonio.ms.resources.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.eclipse.jetty.http.HttpStatus;

public class CustomExceptionMapper implements ExceptionMapper<Exception>{

	//Just  some examples
	@Override
	public Response toResponse(Exception exception) {
		
		Response response=Response.serverError().build();
		
		if(exception instanceof NullPointerException) {
			response=Response.status(HttpStatus.BAD_REQUEST_400)
					.entity(new it.poste.patrimonio.itf.api.Response("NPE "+exception.getMessage())).build();
		}
		
		if(exception instanceof WebApplicationException) {
			response=Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
					.entity(new it.poste.patrimonio.itf.api.Response("WAE "+exception.getMessage())).build();
		}
		
		return response;
	}

}
