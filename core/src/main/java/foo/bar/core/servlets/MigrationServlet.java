package main.java.foo.bar.core.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.pipes.Pipe;
import org.apache.sling.pipes.Plumber;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {

"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "migration-tool/components/migrateComponent",
		"sling.servlet.extensions=" + "html" })
public class MigrationServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(MigrationServlet.class);

	// Can be any pipe
	private static final String PIPEPATH = "/etc/pipes/content/deleteDsStore";

	@Reference
	Plumber plumber;

	@Override
	protected void doGet(final SlingHttpServletRequest request,
			final SlingHttpServletResponse response) throws ServletException,
			IOException {
		StringBuilder responseString = new StringBuilder();

		ResourceResolver resourceResolver = request.getResourceResolver();

		Resource pipeResource = resourceResolver.getResource(PIPEPATH);
		try {
			Pipe containerPipe = plumber.getPipe(pipeResource);
			if (containerPipe != null) {
				Set<String> results = plumber.execute(resourceResolver,
						containerPipe, null, true);
				for (String result : results) {
					responseString.append(result);
					responseString.append("<br/>");
				}
			} else {
				responseString.append("Pipe is null!");
			}
			response.setStatus(SlingHttpServletResponse.SC_OK);
		} catch (Exception e) {
			LOG.error("Exception occourd: ",e);
			responseString.append("Exception occourd: ");
			responseString.append(e.getLocalizedMessage());
			
		
			response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		response.setContentType("text/html");
		response.getWriter().print(responseString.toString());
	}
}
